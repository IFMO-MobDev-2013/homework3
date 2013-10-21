package com.example.raktranslator;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;

public class SecondPage extends Activity {
    public static final String TEXT = "";

    public class async extends AsyncTask<String, Void, String> {
        public async() {
            super();
        }

        @Override
        protected String doInBackground(String... s) {
            String res = "";
            try {
                URL url = new URL("https://translate.yandex.net/api/v1.5/tr/translate?key=trnsl.1.1.20131002T204251Z.ffc956708384ab43.742875daeb355fd9fcc5b6df75316bacb536e233&text=" + URLEncoder.encode(s[0], "UTF-8") + "&lang=en-ru&format=plain&options=1");
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                BufferedInputStream is = new BufferedInputStream(url.openStream());
                Document doc = db.parse(is);
                doc.getDocumentElement().normalize();
                NodeList nodeList = doc.getElementsByTagName("text");
                Node node = nodeList.item(0).getFirstChild();
                res = node.getNodeValue();
            } catch (IOException e) {
                e.printStackTrace();
                return "htiotp";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            TextView forRes = (TextView) findViewById(R.id.textView1);
            if (!"htiotp".equals(s)) {
                forRes.setText(s);
            } else {
                Toast a = Toast.makeText(getApplicationContext(), "No Internet connection, pls enable Internet and try again", 5000);
                a.show();
                finish();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        Bundle extras = getIntent().getExtras();
        String text = extras.getString(TEXT);
        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText(tv.getText() + text);
        async a = new async();
        a.execute(text);
    }

}
