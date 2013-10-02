package ru.ifmo.translatemaster;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;

public class QueryProcessor extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        String text = bundle.getString("text");
        AsyncTask<String, Void, String> translator = new Translator();
        translator.execute(text);
        setContentView(R.layout.translation);
    }

    private void setResult(String result) {
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(result);
    }

    private class Translator extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... text) {
            String query, result = "";
            try {
                query = "https://translate.yandex.net/api/v1.5/tr/translate?key=trnsl.1.1.20130922T074611Z.7a00ee38626ce635.00b0b8c45901e1bed9cf08e1275b7985ce51a66f&text=" + URLEncoder.encode(text[0], "ISO-8859-1") + "&lang=en-ru";
            } catch (UnsupportedEncodingException e) {
                return "Sorry, something went wrong";
            }
            try {
                URL url = new URL(query);
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                Scanner scanner = new Scanner(connection.getInputStream());
                while (scanner.hasNext()) {
                    result += scanner.nextLine();
                }
            } catch (IOException e) {
                return "Error while connecting to server";
            }
            int from = -1, to = -1;
            try {
                for (int i = 0; i < result.length(); i++) {
                    if (i + 6 < result.length() && result.substring(i, i + 6).equals("<text>")) {
                        from = i + 6;
                        break;
                    }
                }
                if (from == -1) {
                    return null;
                }
                for (int i = from; i < result.length(); i++) {
                    if (i + 7 < result.length() && result.substring(i, i + 7).equals("</text>")) {
                        to = i;
                        break;
                    }
                }
            } catch (StringIndexOutOfBoundsException e) {
                return "Sorry, something went wrong";
            }
            return "Translation: " + result.substring(from, to);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            setResult(result);
        }
    }
}
