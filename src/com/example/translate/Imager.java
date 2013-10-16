package com.example.translate;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Tenischev
 * Date: 03.10.13
 * Time: 21:50
 * To change this template use File | Settings | File Templates.
 */
public class Imager extends Thread {
    String text;
    ArrayList<Bitmap> bitmaps;
    boolean flag = false;
    @Override
    public void run(){
        getImage();
    }
    Imager(String s)
    {
        text = s;
    }
    public ArrayList<Bitmap> getImage(){
        String query;
        try {
            query = "http://images.yandex.ru/yandsearch?text=" + URLEncoder.encode(text, "UTF-8");
            String result = "";
            URL url = new URL(query);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNext()) {
                result += scanner.nextLine();
            }
            Pattern pattern = Pattern.compile("<img class=\"[^\"]*preview[^\"]*\" (?:alt=\".*?\" )*src=\"(.*?)\"");
            Matcher matcher = pattern.matcher(result);
            bitmaps = new ArrayList<Bitmap>();
            for(int i = 0; i < 10; i++){
                if(!matcher.find()){
                    break;
                }
                try{
                    bitmaps.add(BitmapFactory.decodeStream(new URL(matcher.group(1)).openStream()));
                } catch (Exception e){
                    --i;
                }
            }
            flag = true;
            return bitmaps;

        } catch (Exception e) {
            return null;
        }
    }
}
