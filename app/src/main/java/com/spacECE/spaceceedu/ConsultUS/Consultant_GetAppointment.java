package com.spacECE.spaceceedu.ConsultUS;


import android.content.Intent;
import android.os.Build;
import android.os.Parcel;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.instamojo.android.Instamojo;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static com.spacECE.spaceceedu.LearnOnApp.LearnOn_List_RecycleAdapter.orderID;
import static com.spacECE.spaceceedu.Utils.UsefulFunctions.DateFunc.StringToTime;
import static java.lang.String.*;
import static java.lang.String.format;

public class Consultant_GetAppointment extends AppCompatActivity implements Instamojo.InstamojoPaymentCallback {

    String name = "No name";
    String consultant_id = "Consultant ID missing";
    String speciality = "None";
    String fee="Free";
    String pic_src;
    String available_days="";
    String timing_from= "";
    String timing_to = "";

    String date, time;
    int mYear, mMonth, mDay, mHour, mMinute;

    private TextView tv_date,tv_time;
    private TextView tv_confirmation,tv_name,tv_speciality,tv_charges;
    private ImageView iv_profile;
    private Button b_confPay;
    private TextView clock, calendar, duration;
    private Button add15, sub15;
    private int Duration = 0;
    private Boolean Date_picked = false;
    private Boolean Time_picked = false;
    private String BOOKING_DAY, BOOKING_TIME;


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
        tv_time = findViewById(R.id.Consultant_GetAppointment_textView_Timing);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("consultant_name");
            fee= extras.getString("fee");
            consultant_id = extras.getString("consultant_id");
            speciality = extras.getString("speciality");
            pic_src=extras.getString("profile_pic");
            available_days=extras.getString("available_days");
            timing_from = extras.getString("startTime");
            timing_to = extras.getString("endTime");

        }
        tv_speciality.setText(speciality);
        tv_charges.setText(fee);
        tv_name.setText(name);
        tv_time.setText("Available from "+timing_from.substring(0,5)+" - "+timing_to.substring(0,5));

        System.out.println(pic_src);

        try {
            Picasso.get().load(pic_src.replace("https://","http://")).into(iv_profile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        clock = findViewById(R.id.Clock);
        calendar = findViewById(R.id.Calendar);
        duration = findViewById(R.id.Duration);
        add15 = findViewById(R.id.add15);
        sub15 = findViewById(R.id.sub15);

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

        add15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Duration += 15;
                duration.setText(format("%d minutes", Duration));
            }
        });

        sub15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Duration > 0) {
                    Duration -= 15;
                    duration.setText(format("%d minutes", Duration));
                }
            }
        });

        b_confPay.setOnClickListener(view -> {
            if(Date_picked & Time_picked & Duration>0){
                //tv_confirmation.setText(BOOKING_DAY+BOOKING_TIME);
                try {
                    if(validTime(timing_from, timing_to, BOOKING_TIME)){
                        tv_confirmation.setText("Appointment booked on " + date + time);
                        Instamojo.getInstance().initialize(this, Instamojo.Environment.TEST);
                        Instamojo.getInstance().initiatePayment(this, orderID, this);
                        //now book appointment in on payment success class
                        BookAppointment();
                    } else {
                        Toast.makeText(getApplicationContext(), "Select A valid Time", Toast.LENGTH_SHORT).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else{
                Toast.makeText(Consultant_GetAppointment.this, "Please Select Date, Time and Duration",
                        Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void datePicker(){
        //date picker

//        Calendar[] ddd = AvailableDays();
//
//        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePickerDialog view, int year,int monthOfYear, int dayOfMonth) {
//                        Date_picked =true; //to mark date is pciked
//                        date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + " ";
//                        BOOKING_DAY = format("%04d:%02d:%02d ", year, (monthOfYear+1), dayOfMonth);
//                        calendar.setText(date);
//
//                        if(!Time_picked){ //is time is not picked before launch time picker
//                            timePicker();
//                        }
//                        DateAndTimePicked(); //checks if time falls between the consultant range
//                    }
//                }, ddd[0].get(Calendar.YEAR), ddd[0].get(Calendar.MONTH), ddd[0].get(Calendar.DATE));
//
//        Calendar abc = Calendar.getInstance();
//        datePickerDialog.setMinDate(abc);
//        abc.add(Calendar.DATE, 30);
//        datePickerDialog.setMaxDate(abc);
//
////        datePickerDialog.setSelectableDays(ddd);
//
//        datePickerDialog.show(getFragmentManager(), "hello1");








    }

    private ArrayList<Integer> days(String AvailableDays) {
        String Days[] = AvailableDays.split(",");
        ArrayList<Integer> wording = new ArrayList<>();

        for (int i = 0; i < Days.length; i++) {
            if(Days[i].equals("Monday"))
                wording.add(2);
            if(Days[i].equals("Tuesday"))
                wording.add(3);
            if(Days[i].equals("Wednesday"))
                wording.add(4);
            if(Days[i].equals("Thursday"))
                wording.add(5);
            if(Days[i].equals("Friday"))
                wording.add(6);
            if(Days[i].equals("Saturday"))
                wording.add(7);
            if(Days[i].equals("Sunday"))
                wording.add(1);
        }
        return wording;
    }

    private Calendar[] AvailableDays() {

        ArrayList<Date> DaysAvailable = new ArrayList<Date>();
        ArrayList<Integer> DaysOfWeek = days(available_days);
        Calendar[] ddd = {};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Calendar today = Calendar.getInstance();
            for (int i = 0; i < 30; i++) {
                if(DaysOfWeek.contains(today.get(Calendar.DAY_OF_WEEK))) {
                    DaysAvailable.add(today.getTime());
                }
                today.add(Calendar.DATE, 1);
            }

            today = new GregorianCalendar();
            ddd = new Calendar[DaysAvailable.size()];

            for (int i = 0; i < DaysAvailable.size(); i++) {
                today.setTime(DaysAvailable.get(i));
                ddd[i] = today;
                System.out.println(ddd[i]);
            }
            return ddd;
        }
        return ddd;
    }

    private void timePicker(){

        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        Time_picked = true; //same as above
                        mHour = hourOfDay;
                        mMinute = minute;
                        time = format("%02d:%02d", hourOfDay, minute);
                        BOOKING_TIME = format("%02d:%02d:00", hourOfDay, minute);
                        clock.setText(time);

                        if (!Date_picked) { //same as above
                            datePicker();
                        }
                        DateAndTimePicked(); //same as above
                    }
                }, mHour, mMinute, false);


//        timePickerDialog.setSelectableTimes(new Timepoint[]{new Timepoint(15, 26)});

        timePickerDialog.show(getFragmentManager(), "hello");


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void DateAndTimePicked() {
        if(Date_picked && Time_picked){
            try {
                if(validTime(timing_from, timing_to, BOOKING_TIME)){
                    tv_confirmation.setText("Appointment booked on " + date + time);
                } else {
                    Toast.makeText(getApplicationContext(), "Select A valid Time", Toast.LENGTH_SHORT).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validTime(String start, String end, String to_check) throws ParseException {
        Date Start = StringToTime(start);
        Date End = StringToTime(end);
        Date To_Check = StringToTime(to_check);

        return (To_Check.before(End) & To_Check.after(Start));

    }

    private void BookAppointment() {

        System.out.println(MainActivity.ACCOUNT.getAccount_id()+consultant_id+ BOOKING_DAY + BOOKING_TIME);
        System.out.println(String.valueOf(Duration));
        new Thread(new Runnable() {

            JSONObject jsonObject;

            final String booking = "http://spacefoundation.in/test/SpacECE-PHP/ConsultUs/api_bookappointment.php";

            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody fromBody = new FormBody.Builder()
                        .add("u_id", MainActivity.ACCOUNT.getAccount_id())
                        .add("c_id", consultant_id)
                        .add("b_time", BOOKING_DAY + BOOKING_TIME)
                        .add("end_time", valueOf(Duration))
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
                                    System.out.println(response.body().string());
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

    @Override
    public void onInstamojoPaymentComplete(String orderID, String transactionID, String paymentID, String paymentStatus) {
        Log.d("TAG", "Payment complete. Order ID: " + orderID + ", Transaction ID: " + transactionID
                + ", Payment ID:" + paymentID + ", Status: " + paymentStatus);
        //move book payment function here
    }

    @Override
    public void onPaymentCancelled() {

    }

    @Override
    public void onInitiatePaymentFailure(String s) {

    }
}