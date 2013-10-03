package com.tourist.Translator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class WelcomeActivity extends Activity {

    public static final String NO_ALPHA = "Слово на английском языке должно содержать хотя бы одну английскую букву";

    public static boolean isLatinLetter(char c) {
        return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z');
    }

    public void submitWord(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        String word = editText.getText().toString().trim();
        boolean alpha = false;
        for (int i = 0; i < word.length(); i++) {
            if (isLatinLetter(word.charAt(i))) {
                alpha = true;
            }
        }
        if (alpha) {
            Intent intent = new Intent(this, TranslationActivity.class);
            intent.putExtra("Word", word);
            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(this, NO_ALPHA, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_welcome);
    }
}
