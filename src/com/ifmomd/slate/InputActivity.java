package com.ifmomd.slate;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

public class InputActivity extends Activity implements View.OnClickListener, OnTranslationCompleteListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_activity_layout);
        findViewsByIds();
        setListeners();
        loadAnimations();
    }

    ImageButton btnTranslate;
    EditText    edtWordInput;
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
            Translator t = new Translator(edtWordInput.getText().toString(), this);
            if (t.isWorking()) {
                lytProgressIndicator.startAnimation(appearFromTop);
                lytProgressIndicator.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void OnTranslationComplete(Translator.TranslationResult[] result) {
        lytProgressIndicator.setVisibility(View.INVISIBLE);
        if (result.length > 0)
            Toast.makeText(this, result[0].translations[0].text, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, getString(R.string.tstTranslationFailed_text), Toast.LENGTH_SHORT).show();
    }
}
