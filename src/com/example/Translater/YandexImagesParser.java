package com.example.Translater;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YandexImagesParser {
    public List<String> parse(String html) {
        Pattern pattern = Pattern.compile("href=\"([^\"]*?)\" title=\"\\d");
        Matcher matcher = pattern.matcher(html);
        List<String> res = new ArrayList<String>();
        while(matcher.find()) {
            res.add(matcher.group(1));
        }
        return res;  //To change body of created methods use File | Settings | File Templates.
    }
}
