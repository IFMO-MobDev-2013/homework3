package com.ifmo.gosugdrTeam.lesson3;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

	static private Fragment translateFragment;
	static private ImagesFragment imagesFragment;
	public AppSectionsPagerAdapter(FragmentManager fm) {
		super(fm);
		translateFragment = new TranslateFragment();
		imagesFragment = new ImagesFragment();
	}
	
	public void updateData() {
		TranslateFragment.updateView();
		imagesFragment.updateData();
	}

	@Override
	public Fragment getItem(int i) {
		if (i == 1) {
			return translateFragment;
		}
		return imagesFragment;
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if (position == 1) {
			return "Translation";
		} else {
			return "Images";
		}
	}
}