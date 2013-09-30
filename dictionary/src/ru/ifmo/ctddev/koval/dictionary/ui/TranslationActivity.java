package ru.ifmo.ctddev.koval.dictionary.ui;

import android.app.Activity;
import android.os.Bundle;
import ru.ifmo.ctddev.koval.dictionary.R;

/**
 * @author ndkoval
 */
public class TranslationActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translation);
    }
}
