package ifmo.mobdev.Task3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class GoogleImageSearcher {
    private String queryExpr;
    private String query = null;
    private static final String queryPref = "https://www.google.ru/search?tbm=isch&tbs=isz:m&q=";
    private static final String imgUrl = "imgurl=";
    private static final String endUrl = "&amp";
    URL url;
    String sourceString;
    int cur = 0;
    int num = 0;
    int numDraw;
    ImageView[] imgView;

    private class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        int i;

        public BitmapDownloaderTask(int pos) {
            i = pos;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = (String) params[0];
            return downloadBitmap(url);
        }

        @Override
        protected void onPostExecute(Bitmap bm) {
            if (bm != null) {
                imgView[i].setImageBitmap(bm);
            }
            Log.d("DownloadImageTask", "image download");
        }

        private Bitmap downloadBitmap(String Url) {
            Bitmap bm = null;
            try {
                URL url = new URL(Url);
                InputStream is = url.openStream();
                bm = BitmapFactory.decodeStream(is);
            }  catch (IOException e) {
                Log.d("downloadBitmap", e.getLocalizedMessage());
            }
            return bm;
        }

    }

    private class DownloadResultTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL(query);
            } catch (MalformedURLException e) {
                Log.d("make URL", query);
            }
            StringBuilder builder = null;
            InputStream is = null;
            try {
                URLConnection connection = url.openConnection();
                connection.addRequestProperty("Referer", "GoogleImageSearchApp");
                connection.setConnectTimeout(15000);
                String line;
                builder = new StringBuilder();
                is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isr);
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } catch (IOException e) {
                Log.d("DownloadResultTask",e.getLocalizedMessage());
            }
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String res) {
            sourceString = res;
            draw();
        }
    }

    GoogleImageSearcher(String expr, ImageView[] imgView, int num) {
        this.imgView = imgView;
        this.num = num;
        query = queryPref;
        queryExpr = expr;
        for (int i = 0; i < queryExpr.length(); i++) {
            query += (queryExpr.charAt(i) == ' ' ? '+' : queryExpr.charAt(i));
        }
    }

    public int drawImages() {
        new DownloadResultTask().execute(query);
        return numDraw;
    }

    private String nextURL() {
        if (sourceString == null) return null;
        cur = sourceString.indexOf(imgUrl, cur);
        if (cur == -1) return null;
        cur += imgUrl.length();
        int end = sourceString.indexOf(endUrl, cur);
        if (end != -1) return sourceString.substring(cur, end);
        else return null;
    }

    private void draw() {
        String next = null;
        int col = 0;
        for (int i = 0; i < num; i++) {
            next = nextURL();
            if (next != null) {
                col++;
                new BitmapDownloaderTask(i).execute(next);
            }
        }
        numDraw = col;
    }
}
