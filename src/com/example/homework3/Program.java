package com.example.homework3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Program extends Activity {
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.main);
    }

    public void onClick(View view) {
        EditText tf = (EditText) findViewById(R.id.query);
        String origin = String.valueOf(tf.getText());
        new TranslateLoader(origin, this).start();
    }

    public void changeActivity(String origin, String translated)
    {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("query", origin);
        intent.putExtra("translated", translated);
        startActivity(intent);
    }
}
