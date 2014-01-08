package com.example.SimpleTranslator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

public class MyActivity2 extends Activity {
	private static final String WWORD = "word";
	public static final int COUNT_IMAGES = 10;

	private ImageView[] imageViews = new ImageView[COUNT_IMAGES];
	private String translatedWord;

	private void setTextView() {
		TextView textView = (TextView) findViewById(R.id.textView);
		textView.setTextColor(Color.BLACK);
		textView.setTextSize(20);
		textView.setText(translatedWord);
	}

	private void setButton() {
		Button button = (Button) findViewById(R.id.button);
		button.setTextColor(Color.WHITE);
		button.setTextSize(15);
	}
	
	private void setImageViews() {
		imageViews[0] = (ImageView) findViewById(R.id.imageView);
		imageViews[1] = (ImageView) findViewById(R.id.imageView1);
		imageViews[2] = (ImageView) findViewById(R.id.imageView2);
		imageViews[3] = (ImageView) findViewById(R.id.imageView3);
		imageViews[4] = (ImageView) findViewById(R.id.imageView4);
		imageViews[5] = (ImageView) findViewById(R.id.imageView5);
		imageViews[6] = (ImageView) findViewById(R.id.imageView6);
		imageViews[7] = (ImageView) findViewById(R.id.imageView7);
		imageViews[8] = (ImageView) findViewById(R.id.imageView8);
		imageViews[9] = (ImageView) findViewById(R.id.imageView9);
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        Window window = getWindow();
        window.setBackgroundDrawableResource(R.drawable.url5);
	    Intent intent = getIntent();
	    translatedWord = intent.getStringExtra(WWORD);
	    setTextView();
	    setButton();
	    setImageViews();
	    new SearchAndDownloadImages().execute(translatedWord);
    }

	public class SearchAndDownloadImages extends AsyncTask<String, Bitmap, Void> {
		private int countDownoadPictures = 0;

		@Override
		protected Void doInBackground(String... params) {
			List<String> urls = ImageUrlsGetter.getImagesUrls(params[0]);
			for (String iUrl : urls) {
				try {
					publishProgress(BitmapFactory.decodeStream(new URL(iUrl).openStream()));
				} catch (Exception e) {

				}
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Bitmap... values) {
			if(countDownoadPictures < COUNT_IMAGES)
				imageViews[countDownoadPictures++].setImageBitmap(values[0]);
		}
	}

    public void click2(View v) {finish();}
}
