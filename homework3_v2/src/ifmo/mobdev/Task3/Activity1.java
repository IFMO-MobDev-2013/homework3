package ifmo.mobdev.Task3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class Activity1 extends Activity {

    ImageButton translateButton;
    String answer = null;
    String request = null;
    EditText editText;
    ImageView imgViewTS;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main);

        Display display =((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();

        editText = (EditText) findViewById(R.id.editText);
        editText.setMinWidth((int)(width * 0.8));

        translateButton = (ImageButton)findViewById(R.id.translate);
        translateButton.setAdjustViewBounds(true);
        translateButton.setMaxWidth((int)(width * 0.5));

        imgViewTS = (ImageView) findViewById(R.id.imgViewTS);
        imgViewTS.setAdjustViewBounds(true);
        imgViewTS.setMaxWidth((int)(width * 0.8));

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.title_anim);
        imgViewTS.startAnimation(animation);

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translate();
            }
        });

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    translate();
                }
                return false;
            }
        }) ;
    }

    private void translate() {
        final String request = editText.getText().toString();

        Translator.translate(request, new AsyncCallback<String, IOException>() {
            @Override
            public void onSuccess(String answer) {
                Intent intent = new Intent(Activity1.this, Activity2.class);
                intent.putExtra("en", request);
                intent.putExtra("ru", answer);

                editText.setText(answer);

                startActivity(intent);
            }

            @Override
            public void onFailure(IOException cause) {
                Toast.makeText(Activity1.this, "something goes wrong", 1000).show();
                editText.setText("");
            }
        });
    }
}






