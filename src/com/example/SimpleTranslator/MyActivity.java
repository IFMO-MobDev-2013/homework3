package com.example.SimpleTranslator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    EditText edittext;
    TextView textView, textView1;
    Spinner spinner, spinner1;
    String to,from;

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Window window = getWindow();
        window.setBackgroundDrawableResource(R.drawable.url5);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setBackgroundColor(Color.BLACK);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setBackgroundColor(Color.BLACK);
        edittext = (EditText) findViewById(R.id.editText);
        edittext.setBackgroundResource(R.drawable.editback);
        edittext.setTextColor(Color.BLACK);
        textView = (TextView) findViewById(R.id.textView);
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView1.setTextSize(20);
        textView1.setTextColor(Color.BLACK);

    }

    private String getLanguage(Spinner spinner) {
        String s = spinner.getSelectedItem().toString();
        if (s.equals("Русский")) {
            s = "ru";

        } else if (s.equals("Английский")) {
            s = "en";

        } else if (s.equals("Немецкий")) {
            s = "de";

        } else if (s.equals("Испанский")) {
            s = "es";

        } else if (s.equals("Французский")) {
            s = "fr";
        }

        return s;
    }

    public void click1(View v) {
        from = getLanguage(spinner);
        to = getLanguage(spinner1);
        Intent intent = new Intent(MyActivity.this, MyActivity2.class);
        intent.putExtra("word", edittext.getText().toString());
        intent.putExtra("from", from);
        intent.putExtra("to", to);
        edittext.setText("");
        startActivity(intent);
    }
}
