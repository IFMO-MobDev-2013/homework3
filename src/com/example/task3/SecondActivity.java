package com.example.task3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class SecondActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pictures);
        TextView translation = (TextView) findViewById(R.id.textview);
        String text = getIntent().getStringExtra("translation");
        translation.setText(text);

    }

    public void onBackPressed() {
        Intent i = new Intent(SecondActivity.this, MyActivity.class);
        startActivity(i);
        finish();
    }
}
