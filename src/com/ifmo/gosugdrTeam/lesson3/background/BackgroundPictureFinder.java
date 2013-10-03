package com.ifmo.gosugdrTeam.lesson3.background;

import android.os.AsyncTask;

/**
 * Created with IntelliJ IDEA.
 * User: dronov
 * Date: 03.10.13
 * Time: 2:26
 * To change this template use File | Settings | File Templates.
 */
public class BackgroundPictureFinder extends AsyncTask<String, Void, String[]> {

    @Override
    protected String[] doInBackground(String... strings) {
        String query = strings[0];
        PictureFinder pictureFinder = new PictureFinder(query);
        String[] imageURL = pictureFinder.getAnswer();
        return imageURL;
    }

}
