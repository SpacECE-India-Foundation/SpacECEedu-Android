package com.spacECE.spaceceedu.LearnOnApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.instamojo.android.Instamojo;
import com.spacECE.spaceceedu.MainActivity;
import com.spacECE.spaceceedu.R;

public class Payment extends AppCompatActivity implements Instamojo.InstamojoPaymentCallback {

    static String orderID = "0169c0a4-342c-4040-9537-9f7d94c86553";



    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        Instamojo.getInstance().initialize(this, Instamojo.Environment.TEST);
        Instamojo.getInstance().initiatePayment(this, orderID, this);

        Button exit = findViewById(R.id.button);

        exit.setText("Go Home");

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });


    }


    @Override
    public void onInstamojoPaymentComplete(String orderID, String transactionID, String paymentID, String paymentStatus) {
        Log.d("TAG", "Payment complete. Order ID: " + orderID + ", Transaction ID: " + transactionID
                + ", Payment ID:" + paymentID + ", Status: " + paymentStatus);
    }


    @Override
    public void onPaymentCancelled() {

    }

    @Override
    public void onInitiatePaymentFailure(String s) {

    }
}
