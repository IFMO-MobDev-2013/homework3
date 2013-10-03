package com.ifmomd.slate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

public class InputActivity extends Activity implements View.OnClickListener, OnTranslationCompleteListener, OnTranslationFailedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_activity_layout);
        findViewsByIds();
        setListeners();
        loadAnimations();
    }

    ImageButton  btnTranslate;
    EditText     edtWordInput;
    LinearLayout lytProgressIndicator;

    Animation appearFromTop;

    private void findViewsByIds() {
        btnTranslate = (ImageButton) findViewById(R.id.btnTranslate);
        edtWordInput = (EditText) findViewById(R.id.edtWordInput);
        lytProgressIndicator = (LinearLayout) findViewById(R.id.lytProgressIndicator);
    }

    private void setListeners() {
        btnTranslate.setOnClickListener(this);
    }

    private void loadAnimations() {
        appearFromTop = AnimationUtils.loadAnimation(this, R.anim.appear_from_top);
    }

    @Override
    public void onClick(View v) {
        if (v == btnTranslate) {
            currentTranslator = new Translator(edtWordInput.getText().toString(), this, this);
            if (currentTranslator.isWorking()) {
                lytProgressIndicator.startAnimation(appearFromTop);
                lytProgressIndicator.setVisibility(View.VISIBLE);
            }
        }
    }

    Translator currentTranslator;

    @Override
    public void onTranslationComplete(Translator t, Translator.Translation[] result) {
        if (t == currentTranslator) {

            if (result.length > 0 && !(t instanceof FullTextTranslator && t.mOriginalWord.equals(result[0].translations[0].text))) {
                lytProgressIndicator.setVisibility(View.INVISIBLE);
                Intent i = new Intent(this, ResultsActivity.class);
                i.putExtra("translations", result);
                i.putExtra("original", t.getRequestedWord());
                startActivity(i);
            } else
            if (!(t instanceof FullTextTranslator))
            {
                currentTranslator = new FullTextTranslator(t.getRequestedWord(), this, this);
            }
            else
                onTranslationFaled(t);
        }
    }

    @Override
    public void onTranslationFaled(Translator t) {
        lytProgressIndicator.setVisibility(View.INVISIBLE);
        Toast.makeText(this,R.string.tstTranslationFailed_text, Toast.LENGTH_SHORT).show();
    }
}

