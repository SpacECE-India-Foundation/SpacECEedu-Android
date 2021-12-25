package com.spacECE.spaceceedu.Consultants;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;
import com.squareup.picasso.Picasso;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.spacECE.spaceceedu.MainActivity.BUILD_NUMBER;
import static java.lang.String.format;

public class Consultant_GetAppointment extends AppCompatActivity {

    Appointment userAppointment ;
    String name = "No name";
    String consultant_id = "Consultant ID missing";
    String speciality = "None";
    String fee="Free";
    String pic_src = "";

    String date, time;
    int mYear, mMonth, mDay, mHour, mMinute;

    private TextView tv_date,tv_time;
    private TextView tv_confirmation,tv_name,tv_speciality,tv_charges;
    private ImageView iv_profile;
    private Button b_confPay;
    private TextView clock, calendar;
    private Boolean Date_picked = false;
    private Boolean Time_picked = false;
    private String BOOKINGDAY, BOOKINGTIME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_get_appointment);

        tv_charges = findViewById(R.id.Consultant_GetAppointment_textView_Charges);
        tv_name = findViewById(R.id.Consultant_GetAppointment_Name);
        tv_speciality = findViewById(R.id.Consultant_GetAppointment_Speciality);
        iv_profile = findViewById(R.id.Consultant_GetAppointment_ProfilePic);
        tv_confirmation = findViewById(R.id.Consultant_GetAppointment_TextView_Confirmation);
        b_confPay = findViewById(R.id.Consultant_GetAppointment_Button_Confirm);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("consultant_name");
            fee= extras.getString("fee");
            consultant_id = extras.getString("consultant_id");
            speciality = extras.getString("speciality");
            pic_src=extras.getString("profile_pic");
        }
        tv_speciality.setText(speciality);
        tv_charges.setText(fee);
        tv_name.setText(name);

        try {
            pic_src = "https://spacefoundation.in/test/SpacECE-PHP/img/users/" + pic_src;
            Picasso.get().load(pic_src.replace("https://","http://")).into(iv_profile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        clock = findViewById(R.id.Clock);
        calendar = findViewById(R.id.Calendar);

        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker();
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    datePicker();
            }
        });


        b_confPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Date_picked&Time_picked){
                    //tv_confirmation.setText(BOOKINGDAY+BOOKINGTIME);
                    BookAppointment();
                } else{
                    Toast.makeText(Consultant_GetAppointment.this, "Please Select Date and Time",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void datePicker(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                        Date_picked =true;
                        date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + " ";
                        BOOKINGDAY = format("%d:%d:%d ", year, (monthOfYear+1), dayOfMonth);
                        calendar.setText(date);

                        if(!Time_picked){
                            timePicker();
                        }
                        if(Date_picked && Time_picked){
                            tv_confirmation.setText("Appointment booked on " + date + time);
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void timePicker(){

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
                        Time_picked = true;
                        mHour = hourOfDay;
                        mMinute = minute;
                        time = format("%02d:%02d", hourOfDay, minute);
                        BOOKINGTIME = format("%02d:%02d:00", hourOfDay, minute);
                        clock.setText(time);

                        if(!Date_picked) {
                            datePicker();
                        }
                        if(Date_picked && Time_picked){
                            tv_confirmation.setText("Appointment booked on " + date + time);
                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public static class Appointments {

        private String date,month;
        private String year;
        private String[] time;

        public String getDate() {
            return date;
        }

        public String getMonth() {
            return month;
        }

        public String getYear() {
            return year;
        }

        public String[] getTime() {
            return time;
        }

        public Appointments(String date, String month, String year, String[] time) {
            this.date = date;
            this.month = month;
            this.year = year;
            this.time = time;
        }

        public Appointments(String date, String month, String[] time) {
            this.date = date;
            this.month = month;
            this.time = time;
        }
    }

    private void BookAppointment() {

        new Thread(new Runnable() {

            JSONObject jsonObject;

            final String booking = "http://spacefoundation.in/test/SpacECE-"+
                    BUILD_NUMBER+"/ConsultUs/api_bookappointment.php";

            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody fromBody = new FormBody.Builder()
                        .add("u_id", MainActivity.ACCOUNT.getAccount_id())
                        .add("c_id", consultant_id)
                        .build();

                Request request = new Request.Builder()
                        .url(booking)
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

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    jsonObject = new JSONObject(response.body().string());
                                    System.out.println(jsonObject);
                                    if(jsonObject.getString("status").equals("success")){
                                        Toast.makeText(Consultant_GetAppointment.this,"Booking Confirmed",
                                                Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), Consultant_AppointmentConfirmation.class));
                                        finishAffinity();
                                    } else {
                                        Toast.makeText(Consultant_GetAppointment.this,"Booking Failed",
                                                Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        }).start();

    }

}