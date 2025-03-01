package com.spacECE.spaceceedu.LibForSmall;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.ConfigUtils;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class My_books extends AppCompatActivity {

    public static ArrayList<Book> books = new ArrayList<Book>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean[] COMPLETED = {false};
        JSONObject[] apiCall = {null};

        setContentView(R.layout.activity_my_books);

        Thread thread = new Thread(() -> {

            try {
                JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
                if(config != null) {
                    String baseUrl= config.getString("BASE_URL");
                    String libBookDataUrl = config.getString("LIB_BOOKDATA");
                    apiCall[0] = UsefulFunctions.UsingGetAPI(baseUrl+libBookDataUrl);
                    try {
                        Log.i("Object Obtained: ", apiCall[0].get("data").toString());
                    } catch (JSONException e) {
                        Log.i("API Response:", "Error");
                        e.printStackTrace();
                    }

                    JSONArray jsonArray = null;
                    try {
                        jsonArray = apiCall[0].getJSONArray("data");
                        Log.i("API : ", apiCall[0].toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            } catch (RuntimeException runtimeException) {
                Log.i("RUNTIME EXCEPTION:::", "Server did not respons");
            }
            catch (Exception e) {
                e.printStackTrace();
                Log.i("ERROR:::", "Failed to load API URLs");
            }
        });

        books.add(new Book("The number system", new Date(), 30));
        Date abc = new Date();
        Log.i("Object", abc.toString());

    }
}
