package com.ifmo.gosugdrTeam.lesson3;

import android.os.Parcelable;

public abstract class Request{
	protected static String query;
	protected static String[] images;
	protected static String translation;
	
	public Request() {
		query = "";
		images = new String[0];
		translation = "";
	}

	public static String getTranslation() {
		return translation;
	}

	public static String[] getImages() {
		return images;
	}
	
	public static void setTranslation(String arg) {
		translation = arg;
	}

	public static void setImages(String[] arg) {
		images = arg;
	}
	
	public static String getQuery() {
		return query;
	}

	public static void setQuery(String query) {
		Request.query = query;
	}
	
	
}
