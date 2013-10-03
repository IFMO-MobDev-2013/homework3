package com.example.homework3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Program extends Activity {
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.main);
    }

    public void onClick(View view) {
        EditText tf = (EditText) findViewById(R.id.query);
        String origin = String.valueOf(tf.getText());
        new JsonLoader(origin, this).start();
    }

    public void changeActivity(String origin, String translated)
    {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("query", origin);
        intent.putExtra("translated", translated);
        startActivity(intent);
    }
}
