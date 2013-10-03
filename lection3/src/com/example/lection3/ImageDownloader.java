package com.example.xml_trying;

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

class DownloadFilesTask extends AsyncTask<LoaderConnection, Void, ScrollView> {

    @Override
    protected ScrollView doInBackground(LoaderConnection... a) {
        try{

            SearchAndLoadImages.LoadPicAndFill(a[0]);

        } catch(Exception e){

        }

        return a[0].imageView;
    }


    @Override
    protected void onPostExecute(ScrollView result) {
        super.onPostExecute(result);


    }

    @Override
    protected void onPreExecute() {

    }

}

class SearchAndLoadImages {
    private static String CodeTable = "UTF-8";
    private static String Key = "71bUwyapucP8fmhCIUvZ2Fk8NmaXmq/lj6+cXAHQooI=";

    private static String buildURLRequest(String text, String key, int number, int offset){
        String s = "";
        try{
            s = "https://api.datamarket.azure.com/Bing/Search/v1/Composite?Sources=%27image%27"  +
                    "&Query=%27" + URLEncoder.encode(text, CodeTable) +
                    "%27&Adult=%27Moderate%27&$top=" + number +
                    "&$skip=" + offset;
        } catch (UnsupportedEncodingException ex){

        }
        return s;
    }

    public static List<String> getPictureURLS(String query) throws JSONException, IOException {
        HttpGet HTTPRequest = new HttpGet(buildURLRequest(query, Key, 10, 0));
        HTTPRequest.setHeader("Authorization", "Basic " + new String(Base64.encodeBase64((Key + ":" + Key).getBytes())));
        HTTPRequest.setHeader("Accept", "application/json");
        HttpResponse HTTPResponse = new DefaultHttpClient().execute(HTTPRequest);
        StringWriter stringWriter = new StringWriter();
        IOUtils.copy(HTTPResponse.getEntity().getContent(), stringWriter, CodeTable);
        String content = stringWriter.toString();

        JSONArray results = new JSONObject(content).getJSONObject("d").getJSONArray("results");
        List<String> resultImages = new ArrayList<String>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject resultBlock = results.getJSONObject(i);
            JSONArray images = resultBlock.getJSONArray("Image");
            for (int j = 0; j < images.length(); j++) {
                JSONObject image = images.getJSONObject(j);

                String imageUrl = image.getString("MediaUrl");
                int width = image.getInt("Width");
                int height = image.getInt("Height");
                String resultImage = new String(imageUrl);
                resultImages.add(resultImage);
            }
        }

        return resultImages;
    }

    public static void LoadPicAndFill(LoaderConnection con) throws JSONException, IOException {
        List<String> LinkList = getPictureURLS(con.text);
        for (int i = 0; i < LinkList.size(); i++){
            String link = LinkList.get(i);
            try {
                ImageView imgView = new ImageView(con.context);
                imgView.setImageDrawable(grabImageFromUrl(link));
                con.imageView.addView(imgView);

            } catch (Exception e) {

            }
        }

        try{
            ImageView imgView = new ImageView(con.context);
            imgView.setImageDrawable(grabImageFromUrl("http://developer.alexanderklimov.ru/android/images/android_cat.jpg"));
            con.imageView.addView(imgView);

        } catch(Exception e){

        }


    }

    private static Drawable grabImageFromUrl(String url) throws Exception {
        Drawable a = Drawable.createFromStream(
                (InputStream) new URL(url).getContent(), "src");
        return a;
    }

}

class LoaderConnection{
    String text;
    ScrollView imageView;
    Context context;
    LoaderConnection(String s, ScrollView imgView, Context cont){
        text = s;
        imageView = imgView;
        context = cont;
    }
}
