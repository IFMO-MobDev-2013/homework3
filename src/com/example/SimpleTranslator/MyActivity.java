package com.example.SimpleTranslator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    EditText edittext;
    TextView textView, textView1;
    Spinner spinner, spinner1;
    String to,from,translate;
    final String WWORD = "word", FFROM = "from", TTO = "to", TTEXT = "text";
    final String URL = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=";
    final String KEY = "trnsl.1.1.20130922T163909Z.4b80d345f1cf210a.7a19c4e91fc64850938354743ae84c172909c842";

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Window window = getWindow();
        window.setBackgroundDrawableResource(R.drawable.url5);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        edittext = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        textView1 = (TextView) findViewById(R.id.textView1);
    }

    private String getLanguage(Spinner spinner) {
        String s = spinner.getSelectedItem().toString();
        if (s.equals("Русский")) {
            s = "ru";
        } else if (s.equals("Английский")) {
            s = "en";
        } else if (s.equals("Немецкий")) {
            s = "de";
        } else if (s.equals("Испанский")) {
            s = "es";
        } else if (s.equals("Французский")) {
            s = "fr";
        }
        return s;
    }

    class Translate extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String requestUrl = URL + KEY + "&lang=" + params[0] + "-" + params[1] + "&text=" + URLEncoder.encode(params[2], "UTF-8");
                java.net.URL url = new URL(requestUrl);
                HttpsURLConnection httpConnection = (HttpsURLConnection) url.openConnection();
                httpConnection.connect();
                int responseCode = httpConnection.getResponseCode();
                if (responseCode == 200) {
                    String line = null;
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                    JSONTokener tokener = new JSONTokener(sb.toString());
                    JSONObject object = (JSONObject) tokener.nextValue();
                    translate = object.getString(TTEXT);
                }
            }catch(Exception e) {
                textView.setText(getString(R.string.exception));
            }
            return null;
        }

        protected void onPostExecute(String result) {
            String ss = new String(translate);
            ss = ss.substring(1, ss.length() - 1);
            Intent intent = new Intent(MyActivity.this, MyActivity2.class);
            intent.putExtra(WWORD, ss);
            startActivity(intent);
            edittext.setText("");
        }
    }

    public void click1(View v) {
        from = getLanguage(spinner);
        to = getLanguage(spinner1);
        new Translate().execute(from, to, edittext.getText().toString());
    }
}
