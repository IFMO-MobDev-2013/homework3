package com.ifmo.gosugdrTeam.lesson3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ImagesFragment extends Fragment {

    String[] urls;
    ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    AbsListView listView;
    ImageAdapter imageAdapter;

    public void updateData() {
        urls = Request.getImages();
        imageAdapter = new ImageAdapter();
        ((GridView) listView).setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmet_images_grid, container,
                false);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getActivity()).build();
        ImageLoader.getInstance().init(config);
        options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true).cacheInMemory(true)
                .cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(20))
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .displayer(new FadeInBitmapDisplayer(1000)).build();
        urls = Request.images;
        imageAdapter = new ImageAdapter();
        listView = (GridView) view.findViewById(R.id.gridview);
        ((GridView) listView).setAdapter(imageAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                startImagePagerActivity(position);
            }
        });
        return view;
    }

    private void startImagePagerActivity(int position) {
        Intent intent = new Intent(getActivity(), ImageViewerActivity.class);
        intent.putExtra("com.ifmo.gosugdrTeam.lesson3.position", position);
        startActivity(intent);
    }

    public class ImageAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return urls.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ImageView imageView;
            if (convertView == null) {
                imageView = (ImageView) getActivity().getLayoutInflater()
                        .inflate(R.layout.item_grid_image, parent, false);
            } else {
                imageView = (ImageView) convertView;
            }

            imageLoader.displayImage(urls[position], imageView, options);

            return imageView;
        }
    }

}
