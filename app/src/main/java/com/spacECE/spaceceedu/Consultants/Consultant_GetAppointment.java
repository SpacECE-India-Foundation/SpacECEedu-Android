package com.spacECE.spaceceedu.Consultants;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.spacECE.spaceceedu.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class Consultant_GetAppointment extends AppCompatActivity {

    ArrayList<Appointments> slots = new ArrayList<>();
    ArrayList<String> timings = new ArrayList<>();
    String orderID = null;
    UserAppointments userAppointment ;
    String name = "No name";
    String consultant_id = "Consultant ID missing";
    String speciality = "None";
    String fee="Free";
    String pic_src = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png";
    String aDate="Unknown";
    String aMonth="Unknow";
    String aYear="Unknown";
    String aTime="Unknown";


    private Consultant_GetAppointment_Date_RecyclerAdapter adapter;
    private Consultant_GetAppointment_Time_RecyclerAdapter timeAdapter;
    private Consultant_GetAppointment_Date_RecyclerAdapter.RecyclerViewClickListener listener;
    private Consultant_GetAppointment_Time_RecyclerAdapter.RecyclerViewClickListener timeListener;
    private RecyclerView dateRecyclerView;
    private RecyclerView timeRecyclerView;

    private TextView tv_date,tv_time;
    private TextView tv_confirmation,tv_name,tv_speciality,tv_charges;
    private ImageView iv_profile;
    private Button b_confPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_get_appointment);

//        Instamojo.getInstance().initialize(this, Instamojo.Environment.TEST);

        tv_charges=findViewById(R.id.Consultant_GetAppointment_textView_Charges);
        tv_name=findViewById(R.id.Consultant_GetAppointment_Name);
        tv_speciality=findViewById(R.id.Consultant_GetAppointment_Speciality);
        iv_profile=findViewById(R.id.Consultant_GetAppointment_ProfilePic);
        tv_date=findViewById(R.id.Consultant_GetAppointment_TextView_AppointmentDate);
        tv_time=findViewById(R.id.Consultant_GetAppointment_TextView_AppointmentTime);
        tv_confirmation=findViewById(R.id.Consultant_GetAppointment_TextView_Confirmation);
        b_confPay=findViewById(R.id.Consultant_GetAppointment_Button_Confirm);

        dateRecyclerView=findViewById(R.id.Consultant_GetAppointment_RecyclerView_AppointmentDate);
        timeRecyclerView= findViewById(R.id.Consultant_GetAppointment_RecyclerView_AppointmentTime);

        slots = ConsultantProfile.appointments;
        setDateAdapter(slots);

        b_confPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmSlot(userAppointment);
            }
        });

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
        Picasso.get().load(pic_src).into(iv_profile);
    }

    private void confirmSlot(UserAppointments userAppointment) {
        if(true){
            orderID="sfioje090sjn";
//            Instamojo.getInstance().initiatePayment(this, orderID, this);
        }else{
            Toast.makeText(this, "Slot unavailable", Toast.LENGTH_LONG).show();
            //Refresh Available Slots
        }
    }


    public void showDates(View view) {
        setDateAdapter(slots);
    }

    private void setDateAdapter(ArrayList<Appointments> myList) {
        Log.i("SetDateAdapter:","Working");
        setOnDateClickListener();
        adapter = new Consultant_GetAppointment_Date_RecyclerAdapter(myList,listener);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager( getApplicationContext(), 4, LinearLayoutManager.VERTICAL, false);
        dateRecyclerView.setLayoutManager(layoutManager);
        dateRecyclerView.setItemAnimator(new DefaultItemAnimator());
        dateRecyclerView.setAdapter(adapter);
        Log.i("Adapter", "Executed");
    }

    public void showTime(View view) {
        setTimeAdapter(timings);
    }

    private void setTimeAdapter(ArrayList<String> timeList) {
        Log.i("SetTimeAdapter:","Working");
        setOnTimeClickListener();
        timeAdapter = new Consultant_GetAppointment_Time_RecyclerAdapter(timeList,timeListener);
        RecyclerView.LayoutManager layoutTManager = new GridLayoutManager( getApplicationContext(), 3, LinearLayoutManager.VERTICAL, false);
        timeRecyclerView.setLayoutManager(layoutTManager);
        timeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        timeRecyclerView.setAdapter(timeAdapter);
        Log.i("Adapter", "Executed");
    }

    private void setOnTimeClickListener() {
        timeListener= new Consultant_GetAppointment_Time_RecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
              //  userAppointment.setTime(timings.get(position));
                tv_time.setText(timings.get(position));
                setTimeAdapter(new ArrayList<String>());
                tv_confirmation.setText("Confirm appointment with "+name+" on "+aTime+", "+aMonth+" "+aYear+", at "+aTime+"? Click to Confirm & make Payment.");
            }
        };
    }

    private void setOnDateClickListener() {
        listener = new Consultant_GetAppointment_Date_RecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                timings=new ArrayList<>(Arrays.asList(slots.get(position).getTime()));
               // userAppointment = new UserAppointments(slots.get(position).getDate(), slots.get(position).getMonth(), slots.get(position).getMonth(), null,consultantName,consultant_id);
                tv_date.setText(slots.get(position).getDate()+", "+slots.get(position).getMonth()+" "+slots.get(position).getMonth());
                tv_time.setText("Tap to Select Time ");
                aTime=null;
                aMonth=slots.get(position).getMonth();
                aYear=slots.get(position).getYear();
                setDateAdapter(new ArrayList<Appointments>());
                setTimeAdapter(timings);
            }
        };
    }

//    @Override
//    public void onInstamojoPaymentComplete(String orderID, String transactionID, String paymentID, String paymentStatus) {
//        Log.d("INSTAMOJO PAYMENT: ", "Payment complete. Order ID: " + orderID + ", Transaction ID: " + transactionID
//                + ", Payment ID:" + paymentID + ", Status: " + paymentStatus);
//        paymentComplete();
//    }
//
//    private void paymentComplete() {
//    }
//
//    @Override
//    public void onPaymentCancelled() {
//        Log.d("INSTAMOJO PAYMENT: ", "Payment cancelled");
//        paymentCancelled();
//    }
//
//    private void paymentCancelled() {
//    }
//
//    @Override
//    public void onInitiatePaymentFailure(String errorMessage) {
//        Log.d("INSTAMOJO PAYMENT: ", "Initiate payment failed ::::: "+errorMessage);
//        paymentCancelled();
//    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public static class Appointments {

        private String date,month;
        private String year="2021";
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

}