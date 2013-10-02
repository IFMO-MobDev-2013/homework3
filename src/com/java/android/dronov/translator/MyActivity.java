package com.java.android.dronov.translator;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        new A(){
            @Override
            protected void onPostExecute(String s) {
                 updateUi(Request.result);
            }
        };
    }

    class A extends AsyncTask<Object, Integer, String>{

        @Override
        protected String doInBackground(Object... objects) {
            Request.result = "";
        }
    }
}
