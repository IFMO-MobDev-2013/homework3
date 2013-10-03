package com.example.Translate;


import android.os.AsyncTask;
import java.io.InputStream;
import java.net.URLEncoder;


public class    Translater extends AsyncTask<Void, Void, String> {
    String word;
    Translater(String word) {
        this.word = URLEncoder.encode(word);
    }

    public String onPostExecute(String... result) {
        super.onPostExecute(result[0]);
        return result[0];
    }

    @Override
    public String doInBackground(Void... parameter) {
        String url = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20130923T111105Z.d461ad359432a880.b1bbf827f59c7207f6aa527fc2b67ecdf52daf68&text=" + word + "&lang=ru-en";
        String Text;
        try {
            InputStream in = new java.net.URL(url).openStream();
            java.util.Scanner str = new java.util.Scanner(in).useDelimiter("\\A");
            Text = str.hasNext() ? str.next() : "";

        } catch (Exception e) {
            return "can't connect to internet";
        }
        int i1 = Text.indexOf('[') + 2;
        int i2 = Text.indexOf(']') - 1;
        return Text.substring(i1,i2);
    }
}





