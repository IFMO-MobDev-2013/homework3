package ru.georgeee.android.Silencio.utility.cacher;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: georgeee
 * Date: 29.09.13
 * Time: 19:37
 * To change this template use File | Settings | File Templates.
 */
public class FileCacherDbManager {
    protected FileCacherDbHelper dbHelper;
    protected ExecutorService executorService;
    protected FileCacher fileCacher;
    protected long byteLimit;
    private long downByteLimit;


    public FileCacherDbManager(Context context, long byteLimit, FileCacher fileCacher) {
        dbHelper = new FileCacherDbHelper(context);
        executorService = Executors.newSingleThreadExecutor();
        this.byteLimit = byteLimit;
        this.downByteLimit = byteLimit / 2;
        this.fileCacher = fileCacher;
    }

    public final class FileCacherContract {
        // To prevent someone from accidentally instantiating the contract class,
        // give it an empty constructor.
        public FileCacherContract() {
        }

        /* Inner class that defines the table contents */
        public abstract class CacheEntry implements BaseColumns {
            public static final String TABLE_NAME = "file_cache_entries";
            public static final String COLUMN_NAME_FILE_ID = "file_id";
            public static final String COLUMN_NAME_PATH = "path";
            public static final String COLUMN_NAME_SIZE = "size";
        }
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String VARCHAR_NOT_NULL_TYPE = " VARCHAR NOT NULL";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FileCacherContract.CacheEntry.TABLE_NAME + " (" +
                    FileCacherContract.CacheEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    FileCacherContract.CacheEntry.COLUMN_NAME_FILE_ID + INTEGER_TYPE + COMMA_SEP +
                    FileCacherContract.CacheEntry.COLUMN_NAME_PATH + VARCHAR_NOT_NULL_TYPE + COMMA_SEP +
                    FileCacherContract.CacheEntry.COLUMN_NAME_SIZE + INTEGER_TYPE + COMMA_SEP +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FileCacherContract.CacheEntry.TABLE_NAME;

    public class FileCacherDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "fileCacheReference.db";

        public FileCacherDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    public long insertNewPath(String path, long size) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FileCacherContract.CacheEntry.COLUMN_NAME_PATH, path);
        values.put(FileCacherContract.CacheEntry.COLUMN_NAME_SIZE, size);

        return db.insert(FileCacherContract.CacheEntry.TABLE_NAME, null, values);
    }

    public static final int trimCachesLimit = 10000;

    public Long findFileIdByPath(String path) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                FileCacherContract.CacheEntry.TABLE_NAME,
                new String[]{
                        FileCacherContract.CacheEntry.COLUMN_NAME_FILE_ID
                },
                FileCacherContract.CacheEntry.COLUMN_NAME_PATH + "=?",
                new String[]{path},
                null,
                null,
                null
        );
        if (cursor.isAfterLast()) return null;
        return cursor.getLong(0);
    }

    public long registerPath(String path) {
        Long fileId = findFileIdByPath(path);
        if (fileId == null) return insertNewPath(path, 0);
        return fileId;
    }

    public void updateSize(String path, long size) {
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FileCacherContract.CacheEntry.COLUMN_NAME_SIZE, size);
        writableDatabase.update(FileCacherContract.CacheEntry.TABLE_NAME,
                values,
                FileCacherContract.CacheEntry.COLUMN_NAME_PATH + "=?",
                new String[]{path});
    }

    public void trimCaches() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();
                SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
                long byte_sum = readableDatabase.query(
                        FileCacherContract.CacheEntry.TABLE_NAME,
                        new String[]{
                                "SUM(" + FileCacherContract.CacheEntry.COLUMN_NAME_SIZE + ")"
                        },
                        "",
                        new String[]{},
                        null,
                        null,
                        null
                ).getLong(0);
                if (byte_sum > byteLimit) {
                    Cursor cursor = readableDatabase.query(
                            FileCacherContract.CacheEntry.TABLE_NAME,  // The table to query
                            new String[]{
                                    FileCacherContract.CacheEntry.COLUMN_NAME_FILE_ID,
                                    FileCacherContract.CacheEntry.COLUMN_NAME_PATH,
                                    FileCacherContract.CacheEntry.COLUMN_NAME_SIZE,
                            },                               // The columns to return
                            "",                                // The columns for the WHERE clause
                            new String[]{},                            // The values for the WHERE clause
                            null,                                     // don't group the rows
                            null,                                     // don't filter by row groups
                            FileCacherContract.CacheEntry.COLUMN_NAME_FILE_ID + " ASC",   // The sort order
                            String.valueOf(trimCachesLimit)
                    );
                    long lastDeletedFileId = -1;
                    while (byte_sum > downByteLimit && !cursor.isAfterLast()) {
                        lastDeletedFileId = cursor.getLong(0);
                        if (fileCacher.rmFile(lastDeletedFileId, cursor.getString(1))) {
                            byte_sum -= cursor.getLong(2);
                            cursor.moveToNext();
                            writableDatabase.delete(FileCacherContract.CacheEntry.TABLE_NAME,
                                    FileCacherContract.CacheEntry.COLUMN_NAME_FILE_ID + " = ?",
                                    new String[]{String.valueOf(lastDeletedFileId)});
                        }
                    }
                }
            }
        });
    }

}
