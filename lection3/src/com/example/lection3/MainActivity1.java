package com.example.lection3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import org.json.JSONException;

public class MainActivity1 extends Activity {

	private final String key = "key=trnsl.1.1.20130921T143302Z.c4c94e2b25dc1cf0.d4f94454ba5c56b03974e132bdacfe538a600df3&text=";
	private String translate;
	private String result;
	private String adress = "https://translate.yandex.net/api/v1.5/tr/translate?";
	private String end = "&lang=ru&format=html";

	TextView t;
	URL url;
	LinearLayout imgBox;
    RelativeLayout mainLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sergej);
		t = ((TextView) findViewById(R.id.editText3));
		//photos = (ScrollView)findViewById(R.id.scrollView1);		
        imgBox = (LinearLayout)findViewById(R.id.imageContainer);
        mainLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        new InternetOperations().execute();
        Button back = (Button)findViewById(R.id.back);
        final Intent intent = new Intent(MainActivity1.this, MainActivity.class);
        back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
			    MainActivity1.this.finish();
                setResult(RESULT_OK);
			}
		});
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;

	}

	private void ParserToTranslate() {		
		result = translate.replaceAll(" ", "%20").replaceAll("&", "%26");
		
	}

	private void query() {
        BufferedReader reader = null;
        InputStreamReader streamReader = null;

        try {
			
			url = new URL(adress + key + result + end);

			URLConnection connection = url.openConnection();
            streamReader = new InputStreamReader(connection.getInputStream());
            reader = new BufferedReader(streamReader);
			String line;
			String ans = "";
			while ((line = reader.readLine()) != null) {
				ans += line;
			}
			result = "";
			int tek = 0;
			for (int i = 0; i < ans.length() - 1;) {
				if (ans.charAt(i) == '<' && ans.charAt(i + 1) == 't') {
					tek = 1;
					i = i + 6;
				} else if (tek == 0) {
					i++;
				} else if (tek == 1 && ans.charAt(i) == '<'
						&& ans.charAt(i + 1) == '/') {
					tek = 0;
				} else {
					result += ans.charAt(i);
					i++;
				}

			}
			

		} catch (MalformedURLException e) {

		} catch (IOException e) {

		} finally {
            try{
                reader.close();
                streamReader.close();
            } catch (Throwable e){
            }

        }

	}

    class InternetOperations extends AsyncTask<Void, Void, ArrayList<Drawable>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            translate = getIntent().getExtras().getString("word");
            ParserToTranslate();
        }

        @Override
        protected ArrayList<Drawable> doInBackground(Void... voids) {
            query();

            try {
                return SearchAndLoadImages.LoadPics(translate);
            } catch (IOException e){
            } catch (JSONException e){
            }

            return new ArrayList<Drawable>();
        }

        @Override
        protected void onPostExecute(ArrayList<Drawable> pics) {
            super.onPostExecute(pics);

            mainLayout.removeView(findViewById(R.id.progressBar));
            t.setText(result);
            showPics(pics);
        }
    }


    private void showPics(ArrayList<Drawable> pics){
        for (int i = 0; i < pics.size(); i++){
            ImageView imgView = new ImageView(MainActivity1.this);
            imgView.setImageDrawable(pics.get(i));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(15, 0, 15, 10);
            params.gravity = Gravity.CENTER;
            imgView.setLayoutParams(params);
            imgBox.addView(imgView);

        }

    }
}
