package com.ifmo.gosugdrTeam.lesson3;

import com.ifmo.gosugdrTeam.lesson3.background.BackgroundPictureFinder;
import com.ifmo.gosugdrTeam.lesson3.background.BackgroundWordTranslator;

public class AsyncTaskRequest extends Request {
	public AsyncTaskRequest(String query) {
		super();
		this.setQuery(query);
		new BackgroundWordTranslator().execute(query);
		new BackgroundPictureFinder().execute(query);
	}

}
