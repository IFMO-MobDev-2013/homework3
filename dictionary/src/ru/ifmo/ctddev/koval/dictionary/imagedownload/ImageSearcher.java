package ru.ifmo.ctddev.koval.dictionary.imagedownload;

import java.util.List;

/**
 * Created by vladimirskipor on 10/3/13.
 */
public interface ImageSearcher {


    List<ResponseImage> search(String text);
    List<ResponseImage> search(String text, int heightFilter, int widthFilter, int imageLimit);


}
