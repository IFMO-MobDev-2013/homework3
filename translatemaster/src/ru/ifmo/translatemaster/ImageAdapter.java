package ru.ifmo.translatemaster;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ViewAnimator;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Bitmap> bitmaps;

    public ImageAdapter(Context c, ArrayList<Bitmap> bitmaps) {
        mContext = c;
        this.bitmaps = bitmaps;
    }

    @Override
    public int getCount() {
        if(bitmaps == null){
            return 0;
        }
        return bitmaps.size();
    }

    @Override
    public Object getItem(int i) {
        if(bitmaps == null){
            return null;
        }
        return bitmaps.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(bitmaps.get(position));
        return imageView;
    }
}
