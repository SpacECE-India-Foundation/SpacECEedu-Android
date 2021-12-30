package com.spacECE.spaceceedu.Library;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Library_main extends AppCompatActivity {

    Button books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_main);

        ArrayList<String> Books = new ArrayList<>();

        boolean[] COMPLETED = {false};
        JSONObject[] apiCall = {null};


        books = findViewById(R.id.My_books) ;

       books.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),My_books.class);
                startActivity(i);
            }
       });

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    apiCall[0] = UsefulFunctions.UsingGetAPI("http://educationfoundation.space/ConsultUs/api_user_appoint?user=raju%20rastogi");
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


                } catch (RuntimeException runtimeException) {
                    Log.i("RUNTIME EXCEPTION:::", "Server did not respons");
                }
            }
        });

        thread.start();


    }
}



