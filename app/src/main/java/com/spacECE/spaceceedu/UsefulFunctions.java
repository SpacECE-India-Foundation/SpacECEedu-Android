package com.spacECE.spaceceedu;

import android.util.Log;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UsefulFunctions {

  public static JSONObject UsingGetAPI(String inputURL) {

      System.out.println(inputURL);

      JSONObject jsonObject = null;

      Response response;
      String resp;

      OkHttpClient client = new OkHttpClient();

      Request request = new Request.Builder()
              .url(inputURL)
              .build();

      Call call = client.newCall(request);

      try {
          response = call.execute();
          resp = response.body().string();
          //System.out.println(resp);
          jsonObject= new  JSONObject(resp);
      } catch (IOException | JSONException e) {
          e.printStackTrace();
      }

      return jsonObject;

    }
}
