package com.spacECE.spaceceedu.LearnOnApp;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.instamojo.android.Instamojo;
import com.spacECE.spaceceedu.Authentication.Account;
import com.spacECE.spaceceedu.Authentication.UserLocalStore;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.ConfigUtils;

import org.json.JSONObject;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;

import static com.spacECE.spaceceedu.LearnOnApp.LearnOn_List_RecycleAdapter.orderID;

public class LearnDetailed extends AppCompatActivity implements Instamojo.InstamojoPaymentCallback {

    TextView Title, Description, Duration, Price, Mode_Type;
    Button Buy;
    String userId;
    String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_detailed);

        Window window = this.getWindow();
        window.setStatusBarColor(Color.rgb(200, 100, 50));

        Title = findViewById(R.id.Detail_Title);
        Description = findViewById(R.id.Detail_Description);
        Duration = findViewById(R.id.ShowCourseStartingDate);
        Price = findViewById(R.id.ShowCoursePrice);
        Mode_Type = findViewById(R.id.Detail_Mode_Type);
        Buy = findViewById(R.id.Buy);

        UserLocalStore userLocalStore = new UserLocalStore(this);
        Account account = userLocalStore.getLoggedInAccount();
        userId = account.getAccount_id();

        Intent intent = getIntent();
        int pos = intent.getIntExtra("pos", 1);
        Learn learn = LearnOn_Main.Llist.get(pos);
        courseId = learn.getId(); // Assuming Learn class has getId() method

        Title.setText(learn.getTitle());
        Description.setText(learn.getDescription());
        Duration.setText(learn.getDuration());
        Price.setText(learn.getPrice());
        Mode_Type.setText(MessageFormat.format("{0} on {1}", learn.getMode(), learn.getType()));

        Buy.setOnClickListener(v -> {
            new CallAPI().execute();
            Instamojo.getInstance().initialize(LearnDetailed.this, Instamojo.Environment.TEST);
            Instamojo.getInstance().initiatePayment(LearnDetailed.this, orderID, LearnDetailed.this);

        });
    }

    @Override
    public void onInstamojoPaymentComplete(String orderID, String transactionID, String paymentID, String paymentStatus) {
        Log.d("TAG", "Payment complete. Order ID: " + orderID + ", Transaction ID: " + transactionID
                + ", Payment ID:" + paymentID + ", Status: " + paymentStatus);
    }

    @Override
    public void onPaymentCancelled() {
        Toast.makeText(this, "Payment Cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInitiatePaymentFailure(String s) {
        Toast.makeText(this, "Payment Initiation Failed: " + s, Toast.LENGTH_SHORT).show();
    }

    private class CallAPI extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
                if(config != null) {
                    String baseUrl= config.getString("BASE_URL");
                    String learnCourseInsertDataUrl = config.getString("LEARNCOURSE_INSERTDATA");
                    URL url = new URL(baseUrl+learnCourseInsertDataUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setDoOutput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("uid", userId);
                    jsonParam.put("cid", courseId);
                    jsonParam.put("payment_status", "initiated");
                    jsonParam.put("payment_details", orderID);

                    OutputStream os = new BufferedOutputStream(conn.getOutputStream());
                    os.write(jsonParam.toString().getBytes());
                    os.flush();
                    os.close();

                    // Log request data
                    Log.d("API_REQUEST", jsonParam.toString());

                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String output;
                    while ((output = br.readLine()) != null) {
                        sb.append(output);
                    }

                    conn.disconnect();

                    // Log response data
                    Log.d("API_RESPONSE", sb.toString());

                    return sb.toString();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            // Return null or a default value if something goes wrong
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                Toast.makeText(LearnDetailed.this, "API Response: " + result, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LearnDetailed.this, "Failed to call API", Toast.LENGTH_SHORT).show();
            }
        }
    }
}