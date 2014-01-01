package com.example.task3;

import android.os.AsyncTask;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mihver1
 * Date: 02.10.13
 * Time: 18:00
 * To change this template use File | Settings | File Templates.
 */
public class BingImageSearch extends AsyncTask<String, Void, String> {

    private static final String BING_SEARCH = "https://api.datamarket.azure.com/Bing/Search/v1/Composite" +
            "?Sources=%%27image%%27" +
            "&Query=%%27" + "%s" + "%%27" +
            "&ImageFilters=%%27Size%%3ASmall%%27" +
            "&$top=" + "%d";
    private static final String accountKey = "pYurF1x1xr+c7M0F6TLbl7KySmVMbphBJPLR0HwTZug=";

    @Override
    protected String doInBackground(String... params) {

        byte[] accountKeyBytes = Base64.encodeBase64((accountKey + ":" + accountKey).getBytes());
        String accountKeyEnc = new String(accountKeyBytes);

        String bingUrl = null;
        try {
            bingUrl = String.format(BING_SEARCH, URLEncoder.encode(params[0], "UTF-8"), 10);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpGet getRequest = new HttpGet(bingUrl);
        getRequest.setHeader("Authorization", "Basic " + accountKeyEnc);
        getRequest.setHeader("Accept", "application/json");
        HttpResponse httpResponse = null;
        try {
            httpResponse = new DefaultHttpClient().execute(getRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringWriter stringWriter = new StringWriter();
        try {
            IOUtils.copy(httpResponse.getEntity().getContent(), stringWriter, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String content = stringWriter.toString();

        return content;
    }
}
