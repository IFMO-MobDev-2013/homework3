package com.example.lection3;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final EditText p = ((EditText) findViewById(R.id.editText4));
		Button button2 = (Button) (findViewById(R.id.back));
		final Intent intent = new Intent(MainActivity.this, MainActivity1.class);
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent.putExtra("word", p.getText().toString());
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
