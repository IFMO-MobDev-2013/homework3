package com.ifmo.gosugdrTeam.lesson3.background;

import com.ifmo.gosugdrTeam.lesson3.AppSectionsPagerAdapter;
import com.ifmo.gosugdrTeam.lesson3.Request;

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

    @Override
    protected void onPostExecute(String result) {
//    	super.onPostExecute(result);
    	Request.setTranslation(result);
    	AppSectionsPagerAdapter.updateTranslateFragment();
    }
    
}
