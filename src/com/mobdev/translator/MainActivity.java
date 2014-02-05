package com.mobdev.translator;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	private String[] examples;
	private String word;
	private String translation = "";
	public static final String WORD_EXTRA_STRING = "WORD";
	public static final String TRANSLATION_EXTRA_STRING = "TRANSLATION";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView txt = (TextView) findViewById(R.id.txt);
		examples = getResources().getStringArray(R.array.examples);
		int index = (new Random()).nextInt(examples.length);
		Spannable example = new SpannableString(" " + examples[index]);
		example.setSpan(new UnderlineSpan(), 1, example.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		txt.setText(example);
		txt.setTag(index);
		txt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				((EditText) findViewById(R.id.edit))
						.setText(examples[(Integer) v.getTag()]);
			}
		});

		((Button) findViewById(R.id.btn))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						word = ((EditText) findViewById(R.id.edit)).getText()
								.toString();
						boolean f1 = word.length() > 0;
						boolean f2 = false;
						for (int i = 0; i < word.length(); ++i)
							if (isEnChar(word.charAt(i))) {
								f2 = true;
								break;
							}
						if (f1 && f2) {
							(new Translator()).execute(word);
						} else {
							(new Dialogs())
									.showEmptyEditTextDialog(MainActivity.this);
						}
					}
				});
	}

	public static boolean isEnChar(char c) {
		return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
	}

	class Translator extends AsyncTask<String, Void, String> {
		protected String address;
		public static final String apiKey = "trnsl.1.1.20140204T131421Z.6155670a2ca33421.3b1d7ce81a75eef6449c31c6c50d33f9a9ff8619";
		public static final String yandexUrl = "https://translate.yandex.net/api/v1.5/tr/translate";

		@Override
		protected String doInBackground(String... params) {
			StringBuilder sb = new StringBuilder();
			String result;
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = factory.newDocumentBuilder();
				Document doc;
				address = yandexUrl + "?key=" + apiKey + "&text="
						+ URLEncoder.encode(params[0], "UTF-8") + "&lang=ru";
				URL url = new URL(address);
				HttpURLConnection connect = (HttpURLConnection) url
						.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						new BufferedInputStream(connect.getInputStream())));
				String temp;
				while ((temp = in.readLine()) != null) {
					sb.append(temp);
				}

				String translationXml = sb.toString();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(sb.toString()));
				doc = db.parse(is);
				connect.disconnect();
				if (translationXml == null) {
					result = null;
				} else {
					result = doc.getElementsByTagName("text").item(0)
							.getTextContent();
				}
			} catch (Exception e) {
				result = null;
			}

			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null){
				AlertDialog.Builder builder = new Builder(MainActivity.this);
				builder.setTitle(R.string.no_connection)
						.setMessage(R.string.try_again_question)
						.setCancelable(false)
						.setIcon(R.drawable.fold)
						.setNegativeButton(R.string.no_string, new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

							}
						})
						.setPositiveButton(R.string.yes_string, new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								(new Translator()).execute(word);
							}
						});
				AlertDialog noConnectionDialog = builder.create();
				noConnectionDialog.show();
			}
			else {
				translation = result;
				AlertDialog.Builder builder = new Builder(MainActivity.this);
				builder.setTitle(word+" - "+translation)
						.setMessage(R.string.show_question)
						.setCancelable(false)
						.setIcon(R.drawable.tick)
						.setNegativeButton(R.string.no_string, new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

							}
						})
						.setPositiveButton(R.string.yes_string, new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent i = new Intent(MainActivity.this, GalleryActivity.class);
								i.putExtra(WORD_EXTRA_STRING, word);
								i.putExtra(TRANSLATION_EXTRA_STRING, translation);
								startActivity(i);
							}
						});
				AlertDialog translationDialog = builder.create();
				translationDialog.show();
			}
				
		}
	}

}
