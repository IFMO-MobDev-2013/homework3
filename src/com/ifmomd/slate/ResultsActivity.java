package com.ifmomd.slate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.URI;
import java.net.URLEncoder;

public class ResultsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_activity_layout);
        lytImages = ((LinearLayout) findViewById(R.id.lytImages));
        lytImages.setSaveEnabled(true);
        LinearLayout lyt = (LinearLayout) findViewById(R.id.lytResults);
        Intent i = getIntent();
        if (i.getExtras() != null && i.hasExtra("translations")) {
            Object[] translations = (Object[]) i.getExtras().get("translations");
            for (Object tr : translations)
                lyt.addView(makeTranslationView((Translator.Translation) tr));
        }
        if (i.getExtras() != null && i.hasExtra("original")) {
            originalWord = i.getStringExtra("original");
        }
        if (originalWord != null) {

            ImagesRenderer r = new ImagesRenderer(this, lytImages, URLEncoder.encode(originalWord));
            r.execute();
        }
    }

    LinearLayout lytImages;
    String       originalWord;

    private View makeTranslationView(Translator.Translation tr) {
        LinearLayout result = (LinearLayout) getLayoutInflater().inflate(R.layout.translation_entry_layout, null);
        if (result != null) {
            if (tr.text != null) ((TextView) result.findViewById(R.id.txtTranslationTitle)).setText(tr.text);
            ((TextView) result.findViewById(R.id.txtTranslationTitle)).setTextColor(Color.parseColor("#006633"));
            if (tr.position != null) ((TextView) result.findViewById(R.id.txtWordPosition)).setText("/"+tr.position+"/");
            if (tr.transcription != null)
                ((TextView) result.findViewById(R.id.txtTranscription)).setText("[" + tr.transcription + "]");
            getLayoutInflater().inflate(R.layout.translation_child_layout, result);
            LinearLayout lyt = (LinearLayout) result.findViewById(R.id.lytChild);
            for (Translator.Translation t : tr.translations) {
                View translationView = getLayoutInflater().inflate(R.layout.translation_entry_layout, null);
                if (translationView != null) {
                    ((TextView) translationView.findViewById(R.id.txtTranslationTitle)).setText(
                            t.text != null ? t.text : "");
                    ((TextView) translationView.findViewById(R.id.txtTranslationTitle)).setTypeface(Typeface.DEFAULT,Typeface.BOLD);
                    ((TextView) translationView.findViewById(R.id.txtWordPosition)).setText(
                            t.position != null ? "/"+t.position+"/" : "");
                    ((TextView) translationView.findViewById(R.id.txtTranscription)).setText(
                            t.transcription != null ? "[" + t.transcription + "]" : "");
                    if (t.synonyms != null && t.synonyms.length > 0) {
                        getLayoutInflater().inflate(R.layout.translation_synonims, (LinearLayout) translationView, true);
                        TextView tvSynonims = (TextView) translationView.findViewById(R.id.txtSynonyms);
                        String syns = "";
                        for (int i = 0; i < t.synonyms.length; i++)
                            syns += t.synonyms[i].text + (i < t.synonyms.length - 1 ? ", " : "");
                        tvSynonims.setText(syns);
                    }
                    if (t.meanings != null && t.meanings.length > 0) {
                        getLayoutInflater().inflate(R.layout.translation_meanings, (LinearLayout) translationView, true);
                        TextView tvMeanings = (TextView) translationView.findViewById(R.id.txtMeanings);
                        String means = "(";
                        for (int i = 0; i < t.meanings.length; i++)
                            means += t.meanings[i].text + (i < t.meanings.length - 1 ? ", " : "");
                        tvMeanings.setText(means + ")");
                    }
                    if (t.examples != null && t.examples.length > 0) {
                        getLayoutInflater().inflate(R.layout.translation_examples, (LinearLayout) translationView, true);
                        TextView tvExamples = (TextView) translationView.findViewById(R.id.txtExamples);
                        String exs = "";
                        for (int i = 0; i < t.examples.length; i++)
                            exs += t.examples[i].text + " — " + t.examples[i].translations[0].text + (
                                    i < t.examples.length - 1 ? "\n" : "");
                        tvExamples.setText(exs);
                    }
                }
                assert translationView != null;
                lyt.addView(translationView);
            }
        }
        return result;
    }
}