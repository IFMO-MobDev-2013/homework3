package ifmo.mobdev.Task3;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


public class Translator {

    private static final String DIRECTION = "https://translate.yandex.net/api/v1.5/tr/translate?key=trnsl.1.1.20130920T125424Z.3a9e8a0c7919077f.0298f380e28264cff2e9fa18ef9b5ab51ff08ec3&text=";
    private static final String TAIL = "&lang=ru";
    private static String answer = null;
    private static String query = null;

    public static String translate(String s) {
        String result = null;
        yandexTranslate(s);
        result = answer;

        // self-made parsing
        // instead of xml parser :)
        int beg = 0;
        int end = 0;
        if (result == null) {
            return null;
        }
        beg = result.indexOf("<text>");
        end = result.indexOf("</text>");
        result = result.substring(beg + 6, end);

        answer = null;
        query = null;
        return result;
    }

    private static void makeQuery(String s) {
        HttpsURLConnection connection = null;
        URL url = null;
        try {
            url = new URL(s);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setConnectTimeout(4000);
            connection.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));

            br = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            StringBuffer stb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                stb.append(line);
            }
            answer = stb.toString();
            if (br != null)
                br.close();
        } catch (MalformedURLException mfuexc) {
            return;
        } catch (IOException IOException) {
            return;
        }
    }

    private static synchronized void yandexTranslate(String s) {
        if (!validate(s)) {
            return;
        }

        query = DIRECTION + replaceSpaces(s) + TAIL;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                makeQuery(query);
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException intexc) {
            answer = null;
        }
    }

    private static boolean validate(String s) {
        char ch;
        for (int i = 0; i < s.length(); ++i) {
            ch = s.charAt(i);
            if (!(('a' <= ch && ch <= 'z') || ('A' <= ch && ch <= 'Z') || (ch == ' '))) {
                return false;
            }
        }
        return true;
    }

    private static String replaceSpaces(String s) {
        String result = "";
        result = s.replaceAll("\\s", "%20");
        return result;
    }
}
