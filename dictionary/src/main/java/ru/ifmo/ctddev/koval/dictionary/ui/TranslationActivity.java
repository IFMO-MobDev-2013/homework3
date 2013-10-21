package ru.ifmo.ctddev.koval.dictionary.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import ru.ifmo.ctddev.koval.dictionary.R;
import ru.ifmo.ctddev.koval.dictionary.imagedownload.ImageSearcherAdapter;
import ru.ifmo.ctddev.koval.dictionary.translate.Translator;
import ru.ifmo.ctddev.koval.dictionary.translate.YandexTranslator;

/**
 * @author ndkoval
 */
public class TranslationActivity extends Activity {

    private TextView translationView;
    private String translatableWord;
    private Translator translator;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translation);

        //Get TextView object for translation
        translationView = (TextView) findViewById(R.id.translationView);

        //Get word to be translated from intent
        translatableWord = getIntent().getStringExtra("inputWord");

        //Set translator implementation
        translator = new YandexTranslator();

        setTranslation();
        setImages();
    }

    private void setTranslation() {
        String translation = translator.translate(translatableWord);
        translationView.setText(translation != null ? translation : "Error while translating...");
    }

    // Special for Vladimir Skipor. Please, add comments to this method
    // and add you Gallery or GridView(LinearView?) object to global variables.
    private void setImages() {
        ListView listView = (ListView) findViewById(R.id.image_list_view);
        ImageSearcherAdapter imageSearcherAdapter = new ImageSearcherAdapter(this);
        listView.setAdapter(imageSearcherAdapter);
        imageSearcherAdapter.addImagesByRequest(translatableWord);


    }
}
