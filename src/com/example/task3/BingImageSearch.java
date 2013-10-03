package com.example.task3;

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
public class BingImageSearch {

    private static final String BING_SEARCH = "https://api.datamarket.azure.com/Bing/Search/v1/Composite" +
            "?Sources=%%27image%%27" +
            "&Query=%%27" + "%s" + "%%27" +
            "&$top=" + "%d";
    private static final String accountKey = "pYurF1x1xr+c7M0F6TLbl7KySmVMbphBJPLR0HwTZug=";

    public List<String> execute(String query) throws IOException, JSONException {

        byte[] accountKeyBytes = Base64.encodeBase64((accountKey + ":" + accountKey).getBytes());
        String accountKeyEnc = new String(accountKeyBytes);

        String bingUrl = String.format(BING_SEARCH, URLEncoder.encode(query, "UTF-8"), 10);

        HttpGet getRequest = new HttpGet(bingUrl);
        getRequest.setHeader("Authorization", "Basic " + accountKeyEnc);
        getRequest.setHeader("Accept", "application/json");
        HttpResponse httpResponse = new DefaultHttpClient().execute(getRequest);
        StringWriter stringWriter = new StringWriter();
        IOUtils.copy(httpResponse.getEntity().getContent(), stringWriter, "UTF-8");
        String content = stringWriter.toString();

        JSONArray results = new JSONObject(content).getJSONObject("d").getJSONArray("results");
        List<String> resultImages = new ArrayList<String>();
        for (int i = 0; i < results.length(); i++) {
            JSONObject object = results.getJSONObject(i);
            JSONArray images = object.getJSONArray("Image");
            for (int j = 0; j < images.length(); j++) {
                JSONObject image = images.getJSONObject(j);

                String imageUrl = image.getString("MediaUrl");
                String resultImage = imageUrl;
                resultImages.add(resultImage);
            }
        }
        return resultImages;
    }

}
