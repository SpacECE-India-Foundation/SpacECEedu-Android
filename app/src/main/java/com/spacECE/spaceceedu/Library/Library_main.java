package com.spacECE.spaceceedu.Library;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.MulticastSocket;
import java.util.ArrayList;

public class Library_main extends AppCompatActivity {

    Button books;

    RecyclerView recyclerView;
    public FloatingActionButton floatingActionButton;

    ArrayList<String> book_name,book_price,book_category;
    BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_main);

        ArrayList<String> Books = new ArrayList<>();

        boolean[] COMPLETED = {false};
        JSONObject[] apiCall = {null};

        floatingActionButton=findViewById(R.id.floatingActionBtnBottom);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Library_main.this, AddBook.class);
                startActivity(intent);
            }
        });

        bottomAppBar=findViewById(R.id.bottomAppBar);
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId()==R.id.menuChat){
                    Intent i=new Intent(Library_main.this, chatUS.class);
                    startActivity(i);
                }
                if(item.getItemId()==R.id.menuBook){
                    Intent i=new Intent(Library_main.this, My_books.class);
                    startActivity(i);
                }
                if(item.getItemId()==R.id.menuMaps){
                    Intent i=new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("geo:47.4925,19.0513"));
                    Intent chooser=Intent.createChooser(i,"Lauch Maps");
                    startActivity(chooser);

                }
                return false;
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



