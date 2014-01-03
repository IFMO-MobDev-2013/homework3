package com.example.SimpleTranslator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MyActivity2 extends Activity {
    TextView textView;
    final String WWORD = "word";
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
        textView.setText(intent.getStringExtra(WWORD));

    }


    public void click2(View v) {finish();}
}
