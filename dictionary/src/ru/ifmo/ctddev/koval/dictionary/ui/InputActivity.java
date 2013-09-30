package ru.ifmo.ctddev.koval.dictionary.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import ru.ifmo.ctddev.koval.dictionary.R;

public class InputActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input);
    }

    public void changeActivity(View view) {
        Intent translationIntent = new Intent(view.getContext(), TranslationActivity.class);
        startActivityForResult(translationIntent, 0);
    }
}
