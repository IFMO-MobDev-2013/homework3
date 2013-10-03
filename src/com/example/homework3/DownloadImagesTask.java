package com.example.homework3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.MalformedInputException;

/**
 * Created with IntelliJ IDEA.
 * User: satori
 * Date: 10/3/13
 * Time: 8:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class DownloadImagesTask extends AsyncTask<Void, Void, Void> {
    private ImageView[] views;
    private URL[] urls;
    private String word;
    private JSONArray res;
    private Bitmap[] bms;
    public DownloadImagesTask(ImageView[] vs, String w) {
        views = vs;
        urls = new URL[10];
        word = w;
        bms = new Bitmap[10];
    }

    @Override
    protected Void doInBackground(Void... vs) {
        try {
            getUrls(8,0);

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 10; i++) {
            if (urls[i] != null) {
                bms[i] = downloadBitmap(urls[i]);
                //views[i].setImageBitmap(downloadBitmap(urls[i]));
            }
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void v) {
        for (int i = 0; i < 10; i++) {
            if (bms[i] != null) {
                views[i].setImageBitmap(bms[i]);
            }
        }
    }
    private void getUrls(int n, int s) throws MalformedURLException {
        try {
        URL url = new URL("https://ajax.googleapis.com/ajax/services/search/images?" +
                "v=1.0&rsz=" + n + "&start=" + s  + "&q=" + URLEncoder.encode(word, "UTF-8"));





            URLConnection connection = url.openConnection();
            //connection.addRequestProperty("Referer", /* Enter the URL of your site here */);

            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }



            JSONObject json = new JSONObject(builder.toString());
            res = json.getJSONObject("responseData").getJSONArray("results");
            for (int i = s; i < (s + n) && i < res.length();i++) {
                urls[i] = new URL(res.getJSONObject(i).getString("tbUrl"));
            }
        }
        catch (IOException e) {

            e.printStackTrace();
        }
        catch (JSONException e1) {
            e1.printStackTrace();
        }

// now have some fun with the results...

    }

    private Bitmap downloadBitmap(URL url) {
        final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
        final HttpGet getRequest = new HttpGet(url.toString());

        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // Could provide a more explicit error message for IOException or IllegalStateException
            getRequest.abort();
            Log.w("ImageDownloader", "Error while retrieving bitmap from " + url.toString(), e);


        } finally {
            if (client != null) {
                client.close();
            }
        }
        return null;

    }

}
