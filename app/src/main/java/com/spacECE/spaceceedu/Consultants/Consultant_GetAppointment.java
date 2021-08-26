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
import android.widget.TextView;
import android.widget.Toast;

import com.spacECE.spaceceedu.Consultants.ConsultantProfile;
import com.spacECE.spaceceedu.Consultants.Consultant_GetAppointment_Date_RecyclerAdapter;
import com.spacECE.spaceceedu.Consultants.Consultant_GetAppointment_Time_RecyclerAdapter;
import com.spacECE.spaceceedu.R;

import java.util.ArrayList;
import java.util.Arrays;

public class Consultant_GetAppointment extends AppCompatActivity {

    ArrayList<Appointments> slots = new ArrayList<>();
    ArrayList<String> timings = new ArrayList<>();
    String consultant_id= "Unknown";
    String consultantName="Unknown";
    String orderID = null;
    UserAppointments userAppointment = new UserAppointments(null, null, null,null,null,null);


    private Consultant_GetAppointment_Date_RecyclerAdapter adapter;
    private Consultant_GetAppointment_Time_RecyclerAdapter timeAdapter;
    private Consultant_GetAppointment_Date_RecyclerAdapter.RecyclerViewClickListener listener;
    private Consultant_GetAppointment_Time_RecyclerAdapter.RecyclerViewClickListener timeListener;
    private RecyclerView dateRecyclerView;
    private RecyclerView timeRecyclerView;

    private TextView tv_date;
    private TextView tv_time;
    private TextView tv_confirmation;
    private Button b_confPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultant_get_appointment);

//        Instamojo.getInstance().initialize(this, Instamojo.Environment.TEST);


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
                userAppointment.setTime(timings.get(position));
                tv_time.setText(timings.get(position));
                setTimeAdapter(new ArrayList<String>());
                tv_confirmation.setText("Confirm appointment with "+userAppointment.name+" on "+userAppointment.date+", "+userAppointment.month+" at "+userAppointment.time+"? Click to Confirm & make Payment.");
            }
        };
    }

    private void setOnDateClickListener() {
        listener = new Consultant_GetAppointment_Date_RecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                timings=new ArrayList<>(Arrays.asList(slots.get(position).getTime()));
                userAppointment = new UserAppointments(slots.get(position).getDate(), slots.get(position).getMonth(), slots.get(position).getMonth(), null,consultantName,consultant_id);
                tv_date.setText(slots.get(position).getDate()+", "+slots.get(position).getMonth()+" "+slots.get(position).getMonth());
                tv_time.setText("Tap to Select Time ");
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
        private String date;
        private String month;
        private String[] time;

        public String getDate() {
            return date;
        }

        public String getMonth() {
            return month;
        }

        public String[] getTime() {
            return time;
        }

        public Appointments(String date, String month, String[] time) {
            this.date = date;
            this.month = month;
            this.time = time;
        }
    }

    public class UserAppointments {
        private String date, month, year, time, name, consultant_id;

        public UserAppointments(String date, String month, String year, String time, String name, String consultant_id) {
            this.date = date;
            this.month = month;
            this.year = year;
            this.time = time;
            this.name = name;
            this.consultant_id = consultant_id;
        }
        public void setTime(String time) {
            this.time = time;
        }
    }
}