package ru.georgeee.android.Silencio;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import ru.georgeee.android.Silencio.utility.GUI.PicturesAdapter;
import ru.georgeee.android.Silencio.utility.cacher.FileCacher;
import ru.georgeee.android.Silencio.utility.http.translate.TranslateResult;
import ru.georgeee.android.Silencio.utility.http.translate.yandex.YandexTranslateTask;

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
    private YandexTranslateTask translateTask;

    private String YANDEX_API_KEY, FLICKR_API_KEY;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        YANDEX_API_KEY = getString(R.string.yandex_translate_api_key);
        FLICKR_API_KEY = getString(R.string.flickr_api_key);

        init();
        addListners();

    }

    private final static int CACHE_BYTE_LIMIT = 50 * 1024 * 1024;

    private void init() {
        FileCacher.getInstance().init(this, CACHE_BYTE_LIMIT);

        searchedImages = (ListView) findViewById(R.id.images_result);
        translate = (TextView) findViewById(R.id.translate_result);
        searchButton = (Button) findViewById(R.id.search_button);
        searchField = (EditText) findViewById(R.id.search_field);

        imm = (InputMethodManager) getSystemService(getBaseContext().INPUT_METHOD_SERVICE);

        adapter = new PicturesAdapter(this, "");
        searchedImages.setAdapter(adapter);

    }

    private void addListners() {
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSearchString().length() == 0)
                    return;

                imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);

                if (translateTask != null) {
                    translateTask.cancel(true);
                }

                translate.setText(getSearchString());

                translateTask = new YandexTranslateTask(YANDEX_API_KEY, getSearchString(), "ru") {
                    @Override
                    protected void onPostExecute(TranslateResult translateResult) {
                        translate.setText(translateResult.getResult());
                    }
                };

                translateTask.execute();

                setPictures(getSearchString());
            }
        });

        searchedImages.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisible, int visibleCount, int totalCount) {
                if (getSearchString().length() == 0)
                    return;
                boolean loadMore = firstVisible + visibleCount >= totalCount - visibleCount /2;

                if (loadMore) {
                    adapter.loadMore(firstVisible + visibleCount * 2);
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
    }
}
