package com.example.Translate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;



public class MainActivity extends Activity {
    public String answer = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button button = (Button) findViewById(R.id.translate);
        final EditText editText = (EditText) findViewById(R.id.editText);
        editText.setTextSize(30);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = editText.getText().toString();
                Intent intent = new Intent(MainActivity.this,Show.class);
                intent.putExtra("word",word);
                startActivity(intent);

            }
        });



    }
}


