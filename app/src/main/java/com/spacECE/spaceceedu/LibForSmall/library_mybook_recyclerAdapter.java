package com.spacECE.spaceceedu.LibForSmall;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.Authentication.Account;
import com.spacECE.spaceceedu.Authentication.UserLocalStore;
import com.spacECE.spaceceedu.R;
import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class library_mybook_recyclerAdapter extends RecyclerView.Adapter<library_mybook_recyclerAdapter.MyViewHolder> {

    private final ArrayList<books2> list;
    private final Context context;
    private OnItemRemovedListener onItemRemovedListener;

    public interface OnItemRemovedListener {
        void onItemRemoved();
    }

    public void setOnItemRemovedListener(OnItemRemovedListener listener) {
        this.onItemRemovedListener = listener;
    }

    public library_mybook_recyclerAdapter(Context context, ArrayList<books2> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_my_book, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        books2 book = list.get(position);
        holder.bookName.setText(book.getProduct_title());
        holder.bookPrice.setText(book.getProduct_price());
        holder.bookCategory.setText(book.getStatus());
        Picasso.get()
                .load("http://43.205.45.96/libforsmall/product_images/"+book.product_image)
                .into(holder.bookImage);
        // Set click listener for remove button
        holder.removeBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setMessage("Are you sure you want to remove this?")
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        Log.e("Tagp", book.product_id);
                        removeItemFromDatabase(book.getProduct_id(),context);

                        // Get the item position
                        int adapterPosition = holder.getAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            // Remove the item from the list
                            list.remove(adapterPosition);
                            notifyItemRemoved(adapterPosition);
                            notifyItemRangeChanged(adapterPosition, list.size());

                            // Notify the listener
                            if (onItemRemovedListener != null) {
                                onItemRemovedListener.onItemRemoved();
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, (dialog, which) ->
                            Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (books2 book : list) {
            totalPrice += Integer.parseInt(book.getProduct_price());
        }
        return totalPrice;
    }

    private void removeItemFromDatabase(String productId, Context context) {
        // Initialize UserLocalStore
        UserLocalStore userLocalStore = new UserLocalStore(context);

        // Get the logged-in account
        Account account = userLocalStore.getLoggedInAccount();
        String userId = account.getAccount_id();

        new Thread(() -> {
            try {
                // Define the API URL
                URL url = new URL("http://43.205.45.96/libforsmall/api_RemoveProductFromCart.php");

                // Create the JSON object to send
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("proId", productId);
                jsonObject.put("user_id", userId); // Add userId to the JSON payload
                Log.e("JSON Payload", jsonObject.toString());

                // Open connection
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                // Write the JSON data to the request
                OutputStream os = conn.getOutputStream();
                os.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
                os.close();

                // Get the response code
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the response
                    InputStream is = conn.getInputStream();
                    StringBuilder response = new StringBuilder();
                    int ch;
                    while ((ch = is.read()) != -1) {
                        response.append((char) ch);
                    }
                    is.close();

                    // Extract the JSON part from the response
                    String responseStr = response.toString();
                    int jsonStartIndex = responseStr.indexOf('{');
                    int jsonEndIndex = responseStr.lastIndexOf('}') + 1;

                    if (jsonStartIndex != -1) {
                        String jsonResponseStr = responseStr.substring(jsonStartIndex, jsonEndIndex);
                        try {
                            JSONObject responseJson = new JSONObject(jsonResponseStr);
                            if (responseJson.has("status") && responseJson.getString("status").equals("success")) {
                                Log.i("API Response", "Product removed from cart successfully.");
                            } else {
                                String message = responseJson.getString("message");
                                Log.e("API Error", message);
                            }
                        } catch (JSONException e) {
                            Log.e("JSON Error", "Error parsing JSON response: " + e.getMessage());
                        }
                    } else {
                        Log.e("API Response", "Unexpected response format: " + responseStr);
                    }
                } else {
                    Log.e("HTTP Error", "Response Code: " + responseCode);
                }
            } catch (JSONException e) {
                Log.e("JSON Error", e.getMessage(), e);
            } catch (IOException e) {
                Log.e("IO Error", e.getMessage(), e);
            }
        }).start();
    }





    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView bookName;
        private final ImageView bookImage;
        private final TextView bookPrice;
        private final TextView bookCategory;
        private final TextView removeBtn; // Button for removing item

        public MyViewHolder(@NonNull View view) {
            super(view);
            bookName = view.findViewById(R.id.mybooks_txtname);
            bookImage = view.findViewById(R.id.mybooks_image);
            bookCategory = view.findViewById(R.id.mybooks_cat);
            bookPrice = view.findViewById(R.id.price_txt);
            removeBtn = view.findViewById(R.id.remove_btn);
        }
    }
}