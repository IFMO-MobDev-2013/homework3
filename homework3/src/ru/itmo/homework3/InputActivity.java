package ru.itmo.homework3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ru.itmo.homework3.R;

/**
 * Created by Nick Smelik on 02.10.13.
 */

public class InputActivity extends Activity {

    private static final String KEY = "trnsl.1.1.20131002T150025Z.d95b6a9ec1b011d6.7161fc9f2e5f26c56c62d41ad1f49444b3524c19";
    private static final String URL = "https://translate.yandex.net/api/v1.5/tr.json/translate?";
    private static final String LANGUAGE = "en-ru";
    private static final String FORMAT = "http";
    private static final HttpClient HTTP_CLIENT = new DefaultHttpClient();
    private static final int GOOD_REQUEST = 200;


    private class RequestAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String word = params[0];

            HttpPost post = new HttpPost(URL);

            List<NameValuePair> request = new ArrayList();
            request.add(new BasicNameValuePair("key", KEY));
            request.add(new BasicNameValuePair("lang", LANGUAGE));
            request.add(new BasicNameValuePair("format", FORMAT));
            request.add(new BasicNameValuePair("text", word));

            try {
                post.setEntity(new UrlEncodedFormEntity(request, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                return null;
            }

            String jsonResponse;
            try {
                HttpResponse httpResponse = HTTP_CLIENT.execute(post);
                BufferedReader buffer= new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
                jsonResponse = buffer.readLine();
            } catch (IOException e) {
                return null;
            }

            try {
                JSONObject jObject = new JSONObject(jsonResponse);

                int code = jObject.getInt("code");
                if (code != GOOD_REQUEST)
                    return "Bad request";

                String result = jObject.getString("text");
                return result.substring(2, result.length() - 2);
            } catch (JSONException e) {
                return null;
            }
        }
    }

    public String getTranslate(String word)
    {
        try {
            return new RequestAsyncTask().execute(word).get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_input);

        final Intent changeToTranslation = new Intent(this, TranslateActivity.class);

        final Button button1 = (Button) findViewById(R.id.startTranslate);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String sourseText = ((EditText) findViewById(R.id.input)).getText().toString();

                    String translation = getTranslate(sourseText);

                    changeToTranslation.putExtra("word", sourseText);
                    changeToTranslation.putExtra("translate", translation);
                } catch (Exception e){

                }
                startActivity(changeToTranslation);
            }
        });
    }

}
