package ru.ifmo.translatemaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class InputActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void onButtonPressed(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        String text = editText.getText().toString();
        Intent intent = new Intent(this, QueryProcessor.class);
        intent.putExtra("text", text);
        startActivity(intent);
    }
}
