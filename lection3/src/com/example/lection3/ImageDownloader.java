package com.example.lection3;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.ScrollView;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: asus
 * Date: 03.10.13
 * Time: 23:44
 * To change this template use File | Settings | File Templates.
 */


class SearchAndLoadImages {
    private static String CodeTable = "UTF-8";
    private static String Key = "71bUwyapucP8fmhCIUvZ2Fk8NmaXmq/lj6+cXAHQooI=";
    private static int PictureNumber = 10;

    private static String buildURLRequest(String text, int number, int offset){
        String s = "";
        try{
            s = "https://api.datamarket.azure.com/Bing/Search/v1/Composite?Sources=%27image%27"  +
                    "&Query=%27" + URLEncoder.encode(text, CodeTable) +
                    "%27&Adult=%27Moderate%27&$top=" + number +
                    "&$skip=" + offset +
                    "&ImageFilters=%27Size%3ASmall%27";
        } catch (UnsupportedEncodingException ex){

        }
        return s;
    }

    public static ArrayList<String> getPictureURLS(String query) throws JSONException, IOException {
        HttpGet HTTPRequest = new HttpGet(buildURLRequest(query, PictureNumber, 0));
        HTTPRequest.setHeader("Authorization", "Basic " + new String(Base64.encodeBase64((Key + ":" + Key).getBytes())));
        HTTPRequest.setHeader("Accept", "application/json");
        HttpResponse HTTPResponse = new DefaultHttpClient().execute(HTTPRequest);
        StringWriter stringWriter = new StringWriter();
        IOUtils.copy(HTTPResponse.getEntity().getContent(), stringWriter, CodeTable);
        String content = stringWriter.toString();

        JSONArray results = new JSONObject(content).getJSONObject("d").getJSONArray("results");
        ArrayList<String> resultImages = new ArrayList<String>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject resultBlock = results.getJSONObject(i);
            JSONArray images = resultBlock.getJSONArray("Image");
            for (int j = 0; j < images.length(); j++) {
                JSONObject image = images.getJSONObject(j);

                String imageUrl = image.getString("MediaUrl");
                String resultImage = new String(imageUrl);
                resultImages.add(resultImage);
            }
        }

        return resultImages;
    }

    public static ArrayList<Drawable> LoadPics(String text) throws JSONException, IOException {
        ArrayList<String> LinkList = getPictureURLS(text);
        ArrayList<Drawable> Pictures = new ArrayList<Drawable>();
        for (int i = 0; i < LinkList.size(); i++){
            String link = LinkList.get(i);
            Pictures.add(grabImageFromUrl(link));

        }


        return Pictures;

    }

    private static Drawable grabImageFromUrl(String url) throws IOException {
        Drawable a = Drawable.createFromStream(
                (InputStream) new URL(url).getContent(), "src");
        return a;
    }

}
