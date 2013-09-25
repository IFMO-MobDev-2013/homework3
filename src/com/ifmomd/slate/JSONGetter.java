package com.ifmomd.slate;

import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

class JSONGetter extends AsyncTask<String, Void, JSONObject> {
    interface ResultHandler {
        void handleResult(JSONObject result);
    }

    public JSONGetter(ResultHandler onCompleteCallback) {
        mOnCompleteCallback = onCompleteCallback;
    }

    private ResultHandler mOnCompleteCallback;

    @Override
    protected JSONObject doInBackground(String... urls) {
        URL url = null;
        try {
            if (urls.length > 0)
                url = new URL(urls[0]);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        JSONObject result = null;
        try {
            InputStream is = url.openStream();
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            result = new JSONObject(new String(buffer));
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
