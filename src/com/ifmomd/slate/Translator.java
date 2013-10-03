package com.ifmomd.slate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

interface OnTranslationCompleteListener {
    public void onTranslationComplete(Translator t, Translator.Translation[] result);
}

interface OnTranslationFailedListener {
    public void onTranslationFaled(Translator t);
}

public class Translator {
    protected boolean mIsWorking = false;
    public boolean isWorking() {return mIsWorking;}

    protected String API_KEY = "dict.1.1.20130923T213640Z.484ec64acb4393dd.2db0fb96a55b00d3399f9b5176ded60b886916cb";

    public String getRequestedWord() {
        return requestedWord;
    }

    protected enum TranslationDirection {
        Ru_En("ru-en"), En_Ru("en-ru");

        public final String requestArg;

        TranslationDirection(String requestArg) {this.requestArg = requestArg;}
    }

    public static class Translation implements Serializable{

        private static Translation[] arrayFromJSON(JSONArray a) throws JSONException {
            Translation[] results = new Translation[a.length()];
            for (int i = 0; i < a.length(); i++)
                results[i] = Translation.fromJSON(a.getJSONObject(i));
            return results;
        }

        protected static Translation fromJSON(JSONObject o) throws JSONException {
            Translation result = new Translation();
            if (o.has("text")) result.text = o.getString("text");
            if (o.has("pos")) result.position = o.getString("pos");
            if (o.has("ts")) result.transcription = o.getString("ts");
            if (o.has("tr")) {
                result.translations = arrayFromJSON(o.getJSONArray("tr"));
            }
            if (o.has("syn")) {
                result.synonyms = arrayFromJSON(o.getJSONArray("syn"));
            }
            if (o.has("mean")) {
                 result.meanings = arrayFromJSON(o.getJSONArray("mean"));
            }
            if (o.has("ex")) {
                result.examples = arrayFromJSON(o.getJSONArray("ex"));
            }
            return result;
        }

        public String text, position, transcription;
        Translation[] translations, synonyms, meanings, examples;
    }

    private String requestedWord;

    public Translator(String word, OnTranslationCompleteListener onTranslationCompleteListener, OnTranslationFailedListener onTranslationFailedListener) {
        requestedWord = word;
        mOnComplete = onTranslationCompleteListener;
        mOnFail = onTranslationFailedListener;
        run(word);
    }

    protected void run(String word) {
        String url = makeURLString(word, determineDirection(word));
        new JSONGetter(new JSONGetter.ResultHandler() {
            @Override
            public void handleResult(JSONObject result) {
                handleResponse(result);
            }
        }).execute(url);
        mIsWorking = true;
    }

    protected void handleResponse(JSONObject response) {
        try {
            if (response != null) {
                Translation[] results = Translation.arrayFromJSON(response.getJSONArray("def"));
                mOnComplete.onTranslationComplete(this, results);
                mIsWorking = false;
            }
            else
                mOnFail.onTranslationFaled(this);
        } catch (JSONException ex) {
            ex.printStackTrace();
            mOnFail.onTranslationFaled(this);
        }
    }

    protected TranslationDirection determineDirection(String word) {
        char[] a = word.toLowerCase().toCharArray();
        for (char c : a) {
            if (c > 'a' && c < 'z') return TranslationDirection.En_Ru;
            if (c > 'а' && c < 'я') return TranslationDirection.Ru_En;
        }
        return TranslationDirection.values()[0];
    }

    protected String makeURLString(String word, TranslationDirection direction) {
        try {
            return String.format("https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=%s&lang=%s&text=%s",
                                 API_KEY, direction.requestArg, URLEncoder.encode(word, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected String                        mOriginalWord;
    protected OnTranslationCompleteListener mOnComplete;
    protected OnTranslationFailedListener   mOnFail;


}