package com.spacECE.spaceceedu.ConsultUS;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;

import com.spacECE.spaceceedu.Utils.Notification;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Consultant_Main extends AppCompatActivity {

    public static ArrayList<ConsultantCategory> categoryList = new ArrayList<>();
    NavigationBarView.OnItemSelectedListener navListener =
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.consultant_main_navButton_all:
                            selectedFragment = new Fragment_Consultant_Categories();
                            break;
                        case R.id.consultant_main_navButton_my:
                            selectedFragment = new Fragment_Appointments_For_User();
                            break;
                        case R.id.consultant_main_navButton_appointments:
                            selectedFragment = new Fragment_Appointments_For_Consultants();
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.ConsultantMain_Frame,
                            selectedFragment).commit();

                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_main);

        getWindow().setStatusBarColor(ContextCompat.getColor(Consultant_Main.this,R.color.black));

        BottomNavigationView bottomNav = findViewById(R.id.Consultant_Main_BottomNav);
        bottomNav.setOnItemSelectedListener(navListener);

        //generated the list based on user type and expanded the fragments accordingly
        if(MainActivity.ACCOUNT!=null){
            if (MainActivity.ACCOUNT.isCONSULTANT()) {
                bottomNav.inflateMenu(R.menu.consultant_main_consultants_bottomnav);
                generateAppointmentsListForUser();
                generateAppointmentsListForConsultant();
            } else {
                bottomNav.inflateMenu(R.menu.consultant_main_user_bottomnav);
                generateAppointmentsListForUser();
            }
        }else{
            bottomNav.inflateMenu(R.menu.consultant_main_nouser_bottomnav);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.ConsultantMain_Frame,
                        new Fragment_Consultant_Categories()).commit();
            }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.ConsultantMain_Frame,
                new Fragment_Consultant_Categories()).commit();
    }

    private void generateAppointmentsListForUser() {

        new Thread(new Runnable() {

            JSONObject jsonObject;
            JSONArray jsonArray;

            String url = "http://43.205.45.96/ConsultUs/api_user_appoint.php";

            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody fromBody = new FormBody.Builder()
                        .add("u_id", MainActivity.ACCOUNT.getAccount_id())
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(fromBody)
                        .build();


                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        System.out.println("Registration Error ApI " + e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {


                        try {
                            jsonObject = new JSONObject(response.body().string());
                            Log.d("Login", "onResponse: " + jsonObject);

                            if(jsonObject.getString("status").equals("success")) {
                                jsonArray = jsonObject.getJSONArray("data");

                                Fragment_Appointments_For_User.appointmentsArrayList = new ArrayList<>();

                                try {
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject response_element = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));

                                        Appointment newAppointment = new Appointment(response_element.getString("c_id"), response_element.getString("c_name"), response_element.getString("u_name")
                                                , response_element.getString("c_image"), response_element.getString("u_image"), response_element.getString("b_time")
                                                , response_element.getString("end_time"));


                                        Intent intent = new Intent(Consultant_Main.this, Notification.class);
                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(Consultant_Main.this,
                                                Integer.parseInt(response_element.getString("booking_id")), intent, 0);
                                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                        Date date = new Date();
                                        Calendar calendar = Calendar.getInstance();
                                        try {
                                            date = UsefulFunctions.DateFunc.StringToDate(response_element.getString("b_time"));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        calendar.setTime(date);
                                        Log.d("Notification", "sendNotification: "+calendar.getTime());
                                        alarmManager.set(AlarmManager.RTC_WAKEUP,
                                                calendar.getTimeInMillis()-1000*60*10, pendingIntent);

                                        Fragment_Appointments_For_User.appointmentsArrayList.add(newAppointment);
                                    }
                                    Log.i("Appointments_For_User:::::", Fragment_Appointments_For_User.appointmentsArrayList.toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    private void generateAppointmentsListForConsultant() {

        new Thread(new Runnable() {

            JSONObject jsonObject;
            JSONArray jsonArray;

            String url = "http://43.205.45.96/ConsultUs/api_user_appoint.php";

            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody fromBody = new FormBody.Builder()
                        .add("c_id", MainActivity.ACCOUNT.getAccount_id())
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(fromBody)
                        .build();


                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        System.out.println("Registration Error ApI " + e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {


                        try {
                            jsonObject = new JSONObject(response.body().string());
                            Log.d("Login", "onResponse: " + jsonObject);

                            if(jsonObject.getString("status").equals("success")) {

                                jsonArray = jsonObject.getJSONArray("data");

                                Fragment_Appointments_For_Consultants.appointmentsArrayList = new ArrayList<>();

                                try {
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject response_element = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));

                                        Appointment newAppointment = new Appointment(response_element.getString("c_id"), response_element.getString("c_name"), response_element.getString("u_name")
                                                , response_element.getString("c_image"), response_element.getString("u_image"), response_element.getString("b_time")
                                                , response_element.getString("end_time"));


                                        Intent intent = new Intent(Consultant_Main.this, Notification.class);
                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(Consultant_Main.this,
                                                Integer.parseInt(response_element.getString("booking_id")), intent, 0);
                                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                        Date date = new Date();
                                        Calendar calendar = Calendar.getInstance();
                                        try {
                                            date = UsefulFunctions.DateFunc.StringToDate(response_element.getString("b_time"));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        calendar.setTime(date);
                                        Log.d("Notification", "sendNotification: "+calendar.getTime());
                                        alarmManager.set(AlarmManager.RTC_WAKEUP,
                                                calendar.getTimeInMillis()-1000*60*10, pendingIntent);


                                        Fragment_Appointments_For_Consultants.appointmentsArrayList.add(newAppointment);
                                    }
                                    Log.i("Appointments_For_Consultant:::::", Fragment_Appointments_For_Consultants.appointmentsArrayList.toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }



    public static void SetDateTimeDay(int position, ArrayList<Appointment> myConsultants, TextView date, TextView time, TextView day) {
        //using this function to put in values in different fragments
        Date dateObject = new Date();
        try {
            dateObject = UsefulFunctions.DateFunc.StringToDate(myConsultants.get(position).getBookedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        date.setText(UsefulFunctions.DateFunc.DateObjectToDate(dateObject));
        time.setText(UsefulFunctions.DateFunc.DateObjectToTime(dateObject));
        day.setText(UsefulFunctions.DateFunc.DateObjectToDay(dateObject));
    }


}
