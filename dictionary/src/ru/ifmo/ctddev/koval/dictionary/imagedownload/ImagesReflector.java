package ru.ifmo.ctddev.koval.dictionary.imagedownload;

import android.content.Context;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by vladimirskipor on 10/3/13.
 */
public class ImagesReflector {

    ListView listView;
    private final Context context;

    private final ImageSearcher imageSearcher;


    public ImagesReflector(ListView listView, Context context){
        this.listView = listView;

        this.context = context;
        imageSearcher = new BingImageSearch(400, 400, 10);
    }


    public void reflect(String imageSearchQuery){




    }
}
