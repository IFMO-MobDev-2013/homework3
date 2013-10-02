package com.example.translate;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MyActivity extends Activity {

    MyTask mt;
    public static String s;

    public static String translate() throws IOException, JSONException {
        String apiKey = "trnsl.1.1.20131002T153823Z.1d17445a09437572.c4ab6b9e93d5810dbcba649e52d5049acacfd8d9";
        String requestUrl = "https://translate.yandex.net/api/v1.5/tr.json/translate?key="
                + apiKey + "&text=" + URLEncoder.encode(s, "UTF-8") + "&lang=ru-en&format=plain";

        URL url = new URL(requestUrl);
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        httpConnection.connect();
        int rc = httpConnection.getResponseCode();

        if (rc == 200) {
            String line = null;
            BufferedReader buffReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            StringBuilder strBuilder = new StringBuilder();
            while ((line = buffReader.readLine()) != null) {
                strBuilder.append(line + '\n');
            }
            return parser(strBuilder.toString()).substring(2, parser(strBuilder.toString()).length() - 2);
        }
        return null;
    }

    public static String parser(String s) throws JSONException {
        JSONObject object = (JSONObject) new JSONTokener(s).nextValue();
        return object.getString("text");
    }

    class MyTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                //translate
                s = translate();
                return s;
            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Button button = (Button) findViewById(R.id.button);
        final EditText text = (EditText) findViewById(R.id.editText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View veiw) {
                s = text.getText().toString();
                mt = new MyTask();
                mt.execute();
                String trans=null;
                try {
                    trans = mt.get(1,TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                if (trans==null)
                {
                Toast toast = Toast.makeText(getApplicationContext(),"Не удалось перевести",5000);
                toast.show();
                }else
                {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), SecondActivity.class);
                intent.putExtra("translate", trans);
                startActivity(intent);
                }
            }
        });
    }
}
