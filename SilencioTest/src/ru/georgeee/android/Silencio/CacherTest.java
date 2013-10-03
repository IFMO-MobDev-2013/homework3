package ru.georgeee.android.Silencio;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import ru.georgeee.android.Silencio.utility.cacher.FileCacher;
import ru.georgeee.android.Silencio.utility.http.download.SimpleCachingDownloadTask;
import ru.georgeee.android.Silencio.utility.http.image.ImageApiResult;
import ru.georgeee.android.Silencio.utility.http.image.flickr.FlickrImageApiTask;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: georgeee
 * Date: 29.09.13
 * Time: 23:32
 * To change this template use File | Settings | File Templates.
 */
public class CacherTest extends ActivityInstrumentationTestCase2<SearchActivity> {
    public static final long CACHE_BYTE_LIMIT = 1500000;
    public static final String BEATA_BEATRIX_URL = "http://allart.biz/up/photos/album/R/Dante%20Gabriel%20Rossetti/dante_gabriel_rossetti_17_beata_beatrix.jpg";
    public static final String ECCE_ANCILLA_DOMINI_URL = "http://allart.biz/up/photos/album/R/Dante%20Gabriel%20Rossetti/dante_gabriel_rossetti_16_ecce_ancilla_domini.jpg";
    static final TestImageUrl[] testImageUrls = new TestImageUrl[]{
//            new TestImageUrl(2076718, BEATA_BEATRIX_URL),
            new TestImageUrl(1480582,ECCE_ANCILLA_DOMINI_URL),
            new TestImageUrl(1480582,ECCE_ANCILLA_DOMINI_URL),
    };

    public CacherTest() {
        super("ru.georgeee.android.Silencio", SearchActivity.class);
    }

    public void _testSimpleDownload() throws Exception {
        FileCacher.getInstance().init(getActivity(), CACHE_BYTE_LIMIT);
        SimpleCachingDownloadTask task = new SimpleCachingDownloadTask(BEATA_BEATRIX_URL) {
            @Override
            protected void onPostExecute(File file) {
                Log.d("CacherTest", "file downloaded location: " + file.getAbsolutePath());
            }
        };
        File file = task.executeOnHttpTaskExecutor().get();
        Log.d("CacherTest", "file downloaded location: " + file.getAbsolutePath() + " fsize:" + file.length());
        assertEquals(2076718, file.length());
        File file2 = new SimpleCachingDownloadTask(BEATA_BEATRIX_URL).executeOnHttpTaskExecutor().get();
        file.delete();
        file2.delete();
        assertEquals(file.getAbsolutePath(), file2.getAbsolutePath());
    }
    public void testSimpleDownload2() throws Exception {
        FileCacher.getInstance().init(getActivity(), CACHE_BYTE_LIMIT);
        FileCacher.getInstance().setUseSdCardStorage(true);
        for(TestImageUrl imageUrl: testImageUrls){
            SimpleCachingDownloadTask task = new SimpleCachingDownloadTask(imageUrl.url) {
                @Override
                protected void onPostExecute(File file) {
                    Log.d("CacherTest", "(onPostExecute) file downloaded location: " + file.getAbsolutePath());
                }
            };
            File file = task.executeOnHttpTaskExecutor().get();
            Log.d("CacherTest", "file downloaded location: " + file.getAbsolutePath() + " fsize:" + file.length());
            assertEquals(imageUrl.size, file.length());
        }
    }

    public void _testImageDownloadCache() throws Exception {
        FileCacher.getInstance().init(getActivity(), CACHE_BYTE_LIMIT);
        ImageApiResult result = new FlickrImageApiTask(ImageApiTest.API_KEY, "Apple").executeOnHttpTaskExecutor().get();
        ImageApiResult.Image[] images = result.getImages();
        File[] files = new File[Math.min(10, images.length)];
        SimpleCachingDownloadTask[] tasks = new SimpleCachingDownloadTask[files.length];
        for (int i = 0; i < files.length; ++i) {
            tasks[i] = new SimpleCachingDownloadTask(images[i].getSmallImageUrl()) {
                @Override
                protected void onPostExecute(File file) {
                    assertTrue(file.exists());
                    file.deleteOnExit();
                    Log.d("CacherTest", "Url " + getUrl() + " downloaded into " + file.getAbsolutePath() + " (" + file.length() + "bytes)");
                }
            };
            tasks[i].executeOnHttpTaskExecutor();
        }

    }

    static class TestImageUrl {
        long size;
        String url;

        TestImageUrl(long size, String url) {
            this.size = size;
            this.url = url;
        }
    }
}
