package com.translate;
/*
скролл на переводе
размер поля ввода
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Translate extends Activity implements OnClickListener {
    String text;
    String[] langs;
    Button translateButton, clearButton;
    EditText translateField;
    ListView lang;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        translateField = (EditText) findViewById(R.id.translateField);

        langs = getResources().getStringArray(R.array.langs);
        lang = (ListView) findViewById(R.id.translationLang);
        lang.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(
                this, R.array.langs, android.R.layout.simple_list_item_single_choice);
        lang.setAdapter(list);
        lang.setItemChecked(0, true);

        translateButton = (Button) findViewById(R.id.translateButton);
        clearButton = (Button) findViewById(R.id.clearButton);
        translateButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.translateButton:
                text = translateField.getText().toString();
                Intent intent = new Intent(this, Translation.class);
                intent.putExtra("phrase", text);
                intent.putExtra("lang", lang.getCheckedItemPosition() == 0 ? "en-ru" : "ru-en");
                startActivity(intent);
                break;
            case R.id.clearButton:
                translateField.setText("");
                break;
            default:
                break;
        }
    }
}
