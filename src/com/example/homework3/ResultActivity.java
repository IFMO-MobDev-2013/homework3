package com.example.homework3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ResultActivity extends Activity
{
    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(new ResultView(this));
        Intent intent = getIntent();
        Log.i("Tag", "Extra: " + intent.getStringExtra("QUERY_MESSAGE"));
    }
}
