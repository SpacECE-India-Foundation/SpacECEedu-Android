package com.spacECE.spaceceedu.LearnOnApp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class LearnOn_Main extends AppCompatActivity {

    public static ArrayList<Learn> Llist = new ArrayList<>();
    Fragment fragment = new LearnOn_List();
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_on_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.LearnOnMain_Frame, fragment).commit();

//        BottomNavigationView
        bottomNavigationView=findViewById(R.id.bottom_navigation_learn);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.allCourse:
//                          You Just Attach Here Fragment Manager Here Of All Course
//                            getSupportFragmentManager().beginTransaction().replace(R.id.LearnOnMain_Frame, fragment).commit();
                            Toast.makeText(getApplicationContext(), "All Course", Toast.LENGTH_SHORT).show();
                            break;

                    case R.id.myCourse:
//                          You just Attach Here Fragment Manager Here Of My Course
//                            getSupportFragmentManager().beginTransaction().replace(R.id.LearnOnMain_Frame, fragment).commit();
                            Toast.makeText(getApplicationContext(), "My Course", Toast.LENGTH_SHORT).show();
                            break;
                }
                return false;
            }
        });

    }


}
