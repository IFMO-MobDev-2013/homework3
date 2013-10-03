package ru.georgeee.android.Silencio.utility.GUI;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
    private ImageApiResult.Image leftImage;
    private ImageApiResult.Image rightImage;

    private SimpleCachingDownloadTask leftDownloadTask, rightDownloadTask;

    public TwoPicturesModel(ImageApiResult.Image leftImage, ImageApiResult.Image rightImage) {
        this.leftImage = leftImage;
        this.rightImage = rightImage;
    }

    public void setViews(ImageView leftView, ImageView rightView){
        this.leftView = leftView;
        this.rightView = rightView;

        setListeners();
    }

    private void setListeners(){
        leftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(leftView.getContext(), ImageActivity.class).putExtra("image", R.drawable.ic_launcher);
                leftView.getContext().startActivity(intent);
            }
        });

        rightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(leftView.getContext(), ImageActivity.class).putExtra("image", R.drawable.ic_launcher);
                rightView.getContext().startActivity(intent);
            }
        });
    }


    public void download() {
//        leftDownloadTask = new SimpleCachingDownloadTask(leftImage.getSmallImageUrl()) {
//            @Override
//            protected void onPostExecute(File file) {
//              //  leftView.setImageURI(Uri.fromFile(file));
//            }
//        };
//
//        rightDownloadTask = new SimpleCachingDownloadTask(rightImage.getSmallImageUrl()) {
//            @Override
//            protected void onPostExecute(File file) {
//               // rightView.setImageURI(Uri.fromFile(file));
//            }
//        };
//
//        leftDownloadTask.execute();
//        rightDownloadTask.execute();

    }

    public void cancel() {
        if(rightDownloadTask != null) {
            rightDownloadTask.cancel(true);
        }
        if(leftDownloadTask != null) {
            leftDownloadTask.cancel(true);
        }
    }


}
