package ru.ifmo.ctddev.koval.dictionary.imagedownload;

import android.os.AsyncTask;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by vladimirskipor on 10/3/13.
 */
public class BingImageSearch implements ImageSearcher{


    private static final String ENCODING_CHARSET = "UTF-8";
    private static final String accountKey = "xtAvJ6Q81BmBb9Y3DNykBXdLKWj9GV/MzOFaT3QGpww";
    private static final String accountKeyEnc = new String(Base64.encodeBase64((accountKey + ":" + accountKey).getBytes()));

//    private static final String SEARCH_PATTERN = "https://api.datamarket.azure.com/Bing/Search/v1/Composite?Sources=%%27image%%27" +
//            "&Query=%%27" + "%s" + "%%27" +//text for searching
//            "&ImageFilters=%%27Size%%3AWidth%%3A" + "%d" + "%%2BSize%%3AHeight%%3A" + "%d" + //size filter
//            "%%27&$top=" + "%d" +//image count limit
//            "&Adult=%%27Moderate%%27";

    private static final String SEARCH_PATTERN = "https://api.datamarket.azure.com/Bing/Search/v1/Composite?Sources=%%27image%%27" +
            "&Query=%%27" + "%s" + "%%27" +//text for searching
            "&ImageFilters=" + //size filter
            "%%27&$top=" + "%d" +//image count limit
            "&Adult=%%27Moderate%%27";


    private final int  defaultHeightFilter;
    private final int defaultWidthFilter;
    private final int defaultImageLimit;


    public BingImageSearch(int defaultHeightFilter, int defaultWidthFilter, int defaultImageLimit) {
        this.defaultHeightFilter = defaultHeightFilter;
        this.defaultWidthFilter = defaultWidthFilter;
        this.defaultImageLimit = defaultImageLimit;

    }


    public int getDefaultHeightFilter() {
        return defaultHeightFilter;
    }

    public int getDefaultWidthFilter() {
        return defaultWidthFilter;
    }

    public int getDefaultImageLimit() {
        return defaultImageLimit;
    }

    public List<ResponseImage> search(String text) throws ImageSearcherException {

        URL queryURL = null;
        try {
            queryURL = new URL(String.format(SEARCH_PATTERN, URLEncoder.encode(text, ENCODING_CHARSET),
                    defaultWidthFilter, defaultHeightFilter, defaultImageLimit));

//            queryURL = new URL("https://api.datamarket.azure.com/Bing/Search/v1/Image?Query=%27xbox%27&Market=%27en-US%27&Adult=%27Moderate%27");
        } catch (MalformedURLException e) {
            throw new ImageSearcherException(e);
        } catch (UnsupportedEncodingException e) {
            throw new ImageSearcherException(e);
        }

        AsyncSearchQuery asyncSearchQuery = new AsyncSearchQuery();
        asyncSearchQuery.execute(queryURL);
        try {
            return asyncSearchQuery.get();
        } catch (InterruptedException e) {
            throw new ImageSearcherException(e);
        } catch (ExecutionException e) {
            throw new ImageSearcherException(e);
        }


    }

//


    class AsyncSearchQuery extends AsyncTask<URL, Void, List<ResponseImage>> {


        @Override
        protected List<ResponseImage> doInBackground(URL... params) {

            try {

                URL url = params[0];
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
                conn.setRequestProperty("Accept", "application/json");
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), ENCODING_CHARSET));

                StringBuilder builder = new StringBuilder();
                String inputLine = in.readLine();
                while (inputLine != null) {
                    builder.append(inputLine);
                    inputLine = in.readLine();
                }
                in.close();
                builder.toString();
                conn.disconnect();

                String searchResponse = builder.toString();


                JSONArray results = null;
                List<ResponseImage> resultImages = null;

                try {
                    results = new JSONObject(searchResponse).getJSONObject("d").getJSONArray("results");
                    resultImages = new ArrayList<ResponseImage>();
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject object = results.getJSONObject(i);
                        JSONArray images = object.getJSONArray("Image");
                        for (int j = 0; j < images.length(); j++) {
                            JSONObject image = images.getJSONObject(j);

                            String imageUrl = image.getString("MediaUrl");
                            int width = image.getInt("Width");
                            int height = image.getInt("Height");
                            String title = image.getString("Title");
                            ResponseImage resultImage = new ResponseImage(imageUrl, width, height, title);
                            resultImages.add(resultImage);

                        }
                    }


                } catch (JSONException e) {
                     e.printStackTrace();
                    return null;
                }



                return resultImages;




            } catch (IOException e) {
                e.printStackTrace();

            }
            return null;
        }
    }


}
