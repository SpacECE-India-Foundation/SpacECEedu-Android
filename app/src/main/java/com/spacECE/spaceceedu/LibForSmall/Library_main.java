package com.spacECE.spaceceedu.LibForSmall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.spacECE.spaceceedu.R;

import java.util.ArrayList;

public class Library_main extends AppCompatActivity {

    public static ArrayList<books> list = new ArrayList<>();

    BottomNavigationView bottomNavigationView;

    Allbooks_fragment allbooks_fragment = new Allbooks_fragment();
    Mybooks_fragment mybooks_fragment = new Mybooks_fragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_main);

        // Fixed: Use correct ID from XML (R.id.bottomAppBar instead of R.id.bottom_navigation)
        bottomNavigationView = findViewById(R.id.bottomAppBar);
        
        // Fixed: Use correct container ID from XML (R.id.book_framelayout instead of R.id.container)
        getSupportFragmentManager().beginTransaction().replace(R.id.book_framelayout, allbooks_fragment).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.allbooks) {
                getSupportFragmentManager().beginTransaction().replace(R.id.book_framelayout, allbooks_fragment).commit();
                return true;
            } else if (itemId == R.id.mybooks) {
                getSupportFragmentManager().beginTransaction().replace(R.id.book_framelayout, mybooks_fragment).commit();
                return true;
            } else if (itemId == R.id.chat) {
                startActivity(new Intent(getApplicationContext(), ChatUS.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });

    }
}