package ru.georgeee.android.Silencio;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import ru.georgeee.android.Silencio.utility.cacher.FileCacher;
import ru.georgeee.android.Silencio.utility.http.download.SimpleCachingDownloadTask;
import ru.georgeee.android.Silencio.utility.http.image.ImageApiResult;
import ru.georgeee.android.Silencio.utility.http.image.flickr.FlickrImageApiTask;

import java.io.File;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: georgeee
 * Date: 29.09.13
 * Time: 23:32
 * To change this template use File | Settings | File Templates.
 */
public class CacherTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public static final long CACHE_BYTE_LIMIT = 50*1024*1024;

    public CacherTest(String pkg) {
        super(pkg, MainActivity.class);
    }

    public void testImageDownloadCache() throws Exception {
        FileCacher.getInstance().init(getActivity(), CACHE_BYTE_LIMIT);
        ImageApiResult result = new FlickrImageApiTask(ImageApiTest.API_KEY, "Каннио").executeOnHttpTaskExecutor().get();

        ImageApiResult.Image [] images = result.getImages();
        File[] files = new File[Math.min(10, images.length)];
        SimpleCachingDownloadTask[] tasks = new SimpleCachingDownloadTask[files.length];
        for(int i=0; i< files.length; ++i){
            tasks[i] = new SimpleCachingDownloadTask(images[i].getSmallImageUrl());
            tasks[i].executeOnHttpTaskExecutor();
        }
        for(int i=0; i< files.length; ++i){
            SimpleCachingDownloadTask task = tasks[i];
            files[i] = task.get();
            assertTrue(files[i].exists());
        }
        assertTrue(result.getImages().length > 0);

    }
}
