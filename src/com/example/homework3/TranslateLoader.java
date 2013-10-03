package com.example.homework3;

import android.app.Activity;
import android.util.Log;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class TranslateLoader extends Thread
{
    private String origin = "";
    private String translated = "";
    private JSONObject data = null;
    private Program parent = null;
    private final static String KEY = "trnsl.1.1.20131003T151001Z.d29ccf314f9c4291.152604fcad2b63429bdca302baa4a4c9dc805334";

    public TranslateLoader(String origin, Program parent)
    {
        this.origin = origin;
        this.parent = parent;
    }

    public String getOrigin()
    {
        return origin;
    }

    public String getTranslated()
    {
        return translated;
    }

    public JSONObject getData()
    {
        return data;
    }

    @Override
    public void run()
    {
        try
        {
            String api_url = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + KEY + "&text="+ URLEncoder.encode(origin, "utf-8")+"&lang=ru";

            URL url = new URL(api_url);
            URLConnection connection = url.openConnection();
            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while ((line = reader.readLine()) != null)
            {
                builder.append(line);
            }

            String response = builder.toString();
            data = new JSONObject(response);
            Log.i("Tag", data.toString());
            parent.changeActivity(origin, data.getJSONArray("text").get(0).toString());
        }
        catch (Exception e)
        {

        }
    }
}
