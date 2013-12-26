package com.example.YandexTranslit;

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestSender extends Thread {
    private String request;
    private String[] links;

    public RequestSender(String request) {
        this.request = request;
    }

    public String sendRequest(String word) {
        String request;

        request = "https://api.datamarket.azure.com/Bing/Search/Image?Query=%27" + word + "%27&ImageFilters=%27Size%3AMedium%27&$top=20&$format=ATOM";
        String key = "v9r0LvmIDZSbVq9EB+DXBdvJmue+q1X0G41BFQBvNcU";

        byte[] accountKeyBytes = Base64.encodeBase64((key + ":" + key).getBytes());
        String accountKeyEnc = new String(accountKeyBytes);

        URL url;
        HttpURLConnection connect;
        BufferedReader stream;
        String result = "";
        String buffer;

        try {
            url = new URL(request);
            connect = (HttpURLConnection) url.openConnection();
            connect.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
            stream = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            while ((buffer = stream.readLine()) != null) {
                result += buffer;
            }
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String[] getLinks(String s) {

        String[] answer = new String[10];
        int count = 0;

        for (int i = 1; i < s.length(); i++) {
            if ((s.charAt(i-1) == '.') && (s.charAt(i) == 'j') && (s.charAt(i+1) == 'p') && (s.charAt(i+2) == 'g')  && (s.charAt(i+3) == '<')) {
                String tmp = "";
                for (int j = i; s.charAt(j) != '>'; j--) {
                    tmp = s.charAt(j) + tmp;
                }
                tmp += "pg";
                answer[count] = tmp;
                count++;
                if (count == 10) {
                    break;
                }
            }
            if ((s.charAt(i-1) == '.') && (s.charAt(i) == 'p') && (s.charAt(i+1) == 'n') && (s.charAt(i+2) == 'g')  && (s.charAt(i+3) == '<')) {
                String tmp = "";
                for (int j = i; s.charAt(j) != '>'; j--) {
                    tmp = s.charAt(j) + tmp;

                }
                tmp += "ng";
                answer[count] = tmp;
                count++;
                if (count == 10) {
                    break;
                }
            }

        }

        return answer;
    }

    public String[] getLinks() {
        return links;
    }

    @Override
    public void run() {
        links = getLinks(sendRequest(request));
    }
}
