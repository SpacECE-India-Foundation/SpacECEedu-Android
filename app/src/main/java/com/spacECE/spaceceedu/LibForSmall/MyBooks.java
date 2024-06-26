package com.spacECE.spaceceedu.LibForSmall;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Objects;

public class MyBooks extends Fragment implements library_mybook_recyclerAdapter.OnItemRemovedListener {

    private RecyclerView mybooksrclview;
    private ArrayList<books2> list;
    private library_mybook_recyclerAdapter adapter;
    private TextView totalPriceTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_books, container, false);
        v.setBackgroundColor(Color.WHITE);

        Window window = requireActivity().getWindow();
        window.setStatusBarColor(Color.rgb(200, 100, 50));

        mybooksrclview = v.findViewById(R.id.recyclerview_cart);

        list = new ArrayList<>();
        adapter = new library_mybook_recyclerAdapter(getContext(), list);
        adapter.setOnItemRemovedListener(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, LinearLayoutManager.VERTICAL, false);
        mybooksrclview.setLayoutManager(layoutManager);
        mybooksrclview.setItemAnimator(new DefaultItemAnimator());
        mybooksrclview.setAdapter(adapter);

        totalPriceTxt = v.findViewById(R.id.total_price);

        fetchBooksData();

        return v;
    }

    @Override
    public void onItemRemoved() {
        int totalPrice = adapter.getTotalPrice();
        totalPriceTxt.setText(String.valueOf(totalPrice));
    }

    private void fetchBooksData() {
        new Thread(() -> {
            try {
                JSONObject apiCall = UsefulFunctions.UsingGetAPI("http://43.205.45.96/libforsmall/api_fetchCartProducts.php");
                Log.i("API Response", apiCall.toString());

                if (apiCall.has("status") && apiCall.getString("status").equals("success")) {
                    JSONArray jsonArray = apiCall.getJSONArray("cart");
                    Log.i("API Data Array", jsonArray.toString());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject responseElement = jsonArray.getJSONObject(i);
                        books2 temp = new books2(
                                responseElement.getString("cart_id"),
                                responseElement.getString("product_id"),
                                responseElement.getString("user_id"),
                                responseElement.getString("quantity"),
                                responseElement.getString("status"),
                                responseElement.getString("exchange_product"),
                                responseElement.getString("exchange_price"),
                                responseElement.getString("product_title"),
                                responseElement.getString("product_quantity"),
                                responseElement.getString("product_price"),
                                responseElement.getString("product_brand"),
                                responseElement.getString("product_image"),
                                responseElement.getString("product_cat"));
                        list.add(temp);
                    }

                    // Notify adapter on the UI thread
                    getActivity().runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                        int totalPrice = adapter.getTotalPrice();
                        totalPriceTxt.setText(String.valueOf(totalPrice));
                    });
                } else {
                    String message = apiCall.getString("message");
                    Log.e("API Error", message);
                }
            } catch (JSONException e) {
                Log.e("JSON Error", e.getMessage(), e);
            } catch (RuntimeException runtimeException) {
                Log.e("Runtime Exception", "Server did not respond", runtimeException);
            }
        }).start();
    }
}