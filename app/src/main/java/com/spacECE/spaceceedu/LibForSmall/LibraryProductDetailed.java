package com.spacECE.spaceceedu.LibForSmall;

import android.os.Bundle;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class LibraryProductDetailed extends Fragment {

    private TextView book, author, edition, desc, price, condition;
    private Button addtocartbtn;
    private ImageView productImg;
    private String baseUrl ="";
    private String libAddtoCartUrl ="";
    private int pos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library_product_detailed, container, false);

        book = view.findViewById(R.id.book_name);
        author = view.findViewById(R.id.author_name);
        edition = view.findViewById(R.id.edition_name);
        desc = view.findViewById(R.id.desc_name);
        price = view.findViewById(R.id.price_name);
        condition = view.findViewById(R.id.condition_name);
        addtocartbtn = view.findViewById(R.id.add_to_cart_btn);
        productImg = view.findViewById(R.id.lfs_product_img);


        // Get arguments passed to the fragment
        if (getArguments() != null) {
            pos = getArguments().getInt("pos", 1);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            getActivity().getWindow().setStatusBarColor(Color.rgb(200, 100, 50));
        }

        // Set data to views
        books books = Library_main.list.get(pos);

        book.setText(books.getProduct_title());
        desc.setText(books.getProduct_desc());
        price.setText(books.getProduct_price());
        author.setText(books.getProduct_brand());
        try {
            JSONObject config = ConfigUtils.loadConfig(getContext().getApplicationContext());
            if (config != null) {
                baseUrl= config.getString("BASE_URL");
                String libProductimgUrl = config.getString("LIB_PRODUCTIMG");
                libAddtoCartUrl = config.getString("LIB_ADDTOCART");


                Picasso.get()
                        .load(baseUrl+ libProductimgUrl + books.getProduct_image())
                        .error(R.drawable.tile_icon_2)
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
                RequestQueue queue = Volley.newRequestQueue(getActivity());

                // Retrieve the account_id from UserLocalStore
                UserLocalStore userLocalStore = new UserLocalStore(getActivity());
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
                                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Show error or warning message
                                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(), "JSON Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(getActivity(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
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
