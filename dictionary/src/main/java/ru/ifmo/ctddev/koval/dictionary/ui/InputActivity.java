package ru.ifmo.ctddev.koval.dictionary.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import ru.ifmo.ctddev.koval.dictionary.R;

public class InputActivity extends Activity {

    private EditText input;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input);
        input = (EditText) findViewById(R.id.translationInput);
    }

    public void changeActivity(View view) {
        Intent translationIntent = new Intent(view.getContext(), TranslationActivity.class);
        translationIntent.putExtra("inputWord", input.getText().toString());
        startActivityForResult(translationIntent, 0);
    }
}
