package ifmo.mobdev.Task3;

import android.os.AsyncTask;
import android.util.Log;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class Translator {

    private static final String DIRECTION = "https://translate.yandex.net/api/v1.5/tr/translate?key=trnsl.1.1.20130920T125424Z.3a9e8a0c7919077f.0298f380e28264cff2e9fa18ef9b5ab51ff08ec3&text=";
    private static final String TAIL = "&lang=ru";

    public static void translate(String s, AsyncCallback<? super String, ? super IOException> callback) {
        if (!validate(s)) {
            throw new IllegalArgumentException("Illegal string format passed");
        }

        try {
            new TranslationTask(callback).execute(DIRECTION + URLEncoder.encode(s, "UTF-8") + TAIL);
        } catch (UnsupportedEncodingException e) {
           throw new RuntimeException("Couldn't find UTF-8...", e);
        }
    }

    private static boolean validate(String s) {
        for (int i = 0; i < s.length(); ++i) {
            char ch = s.charAt(i);
            if (!(('a' <= ch && ch <= 'z') || ('A' <= ch && ch <= 'Z') || (ch == ' '))) {
                return false;
            }
        }
        return true;
    }

    private static class TranslationTask extends AsyncTask<String, Void, String> {

        private AsyncCallback<? super String, ? super IOException> callback;
        private IOException exception;

        private TranslationTask(AsyncCallback<? super String, ? super IOException> callback) {
            this.callback = callback;
        }

        @Override
        protected String doInBackground(String... strings) {
            InputStreamReader reader = null;
            try {
                URL url = new URL(strings[0]);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setConnectTimeout(4000);
                connection.connect();
                reader = new InputStreamReader(connection.getInputStream());
                StringBuilder builder = new StringBuilder();
                char[] buffer = new char[8192]; // 8KB
                for (int read; (read = reader.read(buffer)) > 0; ) {
                    builder.append(buffer, 0, read);
                }
                String result = builder.toString();

                // self-made parsing
                // instead of xml parser :)
                int beg = result.indexOf("<text>");
                int end = result.indexOf("</text>");


                if (beg == -1 || end == -1) {
                    throw new IOException("something goes really bad!");
                }

                return result.substring(beg + 6, end);
            } catch (IOException e) {
                exception = e;
                return null;
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        // ignored
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                callback.onSuccess(s);
            } else {
                callback.onFailure(exception);
            }
        }
    }


//    private static String answer = null;
//    private static String query = null;
//
//    public static String translate(String s) {
//        String result = null;
//        yandexTranslate(s);
//        result = answer;
//
//        // self-made parsing
//        // instead of xml parser :)
//        int beg = 0;
//        int end = 0;
//        if (result == null) {
//            return null;
//        }
//        beg = result.indexOf("<text>");
//        end = result.indexOf("</text>");
//        result = result.substring(beg + 6, end);
//
//        answer = null;
//        query = null;
//        return result;
//    }
//
//    private static void makeQuery(String s) {
//        HttpsURLConnection connection = null;
//        URL url = null;
//        try {
//            url = new URL(s);
//            connection = (HttpsURLConnection) url.openConnection();
//            connection.setConnectTimeout(4000);
//            connection.connect();
//            BufferedReader br = new BufferedReader(new InputStreamReader(
//                    connection.getInputStream()));
//
//            br = new BufferedReader(new InputStreamReader(
//                    connection.getInputStream()));
//            String line;
//            StringBuffer stb = new StringBuffer();
//            while ((line = br.readLine()) != null) {
//                stb.append(line);
//            }
//            answer = stb.toString();
//            if (br != null)
//                br.close();
//        } catch (MalformedURLException mfuexc) {
//            return;
//        } catch (IOException IOException) {
//            return;
//        }
//    }
//
//    private static synchronized void yandexTranslate(String s) {
//        if (!validate(s)) {
//            return;
//        }
//
//        query = DIRECTION + replaceSpaces(s) + TAIL;
//        Thread thread = new Thread(new Runnable() {
//            public void run() {
//                makeQuery(query);
//            }
//        });
//
//        thread.start();
//        try {
//            thread.join();
//        } catch (InterruptedException intexc) {
//            answer = null;
//        }
//    }
//
//    private static boolean validate(String s) {
//        char ch;
//        for (int i = 0; i < s.length(); ++i) {
//            ch = s.charAt(i);
//            if (!(('a' <= ch && ch <= 'z') || ('A' <= ch && ch <= 'Z') || (ch == ' '))) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private static String replaceSpaces(String s) {
//        String result = "";
//        result = s.replaceAll("\\s", "%20");
//        return result;
//    }
}
