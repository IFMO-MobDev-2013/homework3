package com.example.task3;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mihver1
 * Date: 03.10.13
 * Time: 21:31
 * To change this template use File | Settings | File Templates.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    public String picturesJson;
    private List<String> pictures;

    public void update(String newPictures) throws JSONException {
        pictures.clear();
        picturesJson = newPictures;

        JSONArray results = new JSONObject(picturesJson).getJSONObject("d").getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject object = results.getJSONObject(i);
            JSONArray images = object.getJSONArray("Image");
            for (int j = 0; j < images.length(); j++) {
                JSONObject image = images.getJSONObject(j);
                String imageUrl = image.getString("MediaUrl");
                String resultImage = imageUrl;
                pictures.add(resultImage);
            }
        }
    }

    // Gets the context so it can be used later
    public ImageAdapter(Context c) {
        mContext = c;
        pictures = new ArrayList<String>();
    }

    // Total number of things contained within the adapter
    public int getCount() {
        return pictures.size();
    }

    // Require for structure, not really used in my code.
    public Object getItem(int position) {
        return null;
    }

    // Require for structure, not really used in my code. Can
    // be used to get the id of an item in the adapter for
    // manual control.
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position,
                        View convertView, ViewGroup parent) {
        WebImageView img = new WebImageView(mContext);
        img.setImageUrl(pictures.get(position));
        return img;
    }
}

