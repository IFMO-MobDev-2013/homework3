package com.java.android.dronov.translator;

import android.graphics.Bitmap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: dronov
 * Date: 02.10.13
 * Time: 3:08
 * To change this template use File | Settings | File Templates.
 */
public class PictureFinder {

    public static final int PICTURES_COUNT = 10;
    private final static String KEY = "IzaSyCQsdOFIXEuee3mAL2z8_VKitws5XkCMvo";
    private final static String ENGINE = "006645901772524591837:w8edr7u5t1c";
    private String[] imageURL;
    public PictureFinder(String query) {
        imageURL = new String[PICTURES_COUNT];
        String currentBufferString;
        try {
            URL url = new URL("https://www.googleapis.com/customsearch/v1?key=" + KEY
                    + "&cx=" + ENGINE
                    + "&q=" + query
                    + "&searchType=image"
                    + "&imgSize=medium"
                    + "&alt=json"
                    + "&num=10");
            URLConnection connection = url.openConnection();
            StringBuilder builder = new StringBuilder();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((currentBufferString = buffer.readLine()) != null) {
                builder.append(currentBufferString);
            }
            JSONObject json = new JSONObject(builder.toString());
            JSONArray array = json.getJSONArray("items");
            for (int i = 0; i < array.length(); i++) {
                imageURL[i] = new URL(array.getJSONObject(i).getString("link")).toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String[] getAnswer() {
        return imageURL;
    }
}
