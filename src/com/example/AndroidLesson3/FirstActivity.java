package com.example.AndroidLesson3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FirstActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button submit = (Button) findViewById(R.id.button);
        final Intent intent = new Intent(this,SecondActivity.class);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence sequence = ((EditText) findViewById(R.id.editText)).getText();
                intent.putExtra("name", sequence);
                startActivity(intent);
            }
        });
    }

}
