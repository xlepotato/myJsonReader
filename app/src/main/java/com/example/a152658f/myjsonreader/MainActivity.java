package com.example.a152658f.myjsonreader;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView myTV, mySymbolTV, myPriceTV, myChangeTV;

    private static String BASE_URL = "http://keeliren.appspot.com/jsondata?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myTV = (TextView)findViewById(R.id.myTV);
        mySymbolTV = (TextView)findViewById(R.id.mySymbolTV);
        myPriceTV = (TextView)findViewById(R.id.myPriceTV);
        myChangeTV = (TextView)findViewById(R.id.myChangeTV);

        getJSONTASK myJsonTask = new getJSONTASK();
        myJsonTask.execute();
    }

    public class getJSONTASK extends AsyncTask<Void,String,String>{
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            myTV.setText(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            mySymbolTV.setText(values[1]);
            myPriceTV.setText(values[2]);
            myChangeTV.setText(values[3]);
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONArray jArr;
            JSONObject jObj;

            String response = getWeatherData("ABC");
            try{
                jArr = new JSONArray(response);
                jObj = jArr.getJSONObject(0);
                String Symbol = "Symbol " + jObj.getString("symbol");
                String price = "Price " + jObj.getString("price");
                String change = "Change " + jObj.getString("change");

                publishProgress(response, Symbol, price, change);
            }catch(JSONException e){
                e.printStackTrace();
            }
            return response;
        }
        public String getWeatherData(String location){
            HttpURLConnection con = null;
            InputStream is = null;
            try {
                con = (HttpURLConnection) (new URL(BASE_URL + location)).openConnection();
                con.setRequestMethod("GET");
                con.connect();

                //Let's read the response
                StringBuffer buffer = new StringBuffer();
                is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = null;
                while((line = br.readLine()) != null)
                    buffer.append(line);
                is.close();
                con.disconnect();
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                try { is.close(); } catch(Throwable t){}
                try { con.disconnect();} catch(Throwable t){}
            }
            return null;
        }
    }
}
