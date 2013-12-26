package com.example.YandexTranslit;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class SendingText extends Thread {
    private String text;
    private String page;
    private String flag;

    public SendingText(String text, String flag) {
        this.text = text;
        this.flag = flag;
    }

    @Override
    public void run() {
        page = getStringWithTranslate();
    }

    public String getPage() {
        return page;
    }

    private String getStringWithTranslate() {
        String yandexRequest = "https://translate.yandex.net/api/v1.5/tr/translate?key=trnsl.1.1.20130923T160324Z.6bd4de234e5cc570.cca4f0853220d80a9a2597e86357cf350000a5a8&text=" + text;
        if (flag == "ru")
            yandexRequest += "&lang=en-ru&format=plain";
        else
            yandexRequest += "&lang=ru-en&format=plain";
        try {
            InputStream inputStream = new URL(yandexRequest).openStream();
            return new Scanner(inputStream).useDelimiter("\\A").next();
        } catch(Exception e) {
            return "Sorry. Translation error :(";
        }
    }
}
