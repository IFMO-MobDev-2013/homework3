package ru.georgeee.android.Silencio.utility.http;

import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Created with IntelliJ IDEA.
 * User: georgeee
 * Date: 29.09.13
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */
public abstract class HttpTask<Result> extends AsyncTask<Object, Float, Result> {
    protected String[] bodyParams;

    public String[] getBodyParams() {
        return bodyParams;
    }

    public void setBodyParams(String[] bodyParams) {
        this.bodyParams = bodyParams;
    }

    public HttpClient getHttpClient() {
        return HttpUtility.getInstance().getMultiThreadHttpClient();
    }

    protected abstract HttpRequestBase getHttpRequestBase();

    protected abstract Executor getExecutor();

    protected void handleHttpIOException(IOException ex) {
        ex.printStackTrace();
    }

    protected abstract Result getResult(HttpResponse httpResponse) throws IOException;

    @Override
    protected Result doInBackground(Object... params) {
        HttpRequestBase base = getHttpRequestBase();
        HttpResponse httpResponse;
        try {
            httpResponse = getHttpClient().execute(base);
            return getResult(httpResponse);
        } catch (IOException ex) {
            handleHttpIOException(ex);
            return null;
        }
    }

    public AsyncTask<Object, Float, Result> executeOnHttpTaskExecutor() {
        return executeOnExecutor(getExecutor());
    }

    protected String composeUrl(String urlBase, Map<String, String> getParams) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder(urlBase);
        boolean isFirst = true;
        for (String key : getParams.keySet()) {
            String value = getParams.get(key);
            if (isFirst) {
                sb.append('?');
                isFirst = false;
            } else sb.append('&');
            sb.append(key).append('=').append(URLEncoder.encode(value, "UTF-8"));
        }
        return sb.toString();
    }
}
