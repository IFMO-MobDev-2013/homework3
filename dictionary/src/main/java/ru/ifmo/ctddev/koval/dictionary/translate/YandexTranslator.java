package ru.ifmo.ctddev.koval.dictionary.translate;

import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Translator implementation using Yandex Translation service.
 *
 * @author ndkoval
 */
public class YandexTranslator implements Translator {

    private static final String KEY = "trnsl.1.1.20130930T133759Z.b02a3fec10e65ac3.ea4b48002063d7c9cfcd18a0be43d5b9487f77f6";
    private static final String PATH = "https://translate.yandex.net/api/v1.5/tr.json/translate";
    private static final String LANG = "en-ru";
    private static final String FORMAT = "html";
    private static final HttpClient HTTP_CLIENT = new DefaultHttpClient();
    private static final int GOOD_RESPONSE_CODE = 200;


    @Override
    public String translate(String word) {
        try {
            return new YandexTranslatorAsyncTask().execute(word).get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    /**
     * Class for doing request to Yandex Translation service
     *
     * @author ndkoval
     */
    private class YandexTranslatorAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String word = params[0];

            HttpPost httpPost = new HttpPost(PATH);

            List<NameValuePair> requestParams = new ArrayList<>();
            requestParams.add(new BasicNameValuePair("key", KEY));
            requestParams.add(new BasicNameValuePair("lang", LANG));
            requestParams.add(new BasicNameValuePair("format", FORMAT));
            requestParams.add(new BasicNameValuePair("text", word));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(requestParams, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                return null;
            }

            String jsonResponse;
            try {
                HttpResponse httpResponse = HTTP_CLIENT.execute(httpPost);
                BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
                jsonResponse = br.readLine();
            } catch (IOException e) {
                return null;
            }

            try {
                JSONObject jsonObject = new JSONObject(jsonResponse);

                int code = jsonObject.getInt("code");
                if (code != GOOD_RESPONSE_CODE) {
                    return null;
                }

                String translation = jsonObject.getString("text");
                return translation.substring(2, translation.length() - 2);
            } catch (JSONException e) {
                return null;
            }
        }
    }
}
