package com.spacECE.spaceceedu.VideoLibrary;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.http.Tag;

public class VideoLibrary_Activity extends AppCompatActivity {

    public final static boolean[] ArrayDownloadCOMPLETED = {false};
    public static ArrayList<Topic> trendingTopicList = new ArrayList<>();//used in VideoLibrary_trending_Fragment.java
    public static ArrayList<Topic> paidTopicList = new ArrayList<>();//used in VideoLibrary_Premium.java
    public static ArrayList<Topic> freeTopicList = new ArrayList<>();  //used in VideoLibrary_Free.java
    NavigationBarView.OnItemSelectedListener VL_navListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.videolibrary_nav_free:
                            selectedFragment = new VideoLibrary_Free();
                            break;
                        case R.id.videolibrary_nav_paid:
                            selectedFragment = new VideoLibrary_Premium();
                            break;
                        case R.id.videolibrary_nav_trending:
                            selectedFragment = new VideoLibrary_trending_Fragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.VideoLibrary_Fragment_layout,
                            selectedFragment).commit();
                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_library);

        BottomNavigationView videoLibraryBottomNav = findViewById(R.id.VideoLibrary_Bottom_Navigation);
        videoLibraryBottomNav.setOnItemSelectedListener(VL_navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.VideoLibrary_Fragment_layout,
                    new VideoLibrary_Free()).commit();
        }

    }
}