package ru.ifmo.ctddev.koval.dictionary.imagedownload;

/**
* Created by vladimirskipor on 10/3/13.
*/
public class ImageSearcherException extends Exception {
    public ImageSearcherException() {
        super();
    }

    public ImageSearcherException(String detailMessage) {
        super(detailMessage);
    }

    public ImageSearcherException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ImageSearcherException(Throwable throwable) {
        super(throwable);
    }
}
