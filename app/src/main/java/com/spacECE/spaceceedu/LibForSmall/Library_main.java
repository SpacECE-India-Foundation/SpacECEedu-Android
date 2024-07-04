package com.spacECE.spaceceedu.LibForSmall;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.spacECE.spaceceedu.FragmentAbout;
import com.spacECE.spaceceedu.FragmentProfile;
import com.spacECE.spaceceedu.HomeFragmentLibForSmall;
import com.spacECE.spaceceedu.R;

import java.util.ArrayList;

public class Library_main extends AppCompatActivity {

    public static ArrayList<books> list = new ArrayList<>();
    private Fragment currentFragment = null;
    Fragment fragment=new library_list();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.libs_for_small_fragment_container, fragment).commit();

        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomAppBar);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menuHome:
                        replaceFragment(new HomeFragmentLibForSmall());
                        return true;

                    case R.id.menuProfile:
                        Toast.makeText(Library_main.this, "Welcome to Profile", Toast.LENGTH_SHORT).show();
                        replaceFragment(new FragmentProfile());
                        return true;

                    case R.id.consultUs:
                        Toast.makeText(Library_main.this, "Welcome to Help Section", Toast.LENGTH_SHORT).show();
                        replaceFragment(new FragmentAbout());
                        return true;


                    default:
                        return false;
                }
            }
        });
        replaceFragment(new HomeFragmentLibForSmall());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem addBooksMenuItem = menu.findItem(R.id.toolbar_menu_add_books);
        addBooksMenuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if(id==R.id.toolbar_menu_home){
            replaceFragment(new HomeFragmentLibForSmall());
        }
        if(id==R.id.toolbar_menu_add_books){
            replaceFragment(new AddBooks());
        }
        if(id==R.id.toolbar_menu_my_books){
            replaceFragment(new MyBooks());
        }
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.libs_for_small_fragment_container, fragment).commit();
        currentFragment = fragment;
    }

}



