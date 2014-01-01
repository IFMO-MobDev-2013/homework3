package com.example.task3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import org.json.JSONException;

import java.util.List;


public class SecondActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pictures);
        TextView translation = (TextView) findViewById(R.id.textview);
        String text = getIntent().getStringExtra("translation");
        String results = getIntent().getStringExtra("pictures");
        GridView gridView = (GridView) findViewById(R.id.gridView);
        ImageAdapter imageAdapter = new ImageAdapter(this);
        gridView.setAdapter(imageAdapter);
        try {
            imageAdapter.update(results);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        translation.setText(text);

    }

    public void onBackPressed() {
        Intent i = new Intent(SecondActivity.this, MyActivity.class);
        startActivity(i);
        finish();
    }
}
