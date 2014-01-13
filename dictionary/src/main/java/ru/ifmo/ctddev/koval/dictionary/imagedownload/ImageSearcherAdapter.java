package ru.ifmo.ctddev.koval.dictionary.imagedownload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ImageSearcherAdapter extends BaseAdapter {
    public static final int DEFAULT_SIZE_FILTER = 500;
    public static final int DEFAULT_IMAGE_LIMIT = 20;
    public static final String TAG = "ImageSearcherAdapter";

    private ImageSearcher imageSearcher;
    private Context context;
    private List<Bitmap> bitmaps;




    private void init(Context context) {
        this.context = context;
        imageSearcher = new BingImageSearch(DEFAULT_SIZE_FILTER, DEFAULT_SIZE_FILTER, DEFAULT_IMAGE_LIMIT);
    }


   public ImageSearcherAdapter(Context context) {
        init(context);
        bitmaps = new ArrayList<Bitmap>();


    }

   public ImageSearcherAdapter(Context context, Collection<Bitmap> collection) {
        init(context);
        bitmaps = new ArrayList<Bitmap>(collection);
    }


    public void addImagesByRequest(String imageSearchRequest) {
        AsyncImageAdder imageAdder = new AsyncImageAdder(imageSearchRequest);
        imageAdder.execute();
    }


    private class AsyncImageAdder extends AsyncTask<Void, Bitmap, Void> {

        public static final String ERROR_MESSAGE = "Error, during adding images";

        private final String imageSearchRequest;

        private AsyncImageAdder(String imageSearchRequest) {
            this.imageSearchRequest = imageSearchRequest;
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
        protected Void doInBackground(Void... params) {

            try {
                List<ResponseImage>  responseImageList = imageSearcher.search(imageSearchRequest);

                for (ResponseImage responseImage : responseImageList) {
                    Bitmap downloadedImage = getBitmapFromURL(responseImage.getImageURL());
                    publishProgress(downloadedImage);
                }
            } catch (ImageSearcherException e) {
                Log.e(TAG, ERROR_MESSAGE, e);

            }
            return null;

        }


        @Override
        protected void onProgressUpdate(Bitmap... values) {
            super.onProgressUpdate(values);
            add(values[0]);// notify adapter by default

        }
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


    public void add(Bitmap bitmap) {
        bitmaps.add(bitmap);
        notifyDataSetChanged();
    }

}
