package com.ifmo.gosugdrTeam.lesson3.background;

import android.os.AsyncTask;

/**
 * Created with IntelliJ IDEA.
 * User: dronov
 * Date: 03.10.13
 * Time: 4:44
 * To change this template use File | Settings | File Templates.
 */
public class BackgroundWordTranslator extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        String query = strings[0];
        WordTranslator wordTranslator = new WordTranslator(query);
        String result = wordTranslator.getAnswer();
        return result;
    }

}
