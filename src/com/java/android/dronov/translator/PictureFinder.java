package com.java.android.dronov.translator;

import android.graphics.Bitmap;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: dronov
 * Date: 02.10.13
 * Time: 3:08
 * To change this template use File | Settings | File Templates.
 */
public class PictureFinder {

    public static final int PICTURES_COUNT = 10;
    public static final String KEY = "AIzaSyDpcRCDD60VhR_nsXT9zpRcgfYWy8mudeI";
    private Bitmap[] resultPictures;

    public PictureFinder(String query)  {
        resultPictures = new Bitmap[PICTURES_COUNT];
        int currentPicturesCount = 0;
        try {
            do {
                URL url = new URL("https://ajax.googleapis.com/ajax/services/search/images?v=1.0");


            } while (currentPicturesCount < PICTURES_COUNT);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
