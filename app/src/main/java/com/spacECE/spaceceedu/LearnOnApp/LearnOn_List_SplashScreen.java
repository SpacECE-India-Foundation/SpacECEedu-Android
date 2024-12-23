package com.spacECE.spaceceedu.LearnOnApp;

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
import java.util.Objects;

public class LearnOn_List_SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

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
                        String learnCourseFetchCourseUrl = config.getString("LEARNCOURSE_FETCHCOURSE");
                        apiCall = UsefulFunctions.UsingGetAPI(baseUrl+learnCourseFetchCourseUrl);
                        JSONArray jsonArray = null;
                        try {
                            try {
                                assert apiCall != null;
                            } catch (AssertionError e) {

                                e.printStackTrace();

                                runOnUiThread(() -> {
                                    new AlertDialog.Builder(LearnOn_List_SplashScreen.this)
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
                                                    Intent intent = new Intent(LearnOn_List_SplashScreen.this, MainActivity.class);
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
                        LearnOn_Main.Llist = new ArrayList<>();
                        try {
                            for (int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++) {
                                JSONObject response_element = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));
                                Learn temp = new Learn(response_element.getString("id"), response_element.getString("title"),
                                        response_element.getString("description"), response_element.getString("type"),
                                        response_element.getString("mode"), response_element.getString("duration"),
                                        response_element.getString("price"));
                                LearnOn_Main.Llist.add(temp);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(LearnOn_List_SplashScreen.this, LearnOn_Main.class);
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
