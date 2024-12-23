package com.spacECE.spaceceedu.ConsultUS;

import static com.spacECE.spaceceedu.LearnOnApp.LearnOn_List_RecycleAdapter.orderID;
import static java.lang.String.format;
import static java.lang.String.valueOf;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.instamojo.android.Instamojo;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.ConfigUtils;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Consultant_GetAppointment extends AppCompatActivity implements Instamojo.InstamojoPaymentCallback {

    String name = "No name";
    String consultant_id = "Consultant ID missing";
    String speciality = "None";
    String fee="Free";
    String pic_src;
    String timing_from= "";
    String timing_to = "";

    String date, time;
    int mYear, mMonth, mDay, mHour, mMinute;

    private TextView tv_date,tv_time;
    private TextView tv_confirmation,tv_name,tv_speciality,tv_charges;
    private ImageView iv_profile;
    private Button b_confPay;
    private TextView clock, calendar, duration;
    TextView Consultant_GetAppointment_c_aval_days;
    private Button add15, sub15;
    private int Duration = 0;
    private Boolean Date_picked = false;
    private Boolean Time_picked = false;
    private String BOOKING_DAY, BOOKING_TIME;
    String c_aval_days;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_get_appointment);
        getWindow().setStatusBarColor(ContextCompat.getColor(Consultant_GetAppointment.this,R.color.orange));

        tv_charges = findViewById(R.id.Consultant_GetAppointment_textView_Charges);
        tv_name = findViewById(R.id.Consultant_GetAppointment_Name);
        tv_speciality = findViewById(R.id.Consultant_GetAppointment_Speciality);
        iv_profile = findViewById(R.id.Consultant_GetAppointment_ProfilePic);
        tv_confirmation = findViewById(R.id.Consultant_GetAppointment_TextView_Confirmation);
        b_confPay = findViewById(R.id.Consultant_GetAppointment_Button_Confirm);
        tv_time = findViewById(R.id.Consultant_GetAppointment_textView_Timing);
        Consultant_GetAppointment_c_aval_days = findViewById(R.id.Consultant_GetAppointment_c_aval_days);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("consultant_name");
            fee= extras.getString("fee");
            consultant_id = extras.getString("consultant_id");
            speciality = extras.getString("speciality");
            pic_src=extras.getString("profile_pic");
            timing_from = extras.getString("startTime");
            timing_to = extras.getString("endTime");
            c_aval_days=extras.getString("c_aval_days").replace(",",", ");

        }
        tv_speciality.setText(speciality);
        tv_charges.append(fee);
        tv_name.setText(name);
        tv_time.setText(timing_from.substring(0,5)+" - "+timing_to.substring(0,5));
        Consultant_GetAppointment_c_aval_days.setText(c_aval_days);

        System.out.println(pic_src);

        try {
            Picasso.get().load(pic_src.replace("https://","http://")).into(iv_profile);
            Log.e("onCreate:1",Picasso.get().load(pic_src.replace("https://","http://"))+"");

        } catch (Exception e) {
            Log.e( "onCreate:2",e.toString());
        }

        String url = pic_src.replace("https://","http://");
        RequestQueue requestQueue=new Volley().newRequestQueue(this);
        StringRequest stringRequest=new StringRequest(url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String rsp=response;
                Log.e( "onResponse:-----------------",rsp);
                if (rsp.contains("404 Not Found") || rsp.contains("message=Not Found") || rsp.contains("404") || rsp.length()==1) {
                    Log.e( "onResponse:---------","Not exist");
                    setimg();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e( "onFailure:-----------------",error.toString());
                setimg();
            }
        });
        requestQueue.add(stringRequest);

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
                        tv_confirmation.setText("Appointment will be booked on " + date+" at " + time);
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
                        Date_picked =true; //to mark date is pciked
                        date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + " ";
                        BOOKING_DAY = format("%04d:%02d:%02d ", year, (monthOfYear+1), dayOfMonth);
                        calendar.setText(date);

                        if(!Time_picked){ //is time is not picked before launch time picker
                            timePicker();
                        }
                        DateAndTimePicked(); //checks if time falls between the consultant range
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
                        Time_picked = true; //same as above
                        mHour = hourOfDay;
                        mMinute = minute;
                        time = format("%02d:%02d", hourOfDay, minute);
                        BOOKING_TIME = format("%02d:%02d:00", hourOfDay, minute);
                        clock.setText(time);

                        if(!Date_picked) { //same as above
                            datePicker();
                        }
                        DateAndTimePicked(); //same as above
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void DateAndTimePicked() {
        if(Date_picked && Time_picked){
            try {
                if(validTime(timing_from, timing_to, BOOKING_TIME)){
                    tv_confirmation.setText("Appointment will be booked on " + date+" at " + time);
                } else {
                    Toast.makeText(getApplicationContext(), "Select A valid Time", Toast.LENGTH_SHORT).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validTime(String start, String end, String to_check) throws ParseException {
        Date Start = UsefulFunctions.DateFunc.StringToTime(start);
        Date End = UsefulFunctions.DateFunc.StringToTime(end);
        Date To_Check = UsefulFunctions.DateFunc.StringToTime(to_check);

        return (To_Check.before(End) & To_Check.after(Start));

    }

    private void BookAppointment() {
        try {
            JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
            if (config != null) {
                String baseUrl= config.getString("BASE_URL");
                String consultBookAppointUrl = config.getString("CONSULT_BOOKAPPOINT");


                System.out.println(MainActivity.ACCOUNT.getAccount_id()+consultant_id+ BOOKING_DAY + BOOKING_TIME);
                System.out.println(String.valueOf(Duration));
                Log.e( "BookAppointment: 1",MainActivity.ACCOUNT.getAccount_id()+consultant_id+ BOOKING_DAY + BOOKING_TIME);
                Log.e( "BookAppointment: 2",String.valueOf(Duration));
                new Thread(new Runnable() {

                    JSONObject jsonObject;

                    final String booking = baseUrl+consultBookAppointUrl;

                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        RequestBody fromBody = new FormBody.Builder()
                                .add("u_id", MainActivity.ACCOUNT.getAccount_id())
                                .add("c_id", consultant_id)
                                .add("b_date", BOOKING_DAY.toString().replace(" ","").replace(":","-"))
                                .add("time", BOOKING_TIME)
                                .add("end_time", valueOf(Duration))
                                .build();
                        Log.e( "api hit at Consult_GetAppointment: ","u_id"+"="+MainActivity.ACCOUNT.getAccount_id()+"&"+"c_id="+consultant_id+"&b_date="+BOOKING_DAY.toString().replace(" ","")+"&time="+BOOKING_TIME+"&end_time="+valueOf(Duration));

                        Request request = new Request.Builder()
                                .url(booking)
                                .post(fromBody)
                                .build();


                        Call call = client.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                System.out.println("Registration Error ApI " + e.getMessage());
                                Log.e( "onFailure: 1",e.toString());
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            assert response.body() != null;
                                            jsonObject = new JSONObject(response.body().string());
                                            System.out.println(jsonObject);
                                            if(jsonObject.getString("status").equals("success")){
                                                Toast.makeText(Consultant_GetAppointment.this,"Booking Confirmed",
                                                        Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(getApplicationContext(), Consultant_AppointmentConfirmation.class));
                                                finishAffinity();
                                            } else {
                                                Log.e( "run: 3","failed");
                                                Toast.makeText(Consultant_GetAppointment.this,"Booking Failed",
                                                        Toast.LENGTH_LONG).show();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            Toast.makeText(Consultant_GetAppointment.this,jsonObject.getString("msg").toString(), Toast.LENGTH_SHORT).show();
                                                        } catch (JSONException e) {
                                                            Log.e( "run: 6", e.toString());
                                                            throw new RuntimeException(e);
                                                        }
                                                    }
                                                },2500);
                                            }
                                        } catch (JSONException | IOException e) {
                                            Log.e( "run: instamojo1",e.toString());
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
        catch (Exception e) {
            e.printStackTrace();
            Log.i("ERROR:::", "Failed to load API URLs");
        }

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
    public void setimg(){
        iv_profile.setImageDrawable(getDrawable(R.drawable.img_1));
    }
}