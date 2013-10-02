package ru.georgeee.android.Silencio.utility.GUI;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import ru.georgeee.android.Silencio.R;

/**
 * Created with IntelliJ IDEA.
 * User: marsermd
 * Date: 02.10.13
 * Time: 2:14
 * To change this template use File | Settings | File Templates.
 */
public class TwoPicturesModel {
    private Bitmap leftImage;
    private Bitmap rightImage;

    public TwoPicturesModel(int id, String search){
        //here should be used georgees part
    }

    public void setLeftImage(Bitmap leftImage){
        this.leftImage = leftImage;
    }

    public void setRightImage(Bitmap rightImage){
        this.rightImage = rightImage;
    }

    public Bitmap getLeftImage(){
        return leftImage;
    }

    public Bitmap getRightImage(){
        return rightImage;
    }
}
