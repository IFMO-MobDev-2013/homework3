package com.ifmo.gosugdrTeam.lesson3;

import java.util.Arrays;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImagesFragment extends Fragment implements UpdateableFragment {

	static String[] urls;
	static ImageLoader imageLoader = ImageLoader.getInstance();
	static DisplayImageOptions options;
	static AbsListView listView;
	static ImageAdapter imageAdapter;
	static View view;

	@Override
	public void updateFragment() {
		urls = Request.getImages();
		Log.i("pictures2", Arrays.toString(urls));
		imageAdapter = new ImageAdapter();
		imageAdapter.notifyDataSetChanged();
		if (listView != null) {
			((GridView) listView).setAdapter(imageAdapter);
		}
	}
	
	Activity activity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragmet_images_grid, container, false);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getActivity()).build();
		ImageLoader.getInstance().init(config);
		options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheInMemory(true)
				.cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(20))
				.bitmapConfig(Bitmap.Config.ARGB_8888)
				.displayer(new FadeInBitmapDisplayer(1000)).build();
		urls = Request.images;
		this.activity = getActivity();
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
				imageView = (ImageView) activity.getLayoutInflater()
						.inflate(R.layout.item_grid_image, parent, false);
			} else {
				imageView = (ImageView) convertView;
			}

			imageLoader.displayImage(urls[position], imageView, options);

			return imageView;
		}
	}

}
