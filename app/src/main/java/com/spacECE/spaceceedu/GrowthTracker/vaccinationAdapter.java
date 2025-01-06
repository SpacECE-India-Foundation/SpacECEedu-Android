package com.spacECE.spaceceedu.GrowthTracker;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.spacECE.spaceceedu.Authentication.Account;
import com.spacECE.spaceceedu.Authentication.UserLocalStore;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.ConfigUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class vaccinationAdapter extends RecyclerView.Adapter<vaccinationAdapter.MyViewHolder> {

    private final List<ItemModel> itemList;
    private final Context context;

    public vaccinationAdapter(List<ItemModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vaccination_item_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemModel item = itemList.get(position);
        holder.protectsAgainst.setText(item.getProtectsAgainst());
        holder.vaccineName.setText(item.getVaccineName());

        holder.itemView.setOnClickListener(v -> showVaccinationDialog(item));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView protectsAgainst;
        TextView vaccineName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            protectsAgainst = itemView.findViewById(R.id.protects_against);
            vaccineName = itemView.findViewById(R.id.vaccine_name);

        }

    }

    private void showVaccinationDialog(ItemModel item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Has your child received the " + item.getVaccineName() + " vaccine?")
                //Getting null in item.getInfo() from API
//        builder.setMessage("Has your child received the " + item.getVaccineName() + " vaccine?" + "\n" + "(" + item.getInfo() + ")" )
                .setPositiveButton("Yes", (dialog, id) -> {
                    // Handle Yes option
                    showDoseNumberDialog(item);
                })
                .setNegativeButton("No", (dialog, id) -> {
                    // Handle No option
                    updateVaccinationStatus(item, "not vaccinated", 1, "0"); // Pass 0 as age for "not vaccinated" case
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void updateVaccinationStatus(ItemModel item, String status, int doseNumber, String age) {
        try {
            JSONObject config = ConfigUtils.loadConfig(context.getApplicationContext());
            if (config != null) {
                String baseUrl= config.getString("BASE_URL");
                String growthInsertVaccineDataUrl = config.getString("GROWTH_INSERTVACCINEDATA");
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = baseUrl+growthInsertVaccineDataUrl;
                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        response -> {
                            // Handle the response from your PHP API
                            Log.d("SaveDataResponse", response);
                            // Optionally handle response based on your needs
                        },
                        error -> {
                            // Handle error
                            Log.e("SaveDataError", "Error while saving data: " + error.toString());
                        }) {

                    @Override
                    protected Map<String, String> getParams() {
                        // Retrieve accountId from local storage
                        UserLocalStore userLocalStore = new UserLocalStore(context);
                        Account account = userLocalStore.getLoggedInAccount();
                        String accountId = account.getAccount_id();
                        int vaccineId = Integer.parseInt(item.getVaccineId()); // Get vaccine ID from item model


                        Map<String, String> params = new HashMap<>();
                        params.put("u_id", String.valueOf(accountId));
                        params.put("vaccine_id", String.valueOf(vaccineId));
                        params.put("dose_number", String.valueOf(doseNumber));
                        params.put("age", age);
                        params.put("status", String.valueOf(status));


                        return params;
                    }
                };

                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i("ERROR:::", "Failed to load API URLs");
        }
    }




    private void showDoseNumberDialog(ItemModel item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Number of doses");

        // Create a vertical LinearLayout to hold the input fields
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        // Create EditText for number of doses input
        final EditText doseInput = new EditText(context);
        doseInput.setHint("Enter number of doses");
        doseInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(doseInput);

        // Create EditText for age input
        final EditText ageInput = new EditText(context);
        ageInput.setHint("Enter age");
        ageInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(ageInput);

        // Create Spinner for age unit (months/years)
        final Spinner ageUnitSpinner = new Spinner(context);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new String[]{"Months", "Years"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageUnitSpinner.setAdapter(adapter);
        layout.addView(ageUnitSpinner);

        builder.setView(layout);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String doseText = doseInput.getText().toString();
            String ageText = ageInput.getText().toString();
            String ageUnit = ageUnitSpinner.getSelectedItem().toString();

            if (!doseText.isEmpty() && !ageText.isEmpty()) {
                int doses = Integer.parseInt(doseText);
                for (int doseNumber = 1; doseNumber <= doses; doseNumber++) {
                    updateVaccinationStatus(item, "vaccinated", doseNumber, ageText + " " + ageUnit);
                }
            } else {
                Toast.makeText(context, "Please enter valid number of doses and age", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();

        //by Mohit
    }
}