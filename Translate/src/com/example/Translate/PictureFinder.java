package com.example.Translate;

import android.graphics.drawable.Drawable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;


/**
 * Created with IntelliJ IDEA.
 * User: Ruslan
 * Date: 02.10.13
 * Time: 15:28
 * To change this template use File | Settings | File Templates.
 */



public class PictureFinder {

    private static String sampleLink = "https://www.dropbox.com/s/x3k56wluyq9eygc/f_error.gif";

    public static ArrayList<Drawable> getImages(String requestString) throws IOException, JSONException {
        requestString = URLEncoder.encode(requestString);
        ArrayList<String> pictures = new ArrayList<String>();
        URLConnection connection;
        String requestURL = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
        for (int j = 0; j < 3; j++) {
            URL pageURL = new URL(requestURL + requestString + "&start=" + (4 * j));
            connection = pageURL.openConnection();
            connection.connect();
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF8"));
            JSONObject request = new JSONObject(in.readLine());
            request = request.getJSONObject("responseData");
            JSONArray images = request.getJSONArray("results");
            for (int i = 0; i < (j == 2? 2 : 4); i++) {
                JSONObject cur = images.getJSONObject(i);
                pictures.add(cur.getString("url"));
            }
        }
        ArrayList<Drawable> imageList = new ArrayList<Drawable>();
        for(int i = 0; i < 10; i++){
            imageList.add(getImageFromURL(pictures.get(i)));
        }
        return imageList;
    }

    public static Drawable getImageFromURL(String imageURL) throws IOException{
        Drawable drawable;
        try{
            drawable = Drawable.createFromStream((InputStream) new URL(imageURL).getContent(),"Pic");
        } catch (FileNotFoundException e){
            drawable = Drawable.createFromStream((InputStream) new URL(sampleLink).getContent(), "errorPic");
        }
        return drawable;
    }

}