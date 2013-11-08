package com.example.Translater;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

public class TranslateActivity extends Activity implements View.OnClickListener {

    Button button;
    TextView tv;
    EditText et;
    URL url;
    String urlText;
    String ans = "";
    Intent intent = new Intent();
    String enans;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        button = (Button) findViewById(R.id.Trans);
        button.setOnClickListener(this);
        et = (EditText) findViewById(R.id.editid);
    }

    @Override
    public void onClick(View v) {
        enans = et.getText().toString();
        urlText = "https://translate.yandex.net/api/v1.5/tr/translate?";
        String key = "key=trnsl.1.1.20131002T214106Z.e94886ec58e56536.c0c8431e82d6696a324995f48525d88d56daf1f8";
        String text = null;
        try {
            text = "text=" + URLEncoder.encode(enans, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        String lang = "lang=ru";
        String format = "format=plain";
        String options = "options=1";
        urlText = urlText + key + "&" + text + "&" + lang + "&" + format + "&" + options;
        intent.setClass(this, MyImageView.class);
        new DownLoad().execute(urlText);
    }

    class DownLoad extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL(urlText);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("text");

                ans = nodeList.item(0).getFirstChild().getNodeValue();
                return ans;
            } catch (Exception e) {
                e.printStackTrace();
                return ("All bad");
            }
        }

        protected void onPostExecute(String result) {

            intent.putExtra(MyImageView.trans, enans + "-" + ans);
            startActivity(intent);
            finish();

        }
    }
}
