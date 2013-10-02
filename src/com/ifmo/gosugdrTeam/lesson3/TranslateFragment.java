package com.ifmo.gosugdrTeam.lesson3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TranslateFragment extends Fragment {

	private static View translateView;
	private static TextView englishVersion;
	private static TextView russianVersion;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		translateView = inflater.inflate(R.layout.fragment_translation,
				container, false);
		updateView();
		return translateView;
	}
	
	public static void updateView() {
		englishVersion = (TextView) translateView.findViewById(R.id.englishVersionSourceTextView);
		russianVersion = (TextView) translateView.findViewById(R.id.russianVersionSourceTextView);
		englishVersion.setText(Request.getQuery());
		russianVersion.setText(Request.getTranslation());
	}
}