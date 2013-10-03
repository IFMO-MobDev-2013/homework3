package com.example.homework3;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: satori
 * Date: 10/3/13
 * Time: 7:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class SecondActivity extends Activity {
    private String word;
    private TextView translated_word;
    private GridLayout grid;
    private final ImageView[] imageView = new ImageView[10];
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondary);
        grid = (GridLayout)findViewById(R.id.grid);
        word = getIntent().getStringExtra("Word");
        int total = 10;
        int column = 3;
        int row = total / column;
        grid.setColumnCount(column);
        grid.setRowCount(row + 1);
        for(int i =0, c = 0, r = 0; i < total; i++, c++)
        {
            if(c == column)
            {
                c = 0;
                r++;
            }
            imageView[i] = new ImageView(this);
            //imageView[i].setImageResource(R.drawable.ic_launcher);
            GridLayout.LayoutParams param =new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            //param.rightMargin = 5;
            //param.topMargin = 5;
            param.setGravity(Gravity.CENTER);
            param.columnSpec = GridLayout.spec(c,1);
            param.rowSpec = GridLayout.spec(r,1);
            imageView[i].setLayoutParams (param);
            grid.addView(imageView[i]);
        }
     //for (int i = 0; i < 10; i++) {
//            imageView[i] = new ImageView(this);
//            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//            params.width= GridLayout.LayoutParams.WRAP_CONTENT;
//            params.height=GridLayout.LayoutParams.WRAP_CONTENT;
//            params.rowSpec=

//            imageView[i].setLayoutParams(new GridLayout.LayoutParams(GridLayout.));
//            grid.addView(imageView[i]);

//        }
        DownloadImagesTask downloadImagesTask = new DownloadImagesTask(imageView, word);
        downloadImagesTask.execute();




    }
}