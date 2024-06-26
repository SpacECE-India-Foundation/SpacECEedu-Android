package com.spacECE.spaceceedu.LibForSmall;

import android.app.Activity;
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
                        removeItemFromDatabase(book.getCart_id(), position); // Pass position here

                        // No need to remove item here
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

    private void removeItemFromDatabase(String cart_id,int position) {
        // Make API call to remove item from database
        new Thread(() -> {
            try {
                URL url = new URL("http://43.205.45.96/libforsmall/api_RemoveProductFromCart.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                // Prepare data to send
                OutputStream os = conn.getOutputStream();
                String params = "cart_id=" + URLEncoder.encode(cart_id, "UTF-8");
                os.write(params.getBytes());
                os.flush();
                os.close();

                // Read response
                InputStream inputStream = conn.getInputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                StringBuilder response = new StringBuilder();
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    response.append(new String(buffer, 0, bytesRead));
                }
                inputStream.close();

                // Handle response on UI thread if necessary
                ((Activity) context).runOnUiThread(() -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response.toString());
                        String status = jsonResponse.optString("status");
                        String message = jsonResponse.optString("message");

                        if (status.equals("success")) {
                            // Remove item from the list
                            list.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, list.size());

                            // Notify listener
                            if (onItemRemovedListener != null) {
                                onItemRemovedListener.onItemRemoved();
                            }

                            // Show success message
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        } else {
                            // Show error message
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Failed to parse response", Toast.LENGTH_SHORT).show();
                    }
                });

                conn.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
                ((Activity) context).runOnUiThread(() ->
                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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