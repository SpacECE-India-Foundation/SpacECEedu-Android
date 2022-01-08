package com.spacECE.spaceceedu.Utils;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
          System.out.println(resp);
          jsonObject= new  JSONObject(resp);
      } catch (IOException | JSONException e) {
          e.printStackTrace();
      }

      return jsonObject;

  }

  public static class DateFunc {

      public static Date StringToDate(String date) throws ParseException {
          return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
      }

      public static Date String1ToDate(String date) throws ParseException {
          return new SimpleDateFormat("MMM dd, yyyy").parse(date);
      }

      public static Date StringToTime(String date) throws ParseException {
          return new SimpleDateFormat("HH:mm:ss").parse(date);
      }

      public static String DateObjectToDate(Date date){
          return new SimpleDateFormat("MMM/dd").format(date);
      }

      public static String DateObjectToTime(Date date){
          return new SimpleDateFormat("HH:mm").format(date);
      }

      public static String DateObjectToDay(Date date){
          return new SimpleDateFormat("EEEE").format(date);
      }
  }

}
