package com.spacECE.spaceceedu.LibForSmall;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.spacECE.spaceceedu.R;

import java.util.ArrayList;

public class library_my_books extends AppCompatActivity {

    public static ArrayList<books> list = new ArrayList<>();

    Fragment fragment=new library_myBooks_list();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_my_books);
        getSupportFragmentManager().beginTransaction().replace(R.id.mybooks_frame, fragment).commit();
    }

}