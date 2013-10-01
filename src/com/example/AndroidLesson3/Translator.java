package com.example.AndroidLesson3;

import android.util.Log;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Xottab
 * Date: 01.10.13
 * Time: 18:07
 * To change this template use File | Settings | File Templates.
 */
class Translator {
    protected String translationXml = "";
    protected String address;

    class Task implements Runnable {
        protected String getTranslate(String address) throws IOException {
            StringBuilder result = new StringBuilder();

            URL url = new URL(address.replace(" ","%20"));
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(new BufferedInputStream(connect.getInputStream())));
                String temp;
                while ((temp = in.readLine()) != null) {
                    result.append(temp);
                }
                return result.toString();
            } catch (Exception e) {
                return null;
            } finally {
                connect.disconnect();
            }
        }

        @Override
        public void run() {
            try {
                translationXml = getTranslate(address);
            } catch (IOException e) {
               Log.e("donkey","donkey",e);  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }


    public String translate(String s, String url, HashMap<String, String> parameters) throws ParserConfigurationException, InterruptedException {
        Thread thread = new Thread(new Task());
        StringBuilder address = new StringBuilder(url);
        address.append("?");
        for (Map.Entry<String, String> e : parameters.entrySet()) {
            address.append(e.getKey() + "=" + e.getValue() + "&");
        }
        this.address=address.toString();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        thread.start();
        DocumentBuilder db = factory.newDocumentBuilder();
        thread.join();
        Document document = XmlFromString(translationXml);
        Log.e("helol", translationXml);
        Log.e("helol", document.getElementsByTagName("text").item(0).getTextContent());
        return document.getElementsByTagName("text").item(0).getTextContent();
    }

    public Document XmlFromString(String v) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(v));
            doc = db.parse(is);
            return doc;
        } catch (Exception e) {
            System.out.println("Wrong XML file structure: " + e.getMessage());
            return null;
        }
    }


}