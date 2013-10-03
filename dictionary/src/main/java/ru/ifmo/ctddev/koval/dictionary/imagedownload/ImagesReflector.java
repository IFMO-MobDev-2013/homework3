package ru.ifmo.ctddev.koval.dictionary.imagedownload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by vladimirskipor on 10/3/13.
 */
public class ImagesReflector {

    ListView listView;
    private final Context context;

    private final ImageSearcher imageSearcher;


    public ImagesReflector(ListView listView, Context context) {
        this.listView = listView;

        this.context = context;
        imageSearcher = new BingImageSearch(400, 400, 3);


    }


    public void reflect(String imageSearchQuery) throws ImageSearcherException, ExecutionException, InterruptedException {
        List<ResponseImage> responseImageList = imageSearcher.search(imageSearchQuery);

        List<Bitmap> images = new ArrayList<>();

        ArrayAdapter<Bitmap> adapter = new ArrayAdapter<Bitmap>(context, android.R.layout.simple_list_item_1, images){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView != null) {
                    return convertView;
                } else {
                    ImageView imageView = new ImageView(context);
                    imageView.setImageBitmap(getItem(position));
                    return imageView;
                }
            }
        };
        listView.setAdapter(adapter);



        for(ResponseImage responseImage : responseImageList){
            AsyncBitmapDownloader asyncBitmapDownloader = new AsyncBitmapDownloader();
            asyncBitmapDownloader.execute(responseImage.getImageURL());
            Bitmap downloadedImage = asyncBitmapDownloader.get();
            images.add(downloadedImage);
            adapter.notifyDataSetChanged();
        }


    }


    private static class AsyncBitmapDownloader extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            String imageUrl = params[0];
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();

                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
