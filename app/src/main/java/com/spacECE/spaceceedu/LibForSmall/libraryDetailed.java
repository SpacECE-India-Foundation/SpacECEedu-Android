package com.spacECE.spaceceedu.LibForSmall;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class libraryDetailed extends AppCompatActivity {

    TextView book, author, edition, desc, price, condition, owner;
    Button callbtn, addtocartbtn;
    String url = "http://43.205.45.96/libforsmall/api_addToCart.php";

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
        callbtn = findViewById(R.id.call_btn);
        addtocartbtn = findViewById(R.id.add_to_cart_btn);

        Intent intent = getIntent();
        int pos = intent.getIntExtra("pos", 1);
        books books = Library_main.list.get(pos);

        book.setText(books.getProduct_title());
        desc.setText(books.getProduct_desc());
        price.setText(books.getProduct_price());
        author.setText(books.getProduct_brand());

        addtocartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the account_id from UserLocalStore
                UserLocalStore userLocalStore = new UserLocalStore(libraryDetailed.this);
                Account account = userLocalStore.getLoggedInAccount();
                String accountId = account.getAccount_id();

                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(libraryDetailed.this, "Product added to cart Successfully", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(libraryDetailed.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("proId", books.getProduct_id());
                        params.put("status", "Borrow");
                        params.put("userId", accountId); // Pass the account ID as user ID
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(libraryDetailed.this);
                requestQueue.add(request);
            }
        });

    }
}