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
import com.spacECE.spaceceedu.Utils.ConfigUtils;
import com.spacECE.spaceceedu.Utils.Notification;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

        // Remove the duplicate initialization of Fragment_Consultant_Categories
        if(MainActivity.ACCOUNT!=null){
            if (MainActivity.ACCOUNT.isCONSULTANT()) {
                bottomNav.inflateMenu(R.menu.consultant_main_consultants_bottomnav);
                generateAppointmentsListForUser();
                generateAppointmentsListForConsultant();
            } else {
                bottomNav.inflateMenu(R.menu.consultant_main_user_bottomnav);
                generateAppointmentsListForUser();
            }
        } else {
            bottomNav.inflateMenu(R.menu.consultant_main_nouser_bottomnav);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.ConsultantMain_Frame,
                        new Fragment_Consultant_Categories()).commit();
            }
            return; // Return early if no user is logged in
        }

        // Check if we should show appointments screen
        boolean showAppointments = getIntent().getBooleanExtra("show_appointments", false);
        if (showAppointments) {
            // Show appropriate appointments fragment after a short delay to allow data to load
            new android.os.Handler().postDelayed(() -> {
                Fragment selectedFragment;
                if (MainActivity.ACCOUNT != null && MainActivity.ACCOUNT.isCONSULTANT()) {
                    selectedFragment = new Fragment_Appointments_For_Consultants();
                } else {
                    selectedFragment = new Fragment_Appointments_For_User();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.ConsultantMain_Frame, selectedFragment)
                        .commit();
            }, 1000); // 1 second delay to allow API call to complete
        } else {
            // Only show categories if not showing appointments
            getSupportFragmentManager().beginTransaction().replace(R.id.ConsultantMain_Frame,
                    new Fragment_Consultant_Categories()).commit();
        }

    }

    private void generateAppointmentsListForUser() {
        try {
            JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
            if (config != null) {
                String baseUrl= config.getString("BASE_URL");
                String consultUserAppointUrl = config.getString("CONSULT_USERAPPOINT");


                new Thread(new Runnable() {

                    JSONObject jsonObject;
                    JSONArray jsonArray;

                    String url = baseUrl+consultUserAppointUrl;

                    @Override
                    public void run() {
                        // Clear existing appointments list before making the API call
                        Fragment_Appointments_For_User.appointmentsArrayList.clear();

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
                                                        , response_element.getString("c_image"), response_element.getString("u_image"),response_element.getString("u_mob"), response_element.getString("b_date").replace("-",":")
                                                        , response_element.getString("end_time"));
                                                Log.e( "onResponse:------------",response_element.getString("booking_time")+"---------------");
                                                newAppointment.setTime(response_element.getString("booking_time"));

                                                Intent intent = new Intent(Consultant_Main.this, Notification.class);
                                                PendingIntent pendingIntent = PendingIntent.getBroadcast(Consultant_Main.this,
                                                        Integer.parseInt(response_element.getString("booking_id")), intent, PendingIntent.FLAG_IMMUTABLE);
                                                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                                Date date = new Date();
                                                Calendar calendar = Calendar.getInstance();
                                                try {
                                                    date = UsefulFunctions.DateFunc.StringToDate(response_element.getString("b_date"));
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
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i("ERROR:::", "Failed to load API URLs");
        }
    }

    private void generateAppointmentsListForConsultant() {
        try {
            JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
            if (config != null) {
                String baseUrl= config.getString("BASE_URL");
                String consultUserAppointUrl = config.getString("CONSULT_USERAPPOINT");


                new Thread(new Runnable() {

                    JSONObject jsonObject;
                    JSONArray jsonArray;

                    String url = baseUrl+consultUserAppointUrl;

                    @Override
                    public void run() {
                        // Clear existing appointments list before making the API call
                        Fragment_Appointments_For_Consultants.appointmentsArrayList.clear();
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
                                    assert response.body() != null;
                                    jsonObject = new JSONObject(response.body().string());
                                    Log.d("Login", "onResponse: " + jsonObject);

                                    if(jsonObject.getString("status").equals("success")) {

                                        jsonArray = jsonObject.getJSONArray("data");

                                        Fragment_Appointments_For_Consultants.appointmentsArrayList = new ArrayList<>();

                                        try {
                                            for (int i = 0; i < jsonArray.length(); i++) {

                                                JSONObject response_element = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));

                                                Log.e( "onResponse: ", "hit-----------------------------------------");

                                                Appointment newAppointment = new Appointment(response_element.getString("c_id"), response_element.getString("c_name"), response_element.getString("u_name")
                                                        , response_element.getString("c_image"), response_element.getString("u_image"),response_element.getString("u_mob"), response_element.getString("b_date")
                                                        , response_element.getString("end_time"));
                                                newAppointment.setTime(response_element.getString("booking_time"));

                                                try {
                                                    Intent intent = new Intent(Consultant_Main.this, Notification.class);
                                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(Consultant_Main.this,
                                                            Integer.parseInt(response_element.getString("booking_id")), intent,PendingIntent.FLAG_IMMUTABLE);
                                                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                                    Date date = new Date();
                                                    Calendar calendar = Calendar.getInstance();
                                                    Log.e( "onResponse: ", "hit---------------------------------");
                                                    date = UsefulFunctions.DateFunc.StringToDate(response_element.getString("b_date"));
                                                    calendar.setTime(date);
                                                    Log.d("Notification", "sendNotification: "+calendar.getTime());
                                                    alarmManager.set(AlarmManager.RTC_WAKEUP,
                                                            calendar.getTimeInMillis()-1000*60*10, pendingIntent);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                    Log.e( "onResponse: ",e.toString());
                                                }
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
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i("ERROR:::", "Failed to load API URLs");
        }
    }



    public static void SetDateTimeDay(int position, ArrayList<Appointment> myConsultants, TextView date, TextView time, TextView day) {
        //using this function to put in values in different fragments
        Date dateObject = new Date();
        try {
            dateObject = UsefulFunctions.DateFunc.StringToDate(myConsultants.get(position).getBookedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e( "SetDateTimeDay:------------",UsefulFunctions.DateFunc.DateObjectToDate(dateObject)+"----"+UsefulFunctions.DateFunc.DateObjectToTime(dateObject)+"=="+UsefulFunctions.DateFunc.DateObjectToDay(dateObject));
        date.setText(UsefulFunctions.DateFunc.DateObjectToDate(dateObject));
        Log.e("SetDateTimeDay:876545",myConsultants.get(position).getTime()+"---------");
        time.setText(myConsultants.get(position).getTime());
        day.setText(UsefulFunctions.DateFunc.DateObjectToDay(dateObject));
    }


}
