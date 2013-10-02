package com.example.translate;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        Bundle extras = getIntent().getExtras();
        String text=extras.getString("translate");
        TextView textView=(TextView)findViewById(R.id.textView);
        textView.setText(text);
    }
}