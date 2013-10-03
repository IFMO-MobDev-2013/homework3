package com.example.Translator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import org.json.JSONTokener;


public class MyActivity extends Activity {
    String phrase;
    String result;
    String error = null;
    Translation myTranslation = new Translation();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }


    public void onClick(View view)
    {
        EditText forTranslate = (EditText)findViewById(R.id.editText);
        phrase = forTranslate.getText().toString();
        while(phrase.charAt(0) == ' '){
            phrase = phrase.substring(1);
        }
        phrase = phrase.replace(" ", "+");
        myTranslation.execute(phrase);
    }

    public class Translation extends AsyncTask<String, Void, String> {
        public Translation(){};

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... value) {
            Context context = getApplicationContext();
            result = null;
            try {
                result = getTranslate(value[0]);
            }catch (IOException e) {
                result = null;
            }catch (JSONException e) {
                result = null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String answer) {
            super.onPostExecute(answer);
             if (error == null && answer == null) {
                error = "Ошибка перевода";
            }
            if (answer == null) {
                Toast myToast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT);
                myToast.setGravity(Gravity.CENTER, 0, 0);
                myToast.show();
            } else {
                Intent intent = new Intent(MyActivity.this, SecondActivity.class);
                intent.putExtra("word", answer);
                startActivity(intent);
            }
        }

        public String getTranslate(String text) throws IOException, JSONException {
            String apiKey = "trnsl.1.1.20131003T113448Z.d248e096ddf95c89.7d7bb91d66814222b455ccb81a2e763bf23dc84f";
            String requestUrl = "https://translate.yandex.net/api/v1.5/tr.json/translate?key="
                    + apiKey + "&lang=en-ru" + "&text=" + text;

            URL url = new URL(requestUrl);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.connect();
            int mes = httpConnection.getResponseCode();

            if (mes == 200) {
                String line = null;
                BufferedReader buffReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                StringBuilder strBuilder = new StringBuilder();
                while ((line = buffReader.readLine()) != null) {
                    strBuilder.append(line + '\n');
                }

                return resultJSON(strBuilder.toString());
            }else {
                return null;
            }
        }

        public String resultJSON(String str) throws JSONException {
            JSONObject object = (JSONObject) new JSONTokener(str).nextValue();
            String result = object.getString("text");
            result = result.subSequence(2, result.length() - 2).toString();
            return result;
        }
    }
}
