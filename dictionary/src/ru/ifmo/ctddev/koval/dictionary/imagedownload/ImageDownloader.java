package ru.ifmo.ctddev.koval.dictionary.imagedownload;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Interface for downloading images by keyword.
 * Must use AsyncTask.
 *
 * @author ndkoval
 */
public interface ImageDownloader {

    /**
     * Search and download images by keyword.
     *
     * @param keyword    keyword that will be searched for images
     * @param imageCount count of images to be downloaded
     * @return List of downloaded images
     */
    public List<Bitmap> downloadImages(String keyword, int imageCount);
}
