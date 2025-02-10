package com.spacECE.spaceceedu.LearnOnApp;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.util.Log;
import android.widget.Toast;

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
import java.util.Objects;

public class LearnOn_MyCourses extends Fragment {

    private RecyclerView mycoursesRecyclerView;
    private ArrayList<Learn> list;
    private LearnOn_List_RecycleAdapter_MyCourse adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_learn_on__list, container, false);
        v.setBackgroundColor(Color.WHITE);

        Window window = requireActivity().getWindow();
        window.setStatusBarColor(Color.rgb(200, 100, 50));

        UserLocalStore userLocalStore = new UserLocalStore(getContext());
        Account account = userLocalStore.getLoggedInAccount();

        mycoursesRecyclerView = v.findViewById(R.id.LearnOn_List_RecyclerView);

        list = new ArrayList<>();
        adapter = new LearnOn_List_RecycleAdapter_MyCourse(list, new LearnOn_List_RecycleAdapter_MyCourse.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                // Handle click events here
            }
        });

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false);
        mycoursesRecyclerView.setLayoutManager(layoutManager);
        mycoursesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mycoursesRecyclerView.setAdapter(adapter);
//        list.add(new Learn("1", "Sample Course", "Description", "Type", "Mode", "10", "$100"));
//        list.add(new Learn("2", "Another Course", "Description", "Type", "Mode", "20", "$200"));
//        adapter.notifyDataSetChanged();
        String accountId = account.getAccount_id();
        fetchCoursesData(accountId);

        return v;
    }

    // Fetches course data
    private void fetchCoursesData(String accountId) {
        new Thread(() -> {
            try {
                JSONObject config = ConfigUtils.loadConfig(getContext().getApplicationContext());
                if (config != null) {
                    String baseUrl= config.getString("BASE_URL");
                    String learnCourseFetchCourseUrl = config.getString("LEARNCOURSE_FETCHCOURSEDATA");

                    // Construct the API URL with accountId as user_id parameter
                    String apiUrl = baseUrl + learnCourseFetchCourseUrl + accountId;
                    Log.d("API URL", apiUrl);
                    System.out.println(apiUrl);


                    JSONObject apiCall = UsefulFunctions.UsingGetAPI(apiUrl);
                    Log.d("API Response", apiCall.toString());

                    if (apiCall.has("status") && apiCall.getString("status").equals("success")) {
                        JSONArray jsonArray = apiCall.getJSONArray("data");
                        Log.d("API Data Array", jsonArray.toString());

                        list.clear(); // Clear the list before adding new items

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject responseElement = jsonArray.getJSONObject(i);
                            Learn temp = new Learn(
                                    responseElement.getString("id"),
                                    responseElement.getString("title"),
                                    responseElement.getString("description"),
                                    responseElement.getString("type"),
                                    responseElement.getString("mode"),
                                    responseElement.getString("duration"),
                                    responseElement.getString("price")
                            );
                            list.add(temp);
                        }

                        // Notify adapter on the UI thread
                        requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
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
}