package com.example.AMTranslator;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

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
            MyActivity.english_textView.setText("Internet connection problem");
            return;
        }

        String text;
        if (onEnglish) {
            text = getResult(result);
            MyActivity.english_textView.setText("EN : " + text);
        } else
            MyActivity.russian_textView.setText("RU : " + getResult(result));
    }
}
