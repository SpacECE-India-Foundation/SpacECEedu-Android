package com.spacECE.spaceceedu.LearnOnApp;


import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.spacECE.spaceceedu.R;

import java.util.ArrayList;

public class LearnOn_Main extends AppCompatActivity {

    public static ArrayList<Learn> Llist = new ArrayList<>();
    Fragment fragment = new LearnOn_List();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_on_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.LearnOnMain_Frame, fragment).commit();
    }

}
