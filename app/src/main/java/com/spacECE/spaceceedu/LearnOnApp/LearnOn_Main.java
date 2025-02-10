package com.spacECE.spaceceedu.LearnOnApp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.spacECE.spaceceedu.R;

import java.util.ArrayList;

public class LearnOn_Main extends AppCompatActivity implements PaymentResultWithDataListener {

    public static ArrayList<Learn> Llist = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    TextView showCourseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_on_main);

        // Initialize fragments
        Fragment allCoursesFragment = new LearnOn_List();
        Bundle bundle = new Bundle();
        bundle.putSerializable("course_list", Llist);
        allCoursesFragment.setArguments(bundle);

        Fragment myCoursesFragment = new LearnOn_MyCourses();

        // Default fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.LearnOnMain_Frame, allCoursesFragment)
                .commit();

        bottomNavigationView = findViewById(R.id.bottom_navigation_learn);
        showCourseList = findViewById(R.id.ShowWhichListIsSelected);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.allCourse:
                        selectedFragment = allCoursesFragment;
                        showCourseList.setText("All Course");
                        Toast.makeText(getApplicationContext(), "All Course", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.myCourse:
                        selectedFragment = myCoursesFragment;
                        showCourseList.setText("My Course");
                        Toast.makeText(getApplicationContext(), "My Course", Toast.LENGTH_SHORT).show();
                        break;
                }
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.LearnOnMain_Frame, selectedFragment).commit();
                }
                return true;

            }
        });



    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
    }
}
