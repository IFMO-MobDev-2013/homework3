package com.example.lection3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity1 extends Activity {

    private final String key = "key=trnsl.1.1.20130921T143302Z.c4c94e2b25dc1cf0.d4f94454ba5c56b03974e132bdacfe538a600df3&text=";
    private String translate;
    private String result;
    private String adress = "https://translate.yandex.net/api/v1.5/tr/translate?";
    private String end = "&lang=ru&format=html";

    EditText t;
    URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sergej);
        t = ((EditText) findViewById(R.id.editText3));
        translate = getIntent().getExtras().getString("word");
        ParserToTranslate();
        query();
        t.setText(result);
        Button back = (Button) findViewById(R.id.back);
        final Intent intent = new Intent(MainActivity1.this, MainActivity.class);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    private void ParserToTranslate() {
        result = translate.replaceAll(" ", "%20").replaceAll("&", "%26");

    }

    private void query() {
        try {

            url = new URL(adress + key + result + end);
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            String ans = "";
            while ((line = reader.readLine()) != null) {
                ans += line;
            }
            result = "";
            int tek = 0;
            for (int i = 0; i < ans.length() - 1; ) {
                if (ans.charAt(i) == '<' && ans.charAt(i + 1) == 't') {
                    tek = 1;
                    i = i + 6;
                } else if (tek == 0) {
                    i++;
                } else if (tek == 1 && ans.charAt(i) == '<'
                        && ans.charAt(i + 1) == '/') {
                    tek = 0;
                } else {
                    result += ans.charAt(i);
                    i++;
                }

            }

        } catch (MalformedURLException e) {

        } catch (IOException e) {

        }

    }

}
