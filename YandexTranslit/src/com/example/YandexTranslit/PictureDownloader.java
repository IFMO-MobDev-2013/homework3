package com.example.YandexTranslit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class PictureDownloader extends Thread {
    String link;
    private Bitmap picture;

    public PictureDownloader(String link) {
        this.link = link;
    }

    @Override
    public void run() {
        getPicture();
    }

    private void getPicture() {
        Bitmap otherPicture = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        try {
            URL urlImage = new URL("http://ourqueanbeyan.com/wp-content/uploads/2011/11/image-not-found.gif");

            URLConnection connect = urlImage.openConnection();
            connect.connect();

            BufferedInputStream stream = new BufferedInputStream(connect.getInputStream());
            otherPicture = BitmapFactory.decodeStream(stream);
        }
        catch (MalformedURLException e)  {

        }
        catch (IOException e) {

        }

        try {
            URL urlImage = new URL(link);

            URLConnection connect = urlImage.openConnection();
            connect.connect();

            BufferedInputStream stream = new BufferedInputStream(connect.getInputStream());
            picture = BitmapFactory.decodeStream(stream);
        }
        catch (MalformedURLException e)  {
            picture = otherPicture;
        }
        catch (IOException e) {
            picture = otherPicture;
        }
    }

    public Bitmap getBitmap() {
        return picture;
    }
}
