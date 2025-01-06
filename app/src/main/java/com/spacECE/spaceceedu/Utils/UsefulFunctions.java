package com.spacECE.spaceceedu.Utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UsefulFunctions {

    public static JSONObject UsingGetAPI(String url) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            URL requestUrl = new URL(url);
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            Log.d("API Response Code", String.valueOf(responseCode));

            if (responseCode != HttpURLConnection.HTTP_OK) {
                Log.e("API Error", "Response Code: " + responseCode);
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                Log.e("API Error", "InputStream is null");
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                Log.e("API Error", "Buffer is empty");
                return null;
            }

            String jsonStr = buffer.toString();
            Log.d("API JSON Response", jsonStr);
            return new JSONObject(jsonStr);
        } catch (IOException | JSONException e) {
            Log.e("API Error", e.getMessage(), e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("BufferedReader Error", e.getMessage(), e);
                }
            }
        }
    }

    public static class DateFunc {

        public static Date StringToDate(String date) throws ParseException {
            Log.e("StringToDate: ", date);
            if (date.length() == 10) {
                if (date.contains(":")) {
                    date = date.replace(":", "-");
                    return new SimpleDateFormat("yyyy-MM-dd").parse(date);
                } else {
                    return new SimpleDateFormat("yyyy-MM-dd").parse(date);
                }
            } else {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
            }
        }

        public static Date StringToTime(String date) throws ParseException {
            Log.e("StringToTime: ", date.toString());
            return new SimpleDateFormat("HH:mm:ss").parse(date);
        }

        public static String DateObjectToDate(Date date) {
            return new SimpleDateFormat("MMM/dd").format(date);
        }

        public static String DateObjectToTime(Date date) {
            Log.e("DateObjectToTime:--------", date + "");
            return new SimpleDateFormat("HH:mm").format(date);
        }

        public static String DateObjectToDay(Date date) {
            return new SimpleDateFormat("EEEE").format(date);
        }
    }
}