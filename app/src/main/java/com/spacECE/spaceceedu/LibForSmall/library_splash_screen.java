package com.spacECE.spaceceedu.LibForSmall;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.spacECE.spaceceedu.LibForSmall.Library_main;
import com.spacECE.spaceceedu.LibForSmall.books;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class library_splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_splash_screen);

        LoadList();
    }

    void LoadList() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final JSONObject apiCall;
                try{
                    apiCall = UsefulFunctions.UsingGetAPI("http://43.205.45.96/libforsmall/allproductlist.php");
                    JSONArray jsonArray = null;
                    try {
                        try {
                            assert apiCall != null;
                        } catch (AssertionError e) {

                            e.printStackTrace();

                            runOnUiThread(() -> {
                                new AlertDialog.Builder(library_splash_screen.this)
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
                                                Intent intent = new Intent(library_splash_screen.this, Library_main.class);
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
                    Library_main.list = new ArrayList<>();
                    try {
                        Log.d("TAG", "run: "+jsonArray.length());
                        for (int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++) {
                            JSONObject response_element = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));
                            books temp = new books(response_element.getString("product_id"),response_element.getString("product_title"),
                                    response_element.getString("product_price"),response_element.getString("product_keywords"),
                                    response_element.getString("product_image"),response_element.getString("product_desc"),
                                    response_element.getString("product_brand"), response_element.getString("rent_price"),
                                    response_element.getString("exchange_price"),response_element.getString("deposit"));
                            Library_main.list.add(temp);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(library_splash_screen.this, Library_main.class);
                    startActivity(intent);
                    finish();

                } catch ( Exception e) {
                    Log.i("EXCEPTION", e.toString());
                }
            }
        });

        thread.start();
    }
}