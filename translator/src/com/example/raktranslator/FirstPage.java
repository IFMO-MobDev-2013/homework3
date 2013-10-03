package com.example.raktranslator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FirstPage extends Activity implements OnClickListener {

    Button b;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        b = (Button) findViewById(R.id.button);
        b.setOnClickListener(this);
    }

    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this, SecondPage.class);
        EditText edit = (EditText) findViewById(R.id.editText);
        String st = edit.getEditableText().toString();
        intent.putExtra(SecondPage.TEXT, st);
        startActivity(intent);
    }
}


