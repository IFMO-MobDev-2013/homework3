package com.example.homework3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Program extends Activity
{
    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.main);
    }

    public void onClick(View view)
    {
        EditText tf = (EditText) findViewById(R.id.query);
        String query = String.valueOf(tf.getText());
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("QUERY_MESSAGE", query);
        startActivity(intent);
    }
}
