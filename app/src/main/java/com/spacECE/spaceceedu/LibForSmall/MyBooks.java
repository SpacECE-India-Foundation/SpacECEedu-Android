package com.spacECE.spaceceedu.LibForSmall;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spacECE.spaceceedu.Authentication.Account;
import com.spacECE.spaceceedu.Authentication.UserLocalStore;
import com.spacECE.spaceceedu.R;
import com.spacECE.spaceceedu.Utils.ConfigUtils;
import com.spacECE.spaceceedu.Utils.UsefulFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyBooks extends Fragment implements library_mybook_recyclerAdapter.OnItemRemovedListener,
        library_mybook_recyclerAdapter.OnTotalPriceChangeListener {

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
        adapter.setOnTotalPriceChangeListener(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false);
        mybooksrclview.setLayoutManager(layoutManager);
        mybooksrclview.setItemAnimator(new DefaultItemAnimator());
        mybooksrclview.setAdapter(adapter);

        totalPriceTxt = v.findViewById(R.id.total_price);

        // Retrieve accountId from local storage
        UserLocalStore userLocalStore = new UserLocalStore(requireContext());
        Account account = userLocalStore.getLoggedInAccount();
        String accountId = account.getAccount_id();

        // Fetch book data using accountId
        fetchBooksData(accountId);


        Button checkoutButton = v.findViewById(R.id.button_checkout);
        checkoutButton.setOnClickListener(view -> {
            ArrayList<String> bookNames = new ArrayList<>();
            ArrayList<String> bookQuantities = new ArrayList<>();
            ArrayList<String> bookPrices = new ArrayList<>();
            for (books2 book : list) {
                bookNames.add(book.getProduct_title());
                bookQuantities.add(book.getQuantity());
                bookPrices.add(String.valueOf(book.getItemTotalPrice()));
            }

            int totalPrice = calculateTotalPrice(list);
            OrderTrackingFragment orderTrackingFragment = OrderTrackingFragment.newInstance(bookNames, bookQuantities, bookPrices, totalPrice);

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.libs_for_small_fragment_container, orderTrackingFragment)
                    .addToBackStack(null)
                    .commit();
        });


        return v;
    }

    @Override
    public void onItemRemoved() {
        // No need to update here as handled in onTotalPriceChange
    }

    @Override
    public void onTotalPriceChange(int totalPrice) {
        totalPriceTxt.setText(String.valueOf(totalPrice));
    }

    private void fetchBooksData(String accountId) {
        new Thread(() -> {
            try {
                JSONObject config = ConfigUtils.loadConfig(getContext().getApplicationContext());
                if(config != null){
                    String baseUrl= config.getString("BASE_URL");
                    String libCartProductDataUrl = config.getString("LIB_CARTPRODUCTDATA");
                    // Construct the API URL with accountId as user_id parameter
                    String apiUrl = baseUrl + libCartProductDataUrl + accountId;


                    JSONObject apiCall = UsefulFunctions.UsingGetAPI(apiUrl);
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
                        requireActivity().runOnUiThread(() -> {
                            adapter.notifyDataSetChanged();
                            int totalPrice = calculateTotalPrice(list); // Calculate total price
                            totalPriceTxt.setText(String.valueOf(totalPrice));
                        });
                    } else {
                        String message = apiCall.getString("message");
                        Log.e("API Error", message);
                    }
                }
            } catch (JSONException e) {
                Log.e("JSON Error", e.getMessage(), e);
            } catch (Exception e) {
                Log.e("Exception", e.getMessage(), e);
            }
        }).start();
    }

    private int calculateTotalPrice(ArrayList<books2> booksList) {
        int totalPrice = 0;
        for (books2 book : booksList) {
            totalPrice += Integer.parseInt(book.getProduct_price()) * Integer.parseInt(book.getQuantity());
        }
        return totalPrice;
    }
}
