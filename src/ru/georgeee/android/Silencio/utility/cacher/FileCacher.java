package ru.georgeee.android.Silencio.utility.cacher;

import android.content.Context;

import java.io.File;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: georgeee
 * Date: 29.09.13
 * Time: 19:29
 * To change this template use File | Settings | File Templates.
 */
public class FileCacher {
    private static FileCacher ourInstance = new FileCacher();

    public static FileCacher getInstance() {
        return ourInstance;
    }

    protected Context context = null;
    protected FileCacherDbManager dbManager;

    private FileCacher() {
    }

    public void init(Context context, long byteLimit) {
        if (this.context == null) {
            this.context = context;
            dbManager = new FileCacherDbManager(context, byteLimit, this);
            fileIdSet = new HashSet<Long>();
        }
    }

    HashSet<Long> fileIdSet;

    public boolean rmFile(long fileId, String path) {
        if (fileIdSet.contains(fileId)) return false;
        File file = getFileById(fileId, path);
        if (!file.exists() || (file.delete() && !file.exists())) return true;
        return false;
    }

    public File getCacheDir() {
        File file = new File(context.getFilesDir(), "cache");
//        File file = new File("/sdcard/Silencio", "cache");
        if (!file.exists()) file.mkdir();
        return file;
    }

    protected String getExtension(String path) {
        Pattern p = Pattern.compile("\\.([a-z0-9_]+)$");
        Matcher matcher = p.matcher(path);
        matcher.find();
        if (matcher.group(1) != null)
            return matcher.group(1);
        return "tmp";
    }

    public void updateSize(String path, long size) {
        dbManager.updateSize(path, size);
    }

    public void launchCacheCleaner() {
        dbManager.launchCacheCleaner();
    }

    protected File getFileById(long fileId, String path) {
        File file = new File(getCacheDir(), fileId + "." + getExtension(path));
        return file;
    }

    public File registerFile(String path) {
        long fileId = dbManager.registerPath(path);
        fileIdSet.add(fileId);
        return getFileById(fileId, path);
    }
}
