package com.spacECE.spaceceedu.ConsultUS;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.ConfigUtils;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConsultUs_SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //launched the spinner and loaded the list and then started the activity
        LoadList();

    }

    void LoadList() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final JSONObject apiCall;
                try{
                    JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
                    if(config != null) {
                        String baseUrl= config.getString("BASE_URL");
                        String consultAllCategoryUrl = config.getString("CONSULT_ALLCATEGORY");

                        apiCall = UsefulFunctions.UsingGetAPI(baseUrl+consultAllCategoryUrl);
                        JSONArray jsonArray = null;
                        try {
                            try {
                                assert apiCall != null;
                            } catch (AssertionError e) {

                                e.printStackTrace();

                                //checked for if there is any issue with either the api or the internet

                                runOnUiThread(() -> {
                                    new AlertDialog.Builder(ConsultUs_SplashScreen.this)
                                            .setTitle("Internet Not Working!")
                                            .setMessage("Do you want to retry ?")

                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    LoadList();
                                                }
                                            })

                                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(ConsultUs_SplashScreen.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })

                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                });

                            }
                            jsonArray = apiCall.getJSONArray("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Consultant_Main.categoryList = new ArrayList<>();
                        try {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ConsultantCategory newCategory = new ConsultantCategory((String) jsonArray.get(i), "nice");
                                Consultant_Main.categoryList.add(newCategory);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Intent intent = new Intent(ConsultUs_SplashScreen.this, Consultant_Main.class);
                        startActivity(intent);
                        finish();
                    }
                } catch ( Exception e) {
                    Log.i("EXCEPTION", e.toString());
                }
            }
        });


        thread.start();

    }
}