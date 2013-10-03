package ru.itmo.homework3;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


/**
 * Created by Nick Smelik on 02.10.13.
 */
public class TranslateActivity extends Activity{


    private String inputWord;
    private String translateWord;

    private ListView listViewImages;
    private TextView txtSearchText;

    private ListViewImageAdapter adapter;
    private ArrayList<Object> listImages;
    private Activity activity;

    String strSearch = null;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.layout_translate);

        TextView textOnEnglish = (TextView)findViewById(R.id.source);
        TextView textOnRussian = (TextView)findViewById(R.id.translated);
        try {
            textOnEnglish.setText(getIntent().getExtras().getCharSequence("word"));


            inputWord = getIntent().getExtras().getCharSequence("word").toString();
            translateWord = getIntent().getExtras().getCharSequence("translate").toString();
        } catch (NullPointerException e) {

        }


        textOnEnglish.setText(inputWord);
        textOnRussian.setText(translateWord);

        activity=this;
        listViewImages = (ListView) findViewById(R.id.viewImages);
        txtSearchText = (TextView) findViewById(R.id.source);

        //strSearch = txtSearchText.getText().toString();
        strSearch=textOnEnglish.getText().toString();
        strSearch = Uri.encode(strSearch);

        //System.out.println("Search string => "+strSearch);
        new getImagesTask().execute();
    }

    public class getImagesTask extends AsyncTask<Void, Void, Void>
    {
        JSONObject json;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            dialog = ProgressDialog.show(TranslateActivity.this, "", "Please wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            URL url;
            try {
                url = new URL("https://ajax.googleapis.com/ajax/services/search/images?" +
                        "v=1.0&q="+strSearch+"&rsz=8");//&key=ABQIAAAADxhJjHRvoeM2WF3nxP5rCBRcGWwHZ9XQzXD3SWg04vbBlJ3EWxR0b0NVPhZ4xmhQVm3uUBvvRF-VAA&userip=192.168.0.172");

                URLConnection connection = url.openConnection();
                //connection.addRequestProperty("Referer", "http://technotalkative.com");

                String line;
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                System.out.println("Builder string => "+builder.toString());

                json = new JSONObject(builder.toString());
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if(dialog.isShowing())
            {
                dialog.dismiss();
            }

            try {
                JSONObject responseObject = json.getJSONObject("responseData");
                JSONArray resultArray = responseObject.getJSONArray("results");

                listImages = getImageList(resultArray);
                SetListViewAdapter(listImages);
                System.out.println("Result array length => "+resultArray.length());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public ArrayList<Object> getImageList(JSONArray resultArray)
    {
        ArrayList<Object> listImages = new ArrayList<Object>();
        GoogleImageBean bean;

        try
        {
            for(int i=0; i<resultArray.length(); i++)
            {
                JSONObject obj;
                obj = resultArray.getJSONObject(i);
                bean = new GoogleImageBean();

                bean.setTitle(obj.getString("title"));
                bean.setThumbUrl(obj.getString("tbUrl"));

                System.out.println("Thumb URL => "+obj.getString("tbUrl"));

                listImages.add(bean);

            }
            return listImages;
        }
        catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public void SetListViewAdapter(ArrayList<Object> images)
    {
        adapter = new ListViewImageAdapter(activity, images);
        listViewImages.setAdapter(adapter);
    }

    public void onBackPressed()
    {
        finish();
    }
}