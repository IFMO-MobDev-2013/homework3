package ru.georgeee.android.Silencio.utility.GUI;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import ru.georgeee.android.Silencio.utility.http.translate.TranslateResult;
import ru.georgeee.android.Silencio.utility.http.translate.yandex.YandexTranslateTask;

import java.util.concurrent.ExecutionException;

/**
 * Created with IntelliJ IDEA.
 * User: marsermd
 * Date: 02.10.13
 * Time: 5:27
 * To change this template use File | Settings | File Templates.
 */
public class TranslateSetter extends AsyncTask<Void, Void, String> {
    private TextView translate;
    private String searchRequest;
    private String found;
    public final static String API_KEY = "trnsl.1.1.20130929T093159Z.5d69fad7f86d7976.d05aeca7b3f8d233f46f93930066e4d87793cbb4";

    public TranslateSetter(Context context, TextView translate, String searchRequest) {
        this.translate = translate;
        this.searchRequest = searchRequest;
    }

    @Override
    protected void onPreExecute() {
        translate.setText(searchRequest);
    }

    @Override
    protected String doInBackground(Void... params) {
        YandexTranslateTask translateTask = new YandexTranslateTask(API_KEY, searchRequest, "ru");
        TranslateResult translateResult = null;

        try {
            translateResult = translateTask.executeOnHttpTaskExecutor().get();
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {}

        found = translateResult.getResult();

        return found;
    }

    @Override
    protected void onPostExecute(String result) {
        if (found != null)
            translate.setText(found);
    }
}
