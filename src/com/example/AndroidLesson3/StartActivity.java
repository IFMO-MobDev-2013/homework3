package com.example.AndroidLesson3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.StringReader;
import java.util.HashMap;

public class StartActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    public static final String apiKey = "trnsl.1.1.20130925T205049Z.a92cd36d99706af9.b5a1aaf3c0f791ceb482029c486691897db34357";
    public static final String yandexUrl ="https://translate.yandex.net/api/v1.5/tr/translate";
    Translator translator = new Translator();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button submit = (Button) findViewById(R.id.button);
        final Intent intent = new Intent(this, TranslateActivity.class);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    CharSequence sequence = ((EditText) findViewById(R.id.editText)).getText();
                    String translate = getYandexTranslate(sequence);
                    intent.putExtra("translate",translate);
                } catch (Exception e) {
                    Log.e("lol", "lol", e);
                }
                startActivity(intent);
            }
        });
    }



    private String getYandexTranslate(CharSequence sequence) throws ParserConfigurationException, InterruptedException {
        HashMap<String,String>parameters = new HashMap<>();
        parameters.put("key",apiKey);
        parameters.put("text",sequence.toString());
        parameters.put("lang","ru");
        return translator.translate(sequence.toString(), yandexUrl,parameters);
    }

}
