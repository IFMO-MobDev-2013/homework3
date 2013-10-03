package com.example.homework3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class FirstActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private Intent intent;
    private EditText et;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        et = (EditText)findViewById(R.id.textField);
    }

    public void onButtonClick(View v) {
        if (et.getText().length() != 0) {
            intent = new Intent(this, SecondActivity.class);
            intent.putExtra("Word", et.getText().toString());
            startActivity(intent);
        }

    }
}
