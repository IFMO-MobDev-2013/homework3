package com.example.AndroidLesson3;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: Xottab
 * Date: 20.09.13
 * Time: 14:53
 * To change this template use File | Settings | File Templates.
 */
public class TranslateActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translate);
        TextView name = (TextView) findViewById(R.id.translate);
        name.setText(getIntent().getExtras().getCharSequence("translate"));
    }
}