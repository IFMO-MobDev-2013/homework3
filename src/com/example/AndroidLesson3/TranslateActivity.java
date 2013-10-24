package com.example.AndroidLesson3;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Xottab
 * Date: 20.09.13
 * Time: 14:53
 * To change this template use File | Settings | File Templates.
 */
public class TranslateActivity extends Activity {

    class Translator extends AsyncTask<String, Void, String> {
        public Activity fromActivity;
        protected String address;
        public static final String apiKey = "trnsl.1.1.20130925T205049Z.a92cd36d99706af9.b5a1aaf3c0f791ceb482029c486691897db34357";
        public static final String yandexUrl = "https://translate.yandex.net/api/v1.5/tr/translate";

        @Override
        protected String doInBackground(String... params) {
            StringBuilder sb = new StringBuilder();
            String result;
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = factory.newDocumentBuilder();
                Document doc;
                address = yandexUrl + "?key=" + apiKey + "&text=" + URLEncoder.encode(params[0],"UTF-8") + "&lang=ru";
                URL url = new URL(address);
                HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(new BufferedInputStream(connect.getInputStream())));
                String temp;
                while ((temp = in.readLine()) != null) {
                    sb.append(temp);
                }

                String translationXml = sb.toString();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(sb.toString()));
                doc = db.parse(is);
                connect.disconnect();
                if (translationXml == null) {
                    result = "no internet connection";
                } else {
                    result = doc.getElementsByTagName("text").item(0).getTextContent();
                }
            } catch (Exception e) {
                result = "internal error";
                Log.e("lol", "lol", e);
            }

            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            TextView translationView = (TextView) findViewById(R.id.translate);
            translationView.setText(result);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translate);
        TextView name = (TextView) findViewById(R.id.translate);
        CharSequence sequence = getIntent().getExtras().getCharSequence("translate");
        new Translator().execute(sequence.toString());
        name.setText(sequence);
    }
}