package ru.georgeee.android.Silencio.utility.http;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: georgeee
 * Date: 29.09.13
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */
public abstract class JsonResponseHttpTask<Result> extends HttpTask<Result> {
    protected void handleJSONException(JSONException ex) {
        ex.printStackTrace();
    }

    protected abstract Result getResultByJson(JSONObject jsonObject);

    @Override
    protected Result getResult(HttpResponse httpResponse) throws IOException {
        // json is UTF-8 by default
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"), 8);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        JSONObject jObject = null;
        try {
            jObject = new JSONObject(sb.toString());
        } catch (JSONException e) {
            handleJSONException(e);
        }
        return getResultByJson(jObject);
    }
}
