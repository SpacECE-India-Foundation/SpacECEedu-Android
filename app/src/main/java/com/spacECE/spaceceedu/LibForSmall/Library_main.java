package com.spacECE.spaceceedu.LibForSmall;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Library_main extends AppCompatActivity {

    public static ArrayList<books> list = new ArrayList<>();

    Fragment fragment=new library_list();
    public  FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.book_framelayout, fragment).commit();

        floatingActionButton=findViewById(R.id.floatingActionBtnBottom);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Library_main.this, AddBook.class);
                startActivity(i);
            }
        });

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomAppBar);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuBook:
                        startActivity(new Intent(getApplicationContext(),library_my_books.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menuChat:
                        startActivity(new Intent(getApplicationContext(), ChatUS.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menuHome:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return  false;
            }
        });




    }
}



