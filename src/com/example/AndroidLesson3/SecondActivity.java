package com.example.AndroidLesson3;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: Xottab
 * Date: 20.09.13
 * Time: 14:53
 * To change this template use File | Settings | File Templates.
 */
public class SecondActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello);
        TextView name = (TextView) findViewById(R.id.textView1);
        name.setText(getIntent().getExtras().getCharSequence("name"));
    }
}