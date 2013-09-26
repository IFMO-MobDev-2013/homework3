package com.ifmomd.slate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

interface OnTranslationCompleteListener {
    public void OnTranslationComplete(Translator.TranslationResult[] result);
}

public class Translator {
    private boolean mIsWorking = false;
    public boolean isWorking() {return mIsWorking;}

    private static final String API_KEY = "dict.1.1.20130923T213640Z.484ec64acb4393dd.2db0fb96a55b00d3399f9b5176ded60b886916cb";

    private enum TranslationDirection {
        Ru_En("ru-en"), En_Ru("en-ru");

        public final String requestArg;

        TranslationDirection(String requestArg) {this.requestArg = requestArg;}
    }

    public static class TranslationResult {

        private static TranslationResult[] TranslationsArrayFromJSON(JSONArray a) throws JSONException {
            TranslationResult[] results = new TranslationResult[a.length()];
            for (int i = 0; i < a.length(); i++)
                results[i] = TranslationResult.fromJSON(a.getJSONObject(i));
            return results;
        }

        private static TranslationResult fromJSON(JSONObject o) throws JSONException {
            TranslationResult result = new TranslationResult();
            if (o.has("text")) result.text = o.getString("text");
            if (o.has("pos")) result.position = o.getString("pos");
            if (o.has("tr")) {
                result.translations = TranslationsArrayFromJSON(o.getJSONArray("tr"));
            }
            if (o.has("syn")) {
                result.synonyms = TranslationsArrayFromJSON(o.getJSONArray("syn"));
            }
            if (o.has("mean")) {
                result.meanings = TranslationsArrayFromJSON(o.getJSONArray("mean"));
            }
            if (o.has("ex")) {
                result.examples = TranslationsArrayFromJSON(o.getJSONArray("ex"));
            }
            return result;
        }

        public String text, position;
        TranslationResult[] translations, synonyms, meanings, examples;
    }

    public Translator(String word, OnTranslationCompleteListener onTranslationCompleteListener) {
        mOnComplete = onTranslationCompleteListener;
        String url = makeURLString(word, determineDirection(word));
        new JSONGetter(new JSONGetter.ResultHandler() {
            @Override
            public void handleResult(JSONObject result) {
                handleResponse(result);
            }
        }).execute(url);
        mIsWorking = true;
    }

    private void handleResponse(JSONObject response) {
        try {
            if (response != null) {
                TranslationResult[] results = TranslationResult.TranslationsArrayFromJSON(response.getJSONArray("def"));
                mOnComplete.OnTranslationComplete(results);
                mIsWorking = false;
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private TranslationDirection determineDirection(String word) {
        for (char c : word.toLowerCase().toCharArray()) {
            if (c > 'a' && c < 'z') return TranslationDirection.En_Ru;
            if (c > 'а' && c < 'я') return TranslationDirection.Ru_En;
        }
        return TranslationDirection.values()[0];
    }

    private String makeURLString(String word, TranslationDirection direction) {
        try {
            return String.format("https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=%s&lang=%s&text=%s",
                                 API_KEY, direction.requestArg, URLEncoder.encode(word, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String                        mOriginalWord;
    private OnTranslationCompleteListener mOnComplete;


}