package com.example.Translator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ImageDownloader extends AsyncTask<Void, Integer, Void> {
    String word;
    ImageView[] imageview;
    Bitmap[] bitmap;
    TextView textView;
    int error;
    int count;
    boolean flag = false;

    ImageDownloader(String word, ImageView[] imageview, TextView textView) {
        this.word = word;
        this.imageview = imageview;
        this.textView = textView;
        bitmap = new Bitmap[12];
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        String result = "";
        Scanner scanner;
        try {
            String requestUrl = "http://images.yandex.ru/yandsearch?text=" + URLEncoder.encode(word, "UTF-8");
            URL url = new URL(requestUrl);
            URLConnection connection = url.openConnection();
            scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                result += scanner.nextLine();
            }
        } catch (IOException e) {
        }
        Pattern pattern = Pattern.compile(
                "<img class=\"[^\"]{0,}preview[^\"]{0,}\" {0,}src=\"(.*?)\"");
        Matcher matcher = pattern.matcher(result);

        for (int i = 0; i < 12; i++) {
            if (matcher.find()) {
                try {
                    bitmap[i] = BitmapFactory.decodeStream(new URL(matcher.group(1)).openStream());
                } catch (IOException e) {
                    i--;
                    error++;
                    if (error == 12) {
                        publishProgress(-1);
                    }
                }
                publishProgress(i + 1);
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress[0]);
        if (progress[0] == 0) {
            textView.setText("Images not found");
            flag = true;
        } else {
            textView.setText("Downloaded " + progress[0] + "/12 images");
            count = progress[0];
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        for (int i = 0; i < 12; i++)
            imageview[i].setImageBitmap(bitmap[i]);
        if (!flag)
            textView.setText("Download complete, loaded " + count + " image(s)");
    }
}
