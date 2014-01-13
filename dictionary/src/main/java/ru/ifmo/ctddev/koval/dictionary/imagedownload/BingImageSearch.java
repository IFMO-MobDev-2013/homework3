package ru.ifmo.ctddev.koval.dictionary.imagedownload;

import android.util.Log;

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

/**
 * Created by vladimirskipor on 10/3/13.
 */
public class BingImageSearch implements ImageSearcher {

    public static final String TAG = "BingImageSearch";


    private static final String ENCODING_CHARSET = "UTF-8";
    private static final String accountKey = "xtAvJ6Q81BmBb9Y3DNykBXdLKWj9GV/MzOFaT3QGpww";
    private static final String accountKeyEnc = new String(Base64.encodeBase64((accountKey + ":" + accountKey).getBytes()));

    private static final String SEARCH_PATTERN = "https://api.datamarket.azure.com/Bing/Search/v1/Composite?Sources=%%27image%%27" +
            "&Query=%%27" + "%s" + "%%27" +//text for searching
            "&ImageFilters=%%27Size%%3AWidth%%3A" + "%d" + "%%2BSize%%3AHeight%%3A" + "%d" + //size filter
            "%%27&$top=" + "%d" +//image count limit
            "&Adult=%%27Moderate%%27";
    public static final String JSON_ERROR_MESSAGE = "JSON problem";
    public static final String CONNECTION_PROBLEM = "Connection problem";

//    private static final String SEARCH_PATTERN_NO_IMAGE_FILTERS = "https://api.datamarket.azure.com/Bing/Search/v1/Composite?Sources=%%27image%%27" +
//            "&Query=%%27" + "%s" + "%%27" +//text for searching
//            "&ImageFilters=" + //size filter
//            "%%27&$top=" + "%d" +//image count limit
//            "&Adult=%%27Moderate%%27";


    private int defaultHeightFilter;
    private int defaultWidthFilter;
    private int defaultImageLimit;


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

    public void setDefaultHeightFilter(int defaultHeightFilter) {
        this.defaultHeightFilter = defaultHeightFilter;
    }

    public void setDefaultWidthFilter(int defaultWidthFilter) {
        this.defaultWidthFilter = defaultWidthFilter;
    }

    public void setDefaultImageLimit(int defaultImageLimit) {
        this.defaultImageLimit = defaultImageLimit;
    }

    public List<ResponseImage> search(String text) throws ImageSearcherException {
        return search(text, defaultWidthFilter, defaultHeightFilter, defaultImageLimit);
    }


    public List<ResponseImage> search(String text, int widthFilter, int heightFilter, int imageLimit) throws ImageSearcherException {

        URL queryURL;
        try {
            queryURL = new URL(String.format(SEARCH_PATTERN, URLEncoder.encode(text, ENCODING_CHARSET),
                    widthFilter, heightFilter, imageLimit));

//            queryURL = new URL("https://api.datamarket.azure.com/Bing/Search/v1/Image?Query=%27xbox%27&Market=%27en-US%27&Adult=%27Moderate%27");
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            Log.e(TAG, "URL problem", e);
            throw new ImageSearcherException(e);
        }


        try {

            HttpURLConnection conn = (HttpURLConnection) queryURL.openConnection();
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
            conn.disconnect();

            String searchResponse = builder.toString();


            JSONArray results;
            List<ResponseImage> resultImages;

            try {
                results = new JSONObject(searchResponse).getJSONObject("d").getJSONArray("results");
                resultImages = new ArrayList<>();
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
                Log.e(TAG, JSON_ERROR_MESSAGE, e);
                throw new ImageSearcherException(e);
            }


            return resultImages;


        } catch (IOException e) {
            Log.e(TAG, CONNECTION_PROBLEM, e);
            throw new ImageSearcherException(e);

        }

    }


}

//




