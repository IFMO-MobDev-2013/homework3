package ru.georgeee.android.Silencio;

import android.test.InstrumentationTestCase;
import android.util.Log;
import org.json.JSONException;
import ru.georgeee.android.Silencio.utility.http.translate.TranslateResult;
import ru.georgeee.android.Silencio.utility.http.translate.TranslateTask;
import ru.georgeee.android.Silencio.utility.http.translate.yandex.YandexTranslateTask;

import java.io.IOException;
import java.util.Arrays;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class ru.georgeee.android.Silencio.TranslateTest \
 * ru.georgeee.android.Silencio.tests/android.test.InstrumentationTestRunner
 */
public class TranslateTest extends InstrumentationTestCase {

    public final static String API_KEY = "trnsl.1.1.20130929T093159Z.5d69fad7f86d7976.d05aeca7b3f8d233f46f93930066e4d87793cbb4";

    public void testYandexTranslateTask() throws Exception {
        YandexTranslateTask enRuTranslateTask = new MyYandexTranslateTask("hippie", "ru", "en");
        YandexTranslateTask enRu2TranslateTask = new MyYandexTranslateTask("people", "ru");
        YandexTranslateTask ruEnTranslateTask = new MyYandexTranslateTask("цветочки", "en", "ru");
        YandexTranslateTask ruEn2TranslateTask = new MyYandexTranslateTask("фенечки", "en");


        assertTranslateResult(enRuTranslateTask, "хиппи", "en");
        assertTranslateResult(enRu2TranslateTask, "люди", "en");
        assertTranslateResult(ruEnTranslateTask, "flowers", "ru");
        assertTranslateResult(ruEn2TranslateTask, "baubles", "ru");

    }

    void assertTranslateResult(TranslateTask translateTask, String translation, String detectedLang) throws Exception{
        TranslateResult translateResult = translateTask.executeOnHttpTaskExecutor().get();
        assertEquals(translateResult.getResult(), translation);
        assertEquals(translateResult.getLangDetected(), detectedLang);
    }

    class MyYandexTranslateTask extends YandexTranslateTask{
        MyYandexTranslateTask(String srcText, String toLanguage, String fromLanguage) {
            super(API_KEY, srcText, toLanguage, fromLanguage);
        }

        MyYandexTranslateTask(String srcText, String toLanguage) {
            super(API_KEY, srcText, toLanguage);
        }

        //Unable to  check - it's being executed from non-UI thread
//            @Override
//            protected void onPostExecute(TranslateResult translateResult) {
//            }

        @Override
        protected void handleHttpIOException(IOException ex) {
            throw new RuntimeException(ex);
        }

        @Override
        protected void handleJSONException(JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

}
