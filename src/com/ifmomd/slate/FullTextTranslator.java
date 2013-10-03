package com.ifmomd.slate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Sergey on 10/3/13.
 */
public class FullTextTranslator extends Translator {

    public FullTextTranslator(String phrase, OnTranslationCompleteListener onTranslationCompleteListener, OnTranslationFailedListener onTranslationFailedListener) {
        super(phrase, onTranslationCompleteListener, onTranslationFailedListener);
        mOriginalWord = phrase;
    }

    @Override
    protected void run(String word) {
        API_KEY = "trnsl.1.1.20131003T161157Z.7dadb380384bbab6.321816b6e72f632f1a1f5bbab86b2799ed0d63d6";
        String url = makeURLString(word, determineDirection(word));
        new JSONGetter(new JSONGetter.ResultHandler() {
            @Override
            public void handleResult(JSONObject result) {
                handleResponse(result);
            }
        }).execute(url);
        mIsWorking = true;
    }

    @Override
    protected String makeURLString(String word, Translator.TranslationDirection direction) {
        try {
            return String.format("https://translate.yandex.net/api/v1.5/tr.json/translate?key=%s&lang=%s&text=%s",
                                 API_KEY, direction.requestArg, URLEncoder.encode(word, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void handleResponse(JSONObject response) {
        try {
            if (response != null) {
                if (response.has("text")) {
                    JSONArray a = response.getJSONArray("text");
                    Translation[] results = new Translation[a.length()];
                    for (int i = 0; i<a.length(); i++) {
                        results[i] = new Translation();
                        results[i].text = mOriginalWord;
                        results[i].translations = new Translation[1];
                        results[i].translations[0] = new Translation();
                        results[i].translations[0].text = a.getString(i);
                    }
                    mOnComplete.onTranslationComplete(this, results);
                    mIsWorking = false;
                }
            }
            else
                mOnFail.onTranslationFaled(this);
        } catch (JSONException ex) {
            mOnFail.onTranslationFaled(this);
            ex.printStackTrace();
        }
    }
}

