package com.spacECE.spaceceedu.Utils;

import android.content.Context;

import org.json.JSONObject;

import java.io.InputStream;

public class ConfigUtils {
    public static JSONObject loadConfig(Context context) {
        try {
            InputStream is = context.getAssets().open("config.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            return new JSONObject(json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
