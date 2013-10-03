package com.example.task3;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MyGalleryActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /*
        WebImageView view = (WebImageView) findViewById(R.id.imageView);

        // обрабатываем щелчок на элементе галереи
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((WebImageView)v).setImageUrl("https://2.gravatar.com/avatar/34556985f95a19824059aa75c8c8d851?d=https%3A%2F%2Fidenticons.github.com%2F6e9730ad8e4e7af610e61378697b0a93.png&s=420");
            }
        }); */
    }
}
