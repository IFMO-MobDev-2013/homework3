package ru.georgeee.android.Silencio;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;
import ru.georgeee.android.Silencio.utility.cacher.FileCacher;
import ru.georgeee.android.Silencio.utility.http.HttpUtility;
import ru.georgeee.android.Silencio.utility.http.image.ImageApiResult;
import ru.georgeee.android.Silencio.utility.http.image.flickr.FlickrImageApiTask;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: georgeee
 * Date: 29.09.13
 * Time: 23:00
 * To change this template use File | Settings | File Templates.
 */
public class ImageApiTest extends InstrumentationTestCase {
    public static final String API_KEY = "c6e9f7d17d4a23a08281d34b482a2785";

    public void testImageApi() throws Exception {
        ImageApiResult result = new FlickrImageApiTask(API_KEY, "Магазинчик БО").executeOnHttpTaskExecutor().get();

        Log.d("ImageApiTest", Arrays.toString(result.getImages()));

        assertTrue(result.getImages().length > 0);

    }
}
