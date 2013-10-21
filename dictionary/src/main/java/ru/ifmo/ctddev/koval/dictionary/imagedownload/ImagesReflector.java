package ru.ifmo.ctddev.koval.dictionary.imagedownload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by vladimirskipor on 10/3/13.
 */
public class ImagesReflector {

    public static final int DEFAULT_SIZE_FILTER = 500;
    public static final int DEFAULT_IMAGE_LIMIT = 10;
    ListView listView;
    private final Context context;

    private final ImageSearcher imageSearcher;

    public ImagesReflector(ListView listView, Context context) {
        this.listView = listView;
        listView.getWidth();

        this.context = context;
        imageSearcher = new BingImageSearch(DEFAULT_SIZE_FILTER, DEFAULT_SIZE_FILTER, DEFAULT_IMAGE_LIMIT);


    }


    public void reflect(String imageSearchQuery) throws ImageSearcherException, ExecutionException {
        List<ResponseImage> responseImageList = imageSearcher.search(imageSearchQuery);
//
//        List<Bitmap> images = new ArrayList<>();
//
//        ArrayAdapter<Bitmap> adapter = new ArrayAdapter<Bitmap>(context, android.R.layout.simple_list_item_1, images){
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                if (convertView != null) {
//                    return convertView;
//                } else {
//                    ImageView imageView = new ImageView(context);
//                    imageView.setImageBitmap(getItem(position));
//                    return imageView;
//                }
//            }
//        };


        ImageAdapter adapter = new ImageAdapter(context);
        listView.setAdapter(adapter);
        AsyncImageInserter asyncImageInserter = new AsyncImageInserter(adapter);
        //noinspection unchecked
        asyncImageInserter.execute(responseImageList);


    }


    private static class AsyncImageInserter extends AsyncTask<List<ResponseImage>, Bitmap, Void> {

        private ImageAdapter adapter;
        private AsyncImageInserter(ImageAdapter adapter) {
            this.adapter = adapter;
        }

        private Bitmap getBitmapFromURL(String imageUrl) {
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


        @Override
        protected Void doInBackground(List<ResponseImage>... params) {
            List<ResponseImage> responseImageList = params[0];
            for (ResponseImage responseImage : responseImageList) {
                Bitmap downloadedImage = getBitmapFromURL(responseImage.getImageURL());
                publishProgress(downloadedImage);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Bitmap... values) {
            super.onProgressUpdate(values);
            adapter.add(values[0]);// notify adapter by default

        }
    }
}


class ImageAdapter extends BaseAdapter {

    private Context context;

    private List<Bitmap> bitmaps;

    ImageAdapter(Context context) {
        this.context = context;
        bitmaps = new ArrayList<Bitmap>();
    }

    ImageAdapter(Context context, Collection<Bitmap> collection) {
        this.context = context;
        bitmaps = new ArrayList<Bitmap>(collection);
    }

    @Override
    public int getCount() {

        return bitmaps.size();
    }

    @Override
    public Bitmap getItem(int position) {
        return bitmaps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setImageBitmap(getItem(position));
        } else {
            imageView = ((ImageView) convertView);
            imageView.setImageBitmap(getItem(position));
        }
        return imageView;
    }


    void add(Bitmap bitmap) {
        bitmaps.add(bitmap);
        notifyDataSetChanged();
    }
}

