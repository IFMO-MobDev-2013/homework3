package ru.georgeee.android.Silencio;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import ru.georgeee.android.Silencio.utility.GUI.PicturesAdapter;
import ru.georgeee.android.Silencio.utility.GUI.TranslateSetter;
import ru.georgeee.android.Silencio.utility.GUI.TwoPicturesModel;
import ru.georgeee.android.Silencio.utility.http.translate.TranslateResult;
import ru.georgeee.android.Silencio.utility.http.translate.yandex.YandexTranslateTask;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private ListView searchedImages;
    private TextView translate;
    private Button searchButton;
    private EditText searchField;
    private InputMethodManager imm;
    private PicturesAdapter adapter;
    private TranslateSetter translateSetter = new TranslateSetter(null, null, "");


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        init();
        addListners();

    }

    private void init() {
        searchedImages = (ListView)findViewById(R.id.images_result);
        translate = (TextView) findViewById(R.id.translate_result);
        searchButton = (Button) findViewById(R.id.search_button);
        searchField = (EditText) findViewById(R.id.search_field);
        imm = (InputMethodManager)getSystemService(getBaseContext().INPUT_METHOD_SERVICE);

        List<TwoPicturesModel> list = new LinkedList<TwoPicturesModel>();

        //this should be removed
        {
            list.add(new TwoPicturesModel(0, ""));
        }

        adapter = new PicturesAdapter(this, "");

        searchedImages.setAdapter(adapter);

    }

    private void addListners() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getSearchString().length() == 0)
                    return;
                setPictures(getSearchString());

                translateSetter = new TranslateSetter(getBaseContext(), translate, getSearchString());
                translateSetter.execute();

                imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
            }
        });

        searchedImages.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisible, int visibleCount, int totalCount) {
                boolean loadMore = firstVisible + visibleCount >= totalCount;

                if(loadMore) {
                    adapter.addItems(visibleCount * 3);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public String getSearchString() {
        return searchField.getText().toString();
    }


    private void setPictures(String searchRequest) {
        adapter.init(searchRequest);
        adapter.notifyDataSetChanged();
    }
}
