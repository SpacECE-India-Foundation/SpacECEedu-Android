package com.spacECE.spaceceedu;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UsefulFunctions {

  public static JSONObject UsingGetAPI(String inputURL) {

            String result = "";
            HttpURLConnection urlConnection= null;

            try {
                URL url=new URL(inputURL);
                urlConnection= (HttpURLConnection) url.openConnection();
                Log.i("URL: ",url.toString());

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data!=-1){
                    char current = (char) data;
                    result+= current;
                    data= reader.read();
                }
            }catch (IOException e) {
                Log.i(" API: "," Data parsing error");
                e.printStackTrace();
            }
        try {
           //Log.i(" API: "," Creation"+result);
            return new JSONObject(result);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(" JSON Object: ","Malformed JSON");
            return null;
        }
    }
}
