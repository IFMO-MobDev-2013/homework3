package com.example.AMTranslator;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MyActivity extends Activity {
    public static final String DEFAULT_LINK = "http://www.51allout.co.uk/wp-content/uploads/2012/02/Image-not-found.gif";
    private TextView english_textView;
    private TextView russian_textView;
    private ImageView[] imageView;
    private ImageLoader imageLoader;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));

        english_textView = (TextView) findViewById(R.id.english_textView);
        russian_textView = (TextView) findViewById(R.id.russian_textView);
        imageView = new ImageView[10];
        imageView[0] = (ImageView) findViewById(R.id.imageView);
        imageView[1] = (ImageView) findViewById(R.id.imageView1);
        imageView[2] = (ImageView) findViewById(R.id.imageView2);
        imageView[3] = (ImageView) findViewById(R.id.imageView3);
        imageView[4] = (ImageView) findViewById(R.id.imageView4);
        imageView[5] = (ImageView) findViewById(R.id.imageView5);
        imageView[6] = (ImageView) findViewById(R.id.imageView6);
        imageView[7] = (ImageView) findViewById(R.id.imageView7);
        imageView[8] = (ImageView) findViewById(R.id.imageView8);
        imageView[9] = (ImageView) findViewById(R.id.imageView9);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            EditText editText = (EditText) findViewById(R.id.editText);
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                new TextTranslator().execute(text, "RU");
                new TextTranslator().execute(text, "EN");
                new LinksSearcher().execute(text);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        });
    }

    // images
    public class LinksSearcher extends AsyncTask<String, String, String> {
        public static final String GOOGLE_REQUEST = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String text = params[0];
            try {
                StringBuilder strBuilder = new StringBuilder();
                URL url = new URL(GOOGLE_REQUEST + URLEncoder.encode(text, "UTF-8") + "&rsz=8&start=0");
                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.connect();
                int answer = httpConnection.getResponseCode();
                if (answer == 200) {
                    String line = "";
                    BufferedReader buffReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                    while ((line = buffReader.readLine()) != null) {
                        strBuilder.append(line + "\n");
                    }
                }

                url = new URL(GOOGLE_REQUEST + URLEncoder.encode(text, "UTF-8") + "&rsz=8&start=8");
                httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.connect();
                answer = httpConnection.getResponseCode();
                if (answer == 200) {
                    String line = "";
                    BufferedReader buffReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                    while ((line = buffReader.readLine()) != null) {
                        strBuilder.append(line + "\n");
                    }
                }

                url = new URL(GOOGLE_REQUEST + URLEncoder.encode(text, "UTF-8") + "&rsz=8&start=16");
                httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.connect();
                answer = httpConnection.getResponseCode();
                if (answer == 200) {
                    String line = "";
                    BufferedReader buffReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                    while ((line = buffReader.readLine()) != null) {
                        strBuilder.append(line + "\n");
                    }
                    return strBuilder.toString();
                }

                throw new Exception("Internet connection problem");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private ArrayList<String> getLinks(String result) {
            ArrayList<String> arrayList = new ArrayList<String>();
            int ind = result.indexOf(".jpg");
            while (ind != -1) {
                result = result.substring(0, ind) + result.substring(ind + 1, result.length());
                int ind2 = ind;
                String tmp = "";
                while (result.charAt(ind2) != '\"')
                    --ind2;
                for (int i = ind2 + 1; i < ind; ++i)
                    tmp += result.charAt(i);
                if (arrayList.size() == 0 || arrayList.get(arrayList.size() - 1).equals(tmp + ".jpg") == false) {
                    arrayList.add(tmp + ".jpg");
                    if (arrayList.size() == 10)
                        return arrayList;
                }
                ind = result.indexOf(".jpg");
            }
            return arrayList;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                english_textView.setText("Internet connection problem");
                return;
            }

            ArrayList<String> arrayList = getLinks(result);
            while (arrayList.size() < 10)
                arrayList.add(MyActivity.DEFAULT_LINK);

            for (int i = 0; i < 10; i++) {
                imageLoader.displayImage(arrayList.get(i), imageView[i]);
            }
        }
    }

    // text
    public class TextTranslator extends AsyncTask<String, String, String> {
        public static final String YANDEX_REQUEST = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20131225T145721Z.363310fbf6588e50.ee30817a1e84449e23efefda7d3638ab48331f24&text=";
        boolean onEnglish;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String text = params[0];
            onEnglish = params[1].equals("EN");
            if (text.indexOf(" ") != -1) text.replaceAll("%20", " ");

            try {
                URL url;
                if (onEnglish)
                    url = new URL(YANDEX_REQUEST + URLEncoder.encode(text, "UTF-8") + "&lang=en");
                else
                    url = new URL(YANDEX_REQUEST + URLEncoder.encode(text, "UTF-8") + "&lang=ru");
                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.connect();
                int answer = httpConnection.getResponseCode();
                if (answer == 200) {
                    String line = "";
                    BufferedReader buffReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                    StringBuilder strBuilder = new StringBuilder();
                    while ((line = buffReader.readLine()) != null) {
                        strBuilder.append(line + "\n");
                    }
                    return strBuilder.toString();
                }
                throw new Exception("Internet connection problem");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        private String getResult(String result) {
            result = result.substring(result.indexOf("[") + 2, result.indexOf("]") - 1);
            int ind = result.indexOf("\\n");
            while (ind != -1) {
                result = result.substring(0, ind) + "\n" +  result.substring(ind + 2, result.length());
                ind = result.indexOf("\\n");
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == null) {
                english_textView.setText("Internet connection problem");
                return;
            }

            String text;
            if (onEnglish) {
                text = getResult(result);
                english_textView.setText("EN : " + text);
            } else
                russian_textView.setText("RU : " + getResult(result));
        }
    }
}
