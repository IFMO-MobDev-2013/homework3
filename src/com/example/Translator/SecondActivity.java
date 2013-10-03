package com.example.Translator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        TextView txtInfo = (TextView)findViewById(R.id.textView);
        String word = getIntent().getExtras().getString("word");
        txtInfo.setText(word);
    }

    public void onBackPressed() {
        Intent i = new Intent(SecondActivity.this, MyActivity.class);
        startActivity(i);
        finish();
    }
}

