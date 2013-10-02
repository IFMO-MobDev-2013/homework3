package com.ifmo.gosugdrTeam.lesson3;



public abstract class Request {
	protected static String query;
	protected static String[] images;
	protected static String translation;

	public static String getTranslation() {
		return translation;
	}

	public static String[] getImages() {
		return images;
	}

	public static String getQuery() {
		return query;
	}

	public static void setQuery(String query) {
		Request.query = query;
	}
}
