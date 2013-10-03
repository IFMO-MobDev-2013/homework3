package com.tourist.Translator;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TranslationActivity extends Activity {

    private static final String API_KEY = "trnsl.1.1.20131001T130428Z.18896fd9b4b712d0.b8984cdd58a32edec6bbbd7e752ad2ad3b262b5f";
    private static final String API_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate?lang=en-ru&key=" + API_KEY + "&text=";
    private static final String BAD_LUCK = "Не удалось выполнить перевод. Проверьте подключение к Интернету и попробуйте заново";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_translation);
        String word = getIntent().getStringExtra("Word");
        TextView wordView = (TextView) findViewById(R.id.wordView);
        wordView.setText(word);
        try {
            new TranslationTask().execute(word);
        } catch (Exception e) {
            TextView translationView = (TextView) findViewById(R.id.translationView);
            translationView.setText(BAD_LUCK);
        }
    }

    private class TranslationTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(API_URL + URLEncoder.encode(params[0], "UTF-8"));
                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setConnectTimeout(15000);
                httpConnection.connect();
                int responseCode = httpConnection.getResponseCode();
                if (responseCode != 200) {
                    return null;
                }
                BufferedReader buffReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                String text = "";
                String line;
                while ((line = buffReader.readLine()) != null) {
                    text = text + line + "\n";
                }
                JSONObject object = (JSONObject) new JSONTokener(text).nextValue();
                String result = object.getString("text");
                return result.substring(2, result.length() - 2);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            TextView translationView = (TextView) findViewById(R.id.translationView);
            if (result == null) {
                result = BAD_LUCK;
            }
            translationView.setText(result);
        }
    }
}
