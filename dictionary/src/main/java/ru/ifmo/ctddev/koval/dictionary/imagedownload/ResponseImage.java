package ru.ifmo.ctddev.koval.dictionary.imagedownload;

/**
* Created by vladimirskipor on 10/3/13.
*/
public class ResponseImage {
    private final String imageURL;
    private final int width;
    private final int height;
    private final String title;

    public ResponseImage(String imageURL, int width, int height, String title) {
        this.imageURL = imageURL;
        this.width = width;
        this.height = height;
        this.title = title;
    }


    public String getImageURL() {
        return imageURL;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    public String getTitle() {
        return title;
    }
}
