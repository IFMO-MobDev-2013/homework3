package com.example.YandexTranslit;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MyActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        setContentView(R.layout.main);
        ImageView image = (ImageView) findViewById(R.id.image);
        image.setImageBitmap(bitmap);

        Button send = (Button)  findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            EditText inputText = (EditText) findViewById(R.id.enter_text);
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivity.this, WorkActivity.class);
                intent.putExtra("key", inputText.getText().toString());
                startActivity(intent);
            }
        });
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
