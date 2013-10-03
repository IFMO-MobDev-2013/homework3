package com.example.SimpleTranslator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MyActivity2 extends Activity {
    /**
     * Called when the activity is first created.
     */

    TextView textView;



    class netConnectTranslate implements Runnable {
        String from, to , text, translate;
        public void run() {

            try {
                String requestUrl = "https://translate.yandex.net/api/v1.5/tr.json/translate?key="
                        + "trnsl.1.1.20130922T163909Z.4b80d345f1cf210a.7a19c4e91fc64850938354743ae84c172909c842"
                        + "&lang=" + from + "-" + to + "&text=" + URLEncoder.encode(text, "UTF-8");
                URL url = new URL(requestUrl);
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
                    JSONObject  object = (JSONObject) tokener.nextValue();
                    translate = object.getString("text");
                }
            }catch(IOException e){
                textView.setText("URL is illegal");
            }catch(JSONException e) {
                textView.setText("Запрос не выполнен, пожалуйста, повторите еще раз");
            }

        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        Window window = getWindow();
        window.setBackgroundDrawableResource(R.drawable.url5);
        textView = (TextView) findViewById(R.id.textView);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(25);
        Intent intent = getIntent();
        String begWord = intent.getStringExtra("word");
        //begWord = begWord.replaceAll(" ", "+");
        netConnectTranslate net = new netConnectTranslate();
        net.from = intent.getStringExtra("from");
        net.to = intent.getStringExtra("to");
        net.text = begWord;
        Thread thread = new Thread(net);
        thread.start();
        try{
            thread.join();
        }catch(InterruptedException e) {
            textView.setText("ПОТОК НЕ СМОГ ВЫЙТИ В ИНТЕРНЕТ, ОГОСПОДИ");
        }
        String ss = new String(net.translate);
        ss = ss.substring(1, ss.length() - 1);
        textView.setText(ss);
    }




    public void click2(View v) {finish();}
}
