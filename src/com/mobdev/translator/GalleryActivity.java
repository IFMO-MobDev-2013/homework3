package com.mobdev.translator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class GalleryActivity extends Activity {
	private static ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
	public static final String WORD_EXTRA_STRING = "WORD";
	public static final String TRANSLATION_EXTRA_STRING = "TRANSLATION";
	public static boolean IS_DOWNLOADING = false;
	private static String word;
	private static String translation;
	private GridImageAdapter adapter;
	private GridView grv;
	private int width;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		grv = (GridView) findViewById(R.id.grv);
		Point size = new Point();
		Display display = getWindowManager().getDefaultDisplay();
		display.getSize(size);
		width = size.x;
		Intent intent = getIntent();
		String word1 = intent.getStringExtra(WORD_EXTRA_STRING);
		String translation1 = intent.getStringExtra(TRANSLATION_EXTRA_STRING);
		if (word1 != word) {
			word = word1;
			translation = translation1;
			bitmaps = new ArrayList<Bitmap>();
			(new ImageFinder()).execute(word);
			IS_DOWNLOADING = true;
			Toast toast = Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.downloading),
					Toast.LENGTH_LONG);
			toast.show();
		} else if (!IS_DOWNLOADING)
			setGridImageAdapter();
		((TextView) findViewById(R.id.txt_gallery)).setText(word + " - "
				+ translation);
	}

	private void setGridImageAdapter() {
		if (bitmaps.size() > 0) {
			adapter = new GridImageAdapter(
					getApplicationContext(),
					android.R.layout.simple_list_item_1,
					bitmaps,
					width,
					getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
			grv.setAdapter(adapter);
		} else {
			AlertDialog.Builder builder = new Builder(GalleryActivity.this);
			builder.setTitle(R.string.no_connection)
					.setMessage(R.string.try_again_question)
					.setCancelable(false)
					.setIcon(R.drawable.fold)
					.setNegativeButton(R.string.no_string,
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							})
					.setPositiveButton(R.string.yes_string,
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									bitmaps = new ArrayList<Bitmap>();
									(new ImageFinder()).execute(word);
									IS_DOWNLOADING = true;
									Toast toast = Toast.makeText(
											getApplicationContext(),
											getResources().getString(
													R.string.downloading),
											Toast.LENGTH_LONG);
									toast.show();
								}
							});
			AlertDialog noConnectionDialog = builder.create();
			noConnectionDialog.show();
		}

	}

	class ImageFinder extends AsyncTask<String, Void, String[]> {
		@Override
		protected String[] doInBackground(String... params) {
			final String version = "1.0";
			final String imgsz = "small";
			final int resPerQuery = 8;
			String[] result = new String[10];
			int head = 0;
			int offset = 0;
			while (head < 10) {
				try {
					final String query = URLEncoder.encode(params[0], "utf-8");
					URL url = new URL(
							"https://ajax.googleapis.com/ajax/services/search/images?"
									+ "v=" + version + "&q=" + query + "&rsz="
									+ resPerQuery + "&start=" + offset
									+ "&imgsz=" + imgsz);
					URLConnection connection = url.openConnection();
					String line;
					StringBuilder builder = new StringBuilder();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream()));
					while ((line = reader.readLine()) != null) {
						builder.append(line);
					}
					JSONObject json = new JSONObject(builder.toString());
					JSONArray resArray = json.getJSONObject("responseData")
							.getJSONArray("results");
					String s;
					for (int i = 0; i < resArray.length(); i++) {
						try {
							s = resArray.getJSONObject(i).getString("url");
							URLConnection imgConnection = (new URL(s)).openConnection();
							imgConnection.setConnectTimeout(500);
							BitmapFactory.decodeStream(new URL(s).openConnection().getInputStream());
							result[head] = s;
							head++;
						} catch (Exception e) {
						}
					}
				} catch (Exception e) {
				}
				offset += resPerQuery;
			}
			return result;
		}

		@Override
		protected void onPostExecute(String[] result) {
			for (int i = 0; i < result.length; i++) {
				new ImageDownloader().execute(result[i]);
			}
		}
	}

	class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bitmap;
			try {
				final String imageURL = params[0];
				bitmap = BitmapFactory.decodeStream(new URL(imageURL)
						.openConnection().getInputStream());
			} catch (Exception e) {
				bitmap = null;
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result != null && bitmaps.size()<10){
				bitmaps.add(result);
				setGridImageAdapter();
				if (bitmaps.size() == 10)
					IS_DOWNLOADING = false;
			}
		}
	}
}