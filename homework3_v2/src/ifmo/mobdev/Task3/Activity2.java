package ifmo.mobdev.Task3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;

public class Activity2 extends Activity {
    TextView txtView, txtView2;
    ImageButton prev, next;
    String enword = null, rusword = null;
    ImageView[] imgView;
    final int num = 10;
    int pos = 0;

    void layoutUpdate(int add) {
        imgView[pos * 2].setVisibility(View.INVISIBLE);
        imgView[pos * 2 + 1].setVisibility(View.INVISIBLE);
        pos += add;
        imgView[pos * 2].setVisibility(View.VISIBLE);
        imgView[pos * 2 + 1].setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.result);

        Display display =((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        rusword = "flower";
        enword = "цветочек";

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        enword = extras.getString("en");
        rusword = extras.getString("ru");

        txtView = (TextView) findViewById(R.id.textView);
        txtView2 = (TextView) findViewById(R.id.textView1);
        int ts;
        if ((enword.length() + rusword.length() + 3) > 40) {
            ts = Math.min(25, (width * 3) / (rusword.length() * 5));
            txtView2.setTextSize(ts);
            txtView.setText(enword);
            txtView2.setText(rusword);
        } else {
            ts = Math.min((width * 3) / ((3 + enword.length() + rusword.length()) * 5), 25);
            txtView.setText(enword + " - " + rusword);
            txtView2.setVisibility(View.INVISIBLE);
        }
        txtView.setTextSize(ts);

        imgView = new ImageView[num];
        imgView[0] = (ImageView) findViewById(R.id.imgView1);
        imgView[1] = (ImageView) findViewById(R.id.imgView2);
        imgView[2] = (ImageView) findViewById(R.id.imgView3);
        imgView[3] = (ImageView) findViewById(R.id.imgView4);
        imgView[4] = (ImageView) findViewById(R.id.imgView5);
        imgView[5] = (ImageView) findViewById(R.id.imgView6);
        imgView[6] = (ImageView) findViewById(R.id.imgView7);
        imgView[7] = (ImageView) findViewById(R.id.imgView8);
        imgView[8] = (ImageView) findViewById(R.id.imgView9);
        imgView[9] = (ImageView) findViewById(R.id.imgView10);

        for (int i = 0; i < num; i++) {
            imgView[i].setAdjustViewBounds(true);
            imgView[i].setMaxHeight((height / 2) - (int)(height * 0.1));
            imgView[i].setImageResource(R.drawable.no_image);
            if (i != 0 && i != 1) imgView[i].setVisibility(View.INVISIBLE);
        }

        GoogleImageSearcher gis = new GoogleImageSearcher(enword, imgView, num);
        gis.drawImages();

        next = (ImageButton) findViewById(R.id.imgButR);
        prev = (ImageButton) findViewById(R.id.imgButL);
        next.setAdjustViewBounds(true);
        next.setMaxHeight(height / 10);
        prev.setAdjustViewBounds(true);
        prev.setMaxHeight(height / 10);
        prev.setVisibility(View.INVISIBLE);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos == (num / 2 - 1)) next.setVisibility(View.VISIBLE);
                layoutUpdate(-1);
                if (pos == 0) prev.setVisibility(View.INVISIBLE);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos == 0) prev.setVisibility(View.VISIBLE);
                layoutUpdate(1);
                if (pos == (num / 2 - 1)) next.setVisibility(View.INVISIBLE);
            }
        });
    }
}

