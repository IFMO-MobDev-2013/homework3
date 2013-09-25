package com.ifmomd.slate;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

interface OnTranslationCompleteListener {
    public void OnTranslationComplete(Translator.TranslationResult[] result);
}

public class Translator {
    private static final String API_KEY = "dict.1.1.20130923T213640Z.484ec64acb4393dd.2db0fb96a55b00d3399f9b5176ded60b886916cb";

    private enum TranslationDirection {
        Ru_En("ru-en"), En_Ru("en-ru");

        public final String requestArg;

        TranslationDirection(String requestArg) {this.requestArg = requestArg;}
    }

    public static class TranslationResult {
        private static TranslationResult fromJSON(JSONObject o) throws JSONException{
            TranslationResult result = new TranslationResult();
            if (o.has("text")) result.text = o.getString("text");
            if (o.has("pos")) result.position = o.getString("pos");
            if (o.has("tr")) {
                JSONArray trs = o.getJSONArray("tr");
                result.translations = new TranslationResult[trs.length()];
                for (int i = 0; i<trs.length(); i++)
                    result.translations[i] = TranslationResult.fromJSON(trs.getJSONObject(i));
            }
            if (o.has("syn")) {
                JSONArray syns = o.getJSONArray("syn");
                result.synonyms = new TranslationResult[syns.length()];
                for (int i = 0; i<syns.length(); i++)
                    result.synonyms[i] = TranslationResult.fromJSON(syns.getJSONObject(i));
            }
            if (o.has("mean")) {
                JSONArray means = o.getJSONArray("mean");
                result.meanings = new TranslationResult[means.length()];
                for (int i = 0; i<means.length(); i++)
                    result.meanings[i] = TranslationResult.fromJSON(means.getJSONObject(i));
            }
            if (o.has("ex")) {
                JSONArray exs = o.getJSONArray("ex");
                result.examples = new TranslationResult[exs.length()];
                for (int i = 0; i<exs.length(); i++)
                    result.examples[i] = TranslationResult.fromJSON(exs.getJSONObject(i));
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
        }).doInBackground(url);
    }

    private void handleResponse(JSONObject response) {
        try {
            JSONArray defs = response.getJSONArray("def");
            TranslationResult[] results = new TranslationResult[defs.length()];
            for (int i = 0; i<defs.length(); i++) {
                results[i] = TranslationResult.fromJSON(defs.getJSONObject(i));
            }
            mOnComplete.OnTranslationComplete(results);
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
        return String.format("https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=%s&lang=%s&text=%s",
                             API_KEY, direction.requestArg, word);
    }

    private String                        mOriginalWord;
    private OnTranslationCompleteListener mOnComplete;


}