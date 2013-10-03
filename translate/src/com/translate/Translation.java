package com.translate;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Translation extends Activity {
    Intent getPhrase;
    String phrase, encodedPhrase, lang;
    URL url;
    TextView translationResult;

    private class TranslationTask extends AsyncTask<URL, Void, String> {
        String resultString;

        @Override
        protected String doInBackground(URL... urls) {
            resultString = "It changes nothing...";
            try {
                InputStream dataStream = urls[0].openConnection().getInputStream();
                InputStreamReader isr = new InputStreamReader(dataStream, "UTF-8");

                StringBuilder data = new StringBuilder();
                int c;
                while ((c = isr.read()) != -1){
                    data.append((char) c);
                }

                resultString = data.toString();
                resultString = phrase +
                        "\n___________________\n" +
                        resultString.substring(
                        resultString.indexOf("<text>") + 6,
                        resultString.indexOf("</text"));
            } catch (IOException e1) {
                resultString = "IOException in TranslationTask";
            }
            return resultString;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            translationResult.setText(res);
        }
    }

    private class SearchTask extends AsyncTask<URL, Void, String> {
        String resultString;

        @Override
        protected String doInBackground(URL... urls) {
            resultString = "Doesn't work...";
            try {
                InputStream dataStream = urls[0].openConnection().getInputStream();
                InputStreamReader isr = new InputStreamReader(dataStream, "UTF-8");

                StringBuilder data = new StringBuilder();
                int c;
                while ((c = isr.read()) != -1){
                    data.append((char) c);
                }

                resultString = data.toString();
                resultString = "Images:\n\n" +
                        resultString;
            } catch (IOException e1) {
                resultString = "IOException in SearchTask";
            }
            return resultString;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            translationResult.setText(res);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translation);
        translationResult = (TextView) findViewById(R.id.textView1);

        try {
            getPhrase = getIntent();
            phrase = getPhrase.getStringExtra("phrase");
            encodedPhrase = URLEncoder.encode(phrase, "UTF-8");
            lang = getPhrase.getStringExtra("lang");
            url = new URL("https://translate.yandex.net/api/v1.5/tr/translate?" +
                    "key=trnsl.1.1.20130924T115818Z.acf11e6fbe3a77e6.6d0c42d0160bc" +
                    "b348dabe9efa3c0ed789cdc24a5" +
                    "&text=" + encodedPhrase +
                    "&lang=" + lang +
                    "&format=plain");
            new TranslationTask().execute(url);
        } catch (MalformedURLException e1) {
            translationResult.setText("URL Error");
        } catch (UnsupportedEncodingException e2) {
            translationResult.setText("Encoding Error");
        }
    }
}
