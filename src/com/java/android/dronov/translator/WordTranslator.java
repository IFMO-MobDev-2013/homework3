package com.java.android.dronov.translator;

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
 * Time: 0:59
 * To change this template use File | Settings | File Templates.
 */
public class WordTranslator {

    private String answer = null;
    public static final String LANG = "en-ru";
    public static final String API_KEY = "trnsl.1.1.20131001T224707Z.5515f04325c947b6.30f694d3ffe38b7839ed56e995887f00939c9f0c";

    public WordTranslator(String query) {
        String curString = null;
        try {
            URL url = new URL("https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + API_KEY +
                    "&lang=" + LANG + "&text=" + query);
            URLConnection urlConnection = url.openConnection();
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            curString = reader.readLine();
            while (curString != null) {
                builder.append(curString);
                curString = reader.readLine();
            }

            JSONObject json = new JSONObject(builder.toString());
            JSONArray array = json.getJSONArray("text");
            answer = array.getString(0);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAnswer() {
        return answer;
    }
}
