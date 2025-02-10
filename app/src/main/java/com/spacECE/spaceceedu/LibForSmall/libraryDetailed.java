package com.spacECE.spaceceedu.LibForSmall;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.spacECE.spaceceedu.Authentication.UserLocalStore;
import com.spacECE.spaceceedu.Authentication.Account;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.ConfigUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class libraryDetailed extends AppCompatActivity {

    TextView book, author, edition, desc, price, condition, owner;
    Button callbtn, addtocartbtn;
    ImageView productImg;

    String baseUrl = "";
    String libAddtoCartUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_detailed);

        Window window = this.getWindow();
        window.setStatusBarColor(Color.rgb(200, 100, 50));

        book = findViewById(R.id.book_name);
        author = findViewById(R.id.author_name);
        edition = findViewById(R.id.edition_name);
        desc = findViewById(R.id.desc_name);
        price = findViewById(R.id.price_name);
        condition = findViewById(R.id.condition_name);
        owner = findViewById(R.id.owner_name);
        addtocartbtn = findViewById(R.id.add_to_cart_btn);
        productImg= findViewById(R.id.lfs_product_img);

        Intent intent = getIntent();
        int pos = intent.getIntExtra("pos", 1);
        books books = Library_main.list.get(pos);

        book.setText(books.getProduct_title());
        desc.setText(books.getProduct_desc());
        price.setText(books.getProduct_price());
        author.setText(books.getProduct_brand());
        try {
            JSONObject config = ConfigUtils.loadConfig(getApplicationContext());
            if (config != null) {
                baseUrl= config.getString("BASE_URL");
                libAddtoCartUrl = config.getString("LIB_ADDTOCART");
                String libProductimgUrl = config.getString("LIB_PRODUCTIMG");


                Picasso.get()
                        .load(baseUrl + libProductimgUrl + books.getProduct_image())
                        .into(productImg);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i("ERROR:::", "Failed to load API URLs");
        }

        addtocartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(libraryDetailed.this);

                // Retrieve the account_id from UserLocalStore

                UserLocalStore userLocalStore = new UserLocalStore(libraryDetailed.this);
                Account account = userLocalStore.getLoggedInAccount();
                String accountId = account.getAccount_id();

                // Prepare the POST request
                StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl+libAddtoCartUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Handle response from PHP backend
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    String status = jsonResponse.getString("status");
                                    String message = jsonResponse.getString("message");

                                    // Handle success or failure based on status
                                    if (status.equals("success")) {
                                        // Show success message or handle further actions
                                        Toast.makeText(libraryDetailed.this, message, Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Show error or warning message
                                        Toast.makeText(libraryDetailed.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(libraryDetailed.this, "JSON Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(libraryDetailed.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        // Parameters to be sent in POST request
                        Map<String, String> params = new HashMap<>();
                        params.put("proId", books.getProduct_id()); // Replace with your actual product ID
                        params.put("status", "Buy"); // Example status, modify as needed
                        params.put("user_id", accountId); // Replace with actual user ID retrieval method

                        // Example of adding optional parameter
                        // params.put("end_date", "2024-07-01");

                        return params;
                    }
                };

                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });


    }
}
