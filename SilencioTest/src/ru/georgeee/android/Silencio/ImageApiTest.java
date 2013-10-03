package ru.georgeee.android.Silencio;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import ru.georgeee.android.Silencio.utility.http.image.ImageApiResult;
import ru.georgeee.android.Silencio.utility.http.image.flickr.FlickrImageApiResult;
import ru.georgeee.android.Silencio.utility.http.image.flickr.FlickrImageApiTask;

/**
 * Created with IntelliJ IDEA.
 * User: georgeee
 * Date: 29.09.13
 * Time: 23:00
 * To change this template use File | Settings | File Templates.
 */
public class ImageApiTest extends ActivityInstrumentationTestCase2<SearchActivity> {

    public ImageApiTest() {
        super("ru.georgeee.android.Silencio", SearchActivity.class);
    }

    public static final String API_KEY = "c6e9f7d17d4a23a08281d34b482a2785";

    public void testImageApi() throws Exception {
        FlickrImageApiResult result = (FlickrImageApiResult) new FlickrImageApiTask(API_KEY, "Магазинчик БО").executeOnHttpTaskExecutor().get();

        for (final ImageApiResult.Image _image : result.getImages()) {
            final FlickrImageApiResult.FlickrImage image = (FlickrImageApiResult.FlickrImage) _image;
            image.asyncLoadSizes(new FlickrImageApiResult.FlickrImage.SizeLoadedHandler() {
                @Override
                protected void onPostExecute(FlickrImageApiResult.FlickrImage.Size[] sizes) {
                    FlickrImageApiResult.FlickrImage.Size closestSize = image.getSizeWithClosestWidth(1000);
                    Log.d("ImageApiTest", "Width:1000 closest: " + closestSize.getWidth() + " " + closestSize.getSource());
                }
            }, API_KEY);
        }
        assertTrue(result.getImages().length > 0);

    }
}
