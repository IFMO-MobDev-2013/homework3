package com.example.AMTranslator;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

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
            MyActivity.english_textView.setText("Internet connection problem");
            return;
        }

        ArrayList<String> arrayList = getLinks(result);
        while (arrayList.size() < 10)
            arrayList.add(MyActivity.DEFAULT_LINK);

        for (int i = 0; i < 10; i++) {
            MyActivity.imageLoader.displayImage(arrayList.get(i), MyActivity.imageView[i]);
        }
    }
}