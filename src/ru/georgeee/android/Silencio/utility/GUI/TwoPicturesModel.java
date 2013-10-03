package ru.georgeee.android.Silencio.utility.GUI;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import ru.georgeee.android.Silencio.ImageActivity;
import ru.georgeee.android.Silencio.R;
import ru.georgeee.android.Silencio.SearchActivity;
import ru.georgeee.android.Silencio.utility.http.download.SimpleCachingDownloadTask;
import ru.georgeee.android.Silencio.utility.http.image.ImageApiResult;
import ru.georgeee.android.Silencio.utility.http.image.flickr.FlickrImageApiTask;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: marsermd
 * Date: 02.10.13
 * Time: 2:14
 * To change this template use File | Settings | File Templates.
 */
public class TwoPicturesModel {
    public ImageView leftView;
    public ImageView rightView;
    public TextView textView;
    private ImageApiResult.Image leftImage;
    private ImageApiResult.Image rightImage;
    private File leftFile, rightFile;

    private SimpleCachingDownloadTask leftDownloadTask, rightDownloadTask;

    public TwoPicturesModel(ImageApiResult.Image leftImage, ImageApiResult.Image rightImage) {
        this.leftImage = leftImage;
        this.rightImage = rightImage;
    }

    public void setViews(ImageView leftView, ImageView rightView){
        this.leftView = leftView;
        this.rightView = rightView;
        leftView.setImageResource(0);
        rightView.setImageResource(0);

        if (leftFile != null)
            leftView.setImageURI(Uri.fromFile(leftFile));

        if(rightFile != null)
            rightView.setImageURI(Uri.fromFile(rightFile));

        setListeners();
    }

    private void setListeners(){
        leftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(leftView.getContext(), ImageActivity.class).putExtra("ImageUrl", leftImage.getDefaultImageUrl());
                leftView.getContext().startActivity(intent);
            }
        });

        rightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(leftView.getContext(), ImageActivity.class).putExtra("ImageUrl", rightImage.getDefaultImageUrl());
                rightView.getContext().startActivity(intent);
            }
        });
    }


    public void download() {
        leftDownloadTask = new SimpleCachingDownloadTask(leftImage.getSmallImageUrl()) {
            @Override
            protected void onPostExecute(File file) {
                leftFile = file;
                if (leftView != null){
                    Log.e("LEFT", "end");
                    leftView.setImageURI(Uri.fromFile(leftFile));
                }
            }
        };

        rightDownloadTask = new SimpleCachingDownloadTask(rightImage.getSmallImageUrl()) {
            @Override
            protected void onPostExecute(File file) {
                rightFile = file;
                if (rightView != null) {
                    Log.e("RIGHT", "end");
                    rightView.setImageURI(Uri.fromFile(rightFile));
                }
            }
        };

        leftDownloadTask.executeOnHttpTaskExecutor();
        rightDownloadTask.executeOnHttpTaskExecutor();

    }

    public void cancel() {
        Log.e("wtf?", "cancel");
        if (leftView != null)
            leftView.setOnClickListener(null);
        if (rightView != null)
            rightView.setOnClickListener(null);

        leftView = null;
        rightView = null;
    }


}
