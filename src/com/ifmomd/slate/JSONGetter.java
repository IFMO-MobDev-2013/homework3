package com.ifmomd.slate;

import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

class JSONGetter extends AsyncTask<String, Void, JSONObject> {
    interface ResultHandler {
        void handleResult(JSONObject result);
    }

    public JSONGetter(ResultHandler onCompleteCallback) {
        mOnCompleteCallback = onCompleteCallback;
    }

    private ResultHandler mOnCompleteCallback;

    @Override
    protected JSONObject doInBackground(String... uris) {
        URI uri = null;
        try {
            if (uris.length > 0) {
                uri = new URI(uris[0]);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        JSONObject result = null;
        try {
            HttpGet request = new HttpGet(uri);
            HttpResponse response = new DefaultHttpClient().execute(request);
            BufferedReader content = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder sb = new StringBuilder();
            String s;
            while ((s = content.readLine()) != null)
                sb.append(s);
            result = new JSONObject(sb.toString());
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        mOnCompleteCallback.handleResult(jsonObject);
    }
}
