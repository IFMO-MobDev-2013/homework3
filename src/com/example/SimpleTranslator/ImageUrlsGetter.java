package com.example.SimpleTranslator;

import java.io.InputStream;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageUrlsGetter {
	private ImageUrlsGetter() {}
	private static final String URL = "http://images.yandex.ru/yandsearch?text=";
	private static final String MODE = "&isize=eq&ih=400&iw=400&nl=2&lr=2&uinfo=sw-1894-sh-919-fw-1669-fh-598-pd-1";
	private static final String PATTERN = "<img class=\"[^\"]*preview[^\"]*\" (?:alt=\".*?\" )*src=\"(.*?)\"";
	private static final int TIMEOUTS = 10000;

	private static List<String> parseImages(String string) {
		List<String> result = new ArrayList<String>();

		Matcher matcher = Pattern.compile(PATTERN).matcher(string);
		while (matcher.find()) {
			result.add(matcher.group(1));
		}

		return result;
	}
	private static String readStream(InputStream in) {
		StringBuilder result = new StringBuilder();
		Scanner scanner = new Scanner(in);
		while (scanner.hasNext()) {
			result.append(scanner.nextLine());
		}

		return result.toString();
	}

	public static List<String> getImagesUrls(String word) {
	try {
		String requestUrl = URL + URLEncoder.encode(word, "UTF-8") + MODE;
		URLConnection connection = new URL(requestUrl).openConnection();
		connection.setConnectTimeout(TIMEOUTS);
		connection.setReadTimeout(TIMEOUTS);
		return parseImages(readStream(connection.getInputStream()));
	} catch (Exception e) {
		e.printStackTrace();
	}
		return Collections.emptyList();
	}
}
