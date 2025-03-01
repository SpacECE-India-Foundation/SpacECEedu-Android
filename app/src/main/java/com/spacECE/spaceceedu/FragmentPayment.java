package com.spacECE.spaceceedu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;


public class FragmentPayment extends Fragment implements PaymentResultListener {

    RadioButton oneMonthPlan;
    RadioButton threeMonthPlan;
    RadioButton oneYearPlan;
    Button upgradeButton;
    ImageButton backButton;
    TextView selectText;
    Toolbar toolbar;

    public FragmentPayment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        oneMonthPlan = view.findViewById(R.id.OneMonthPlan);
        threeMonthPlan = view.findViewById(R.id.ThreeMonthPlan);
        oneYearPlan = view.findViewById(R.id.yearlyPlan);
        upgradeButton = view.findViewById(R.id.upgradeButton);
        selectText = view.findViewById(R.id.textselected);
        backButton = view.findViewById(R.id.backButton);

        if (getActivity() != null && getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().hide();
            }
        }

        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Plan & Invoices");
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24);
            }
        }

        backButton.setOnClickListener(v -> {
            // Check if the activity is not null
            if (getActivity() != null) {
                // Create an Intent to navigate to the HomeActivity
                Intent intent = new Intent(getActivity(), MainActivity.class);
                // Clear the activity stack so the user cannot return to the current fragment
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                // Optionally finish the current activity if desired
                getActivity().finish();
            }
        });


        oneMonthPlan.setOnClickListener(v -> {
            if (oneMonthPlan.isChecked()) {
                threeMonthPlan.setChecked(false);
                oneYearPlan.setChecked(false);
                selectText.setText("499");

                //String selectedPlan = oneMonthPlan.getText().toString();
                //Toast.makeText(getContext(), "Selected plan is " + selectedPlan, Toast.LENGTH_SHORT).show();
            }
        });

        threeMonthPlan.setOnClickListener(v -> {
            if (threeMonthPlan.isChecked()) {
                oneMonthPlan.setChecked(false);
                oneYearPlan.setChecked(false);
                selectText.setText("416.67");
                //String selectedPlan = threeMonthPlan.getText().toString();
                //Toast.makeText(getContext(), "Selected plan is " + selectedPlan, Toast.LENGTH_SHORT).show();
            }
        });

        oneYearPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oneYearPlan.isChecked()) {
                    oneMonthPlan.setChecked(false);
                    threeMonthPlan.setChecked(false);
                    selectText.setText("158.25");
                    //String selectedPlan = oneYearPlan.getText().toString();
                    //Toast.makeText(getContext(), "Selected plan is " + selectedPlan, Toast.LENGTH_SHORT).show();
                }
            }
        });

        upgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedPlan = null;

                if (oneMonthPlan.isChecked()) {
                    startPayment(499);
                } else if (threeMonthPlan.isChecked()) {
                    startPayment(416);
                } else if (oneYearPlan.isChecked()) {
                    startPayment(158);
                }
                else{
                    Toast.makeText(getContext(), "Please select a plan before upgrading.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

    // Call this method to start the payment process
    private void startPayment(int totalPrice) {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_KQpgNv8PbMeQk1");
        try {
            JSONObject options = new JSONObject();

            options.put("name", "SpacECEedu");
            options.put("description", "Education Platform");
            options.put("image", "http://example.com/image/rzp.jpg");
            //   options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#EAAE15");
            options.put("currency", "INR");
            options.put("amount", totalPrice * 100);//pass amount in currency subunits
            options.put("prefill.email", "spaceece@gmail.com");
            options.put("prefill.contact","9988776655");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(requireActivity(), options);

        } catch(Exception e) {
            Log.e("Checkout Error", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getContext(), "Payment Successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getContext(), "Payment Failed!, Try again", Toast.LENGTH_SHORT).show();
    }



}
