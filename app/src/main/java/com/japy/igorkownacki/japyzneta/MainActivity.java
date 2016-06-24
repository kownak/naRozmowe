package com.japy.igorkownacki.japyzneta;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button_odswiez)
    protected Button odswiez;

    @BindView(R.id.text_imie)
    protected TextView imie;

    @BindView(R.id.text_plec)
    protected TextView plec;

    @BindView(R.id.image_japa)
    protected ImageView japa;

    @OnClick(R.id.button_odswiez)
    public void onButtonClick(){

       // Log.d("Main","Button click");
        String stringUrl="http://api.randomuser.me";

        BackgroudOperation bgOperation = new BackgroudOperation();
        bgOperation.execute(stringUrl);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Log.d("Main","onCreate");

        //onButtonClick();
    }

    public  class BackgroudOperation extends AsyncTask<String, String, Void>{
        private String imageUrl;
        String tmpImie="";
        String tmpPlec="";
        Bitmap bitmap;

        @Override
        protected Void doInBackground(String... params) {
            //Log.d("BackgroundOperation","doInBackground");
            BufferedReader br;
            URL url;
            URLConnection connection;
            JSONObject jsonObject;



            try {
                url= new URL(params[0]);
                connection = url.openConnection();
                connection.setDoInput(true);

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String string;

                while((string = br.readLine())!=null){
                    stringBuilder.append(string);
                    stringBuilder.append(System.getProperty("string.separator"));
                }
                br.close();

                jsonObject=new JSONObject(stringBuilder.toString());
                JSONArray jsonArray = jsonObject.optJSONArray("results");

                for (int i=0;i<jsonArray.length();i++){
                    JSONObject wartosc = jsonArray.getJSONObject(i);
                    tmpPlec = wartosc.getString("gender");
                    tmpImie=wartosc.getJSONObject("name").getString("title")+" "+
                            wartosc.getJSONObject("name").getString("first")+" "+
                            wartosc.getJSONObject("name").getString("last");
                    imageUrl=wartosc.getJSONObject("picture").getString("large");
                }

                url = new URL(imageUrl);
                connection = url.openConnection();
                connection.setDoInput(true);

                InputStream input = connection.getInputStream();
                bitmap= BitmapFactory.decodeStream(input);
                input.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            plec.setText(tmpPlec);
            imie.setText(tmpImie);
            japa.setImageBitmap(bitmap);


        }
    }

}
