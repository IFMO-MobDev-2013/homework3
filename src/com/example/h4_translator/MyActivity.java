package com.example.h4_translator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MyActivity extends Activity {

    Intent intent;
    EditText source;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        intent = new Intent(this, TranslationActivity.class);
        source = (EditText)findViewById(R.id.editText);
    }

    public void toStartTranslation(View v)
    {
        if (source.getText().toString().equals(""))
        {
            Toast.makeText(this,"Введите текст для перевода", Toast.LENGTH_SHORT).show();
            return;
        }
        intent.putExtra("str",source.getText().toString());
        startActivity(intent);
    }
}
