package com.spacECE.spaceceedu.VideoLibrary;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.spacECE.spaceceedu.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class VideoLibrary_Activity extends AppCompatActivity{

    ArrayList<Topic> recentTopicList = new ArrayList<>();

    private final int[] mImages = new int[]{
            R.drawable.view1, R.drawable.view2, R.drawable.view3,
            R.drawable.img
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_library);

        BottomNavigationView videoLibraryBottomNav = findViewById(R.id.VideoLibrary_Bottom_Navigation);
        videoLibraryBottomNav.setOnItemSelectedListener(VL_navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.VideoLibrary_Fragment_layout,
                    new VideoLibrary_Free()).commit();
        }

        CarouselView carouselView = findViewById(R.id.VideoLibrary_Carousel_RecentlyView);
        carouselView.setPageCount(mImages.length);


        //  getRecentList("http://3.109.14.4/SpacTube/api_getRecent?uid=1",carouselView);

        carouselView.setPageCount(recentTopicList.size());
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(mImages[position]);
                //Picasso.get().load("https://i.ytimg.com/vi/"+recentTopicList.get(position).getV_URL()+"/0.jpg").into(imageView);
            }
        });
        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getApplicationContext(), "Clicked!", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getApplicationContext(), TopicActivity.class);
//                intent.putExtra("acocunt_id",account_id);
//                intent.putExtra("topic_name", recentTopicList.get(position).getTitle());
//                intent.putExtra("v_url", recentTopicList.get(position).getV_URL());
//                intent.putExtra("discrp", recentTopicList.get(position).getDesc());
//                intent.putExtra("status",recentTopicList.get(position).getStatus());
//                intent.putExtra("v_id",recentTopicList.get(position).getV_id());
//                startActivity(intent);
            }
        });
    }

        NavigationBarView.OnItemSelectedListener VL_navListener =
                new NavigationBarView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;

                        switch (item.getItemId()) {
                            case R.id.videolibrary_nav_free:
                                selectedFragment = new VideoLibrary_Free();
                                break;
                            case R.id.videolibrary_nav_paid:
                                selectedFragment = new VideoLibrary_Premium();
                                break;
                        }

                        getSupportFragmentManager().beginTransaction().replace(R.id.VideoLibrary_Fragment_layout,
                                selectedFragment).commit();

                        return true;
                    }
                };

//    private void getRecentList(String URL, CarouselView carouselView) {
//        final JSONObject[] response = {null};
//
//        response[0] = ApiFunctions.UsingGetAPI(URL);
//        Log.i("Returned Object : ",response.toString());
//
//        JSONArray jsonArray= null;
//
//        try {
//            jsonArray = response[0].getJSONArray("data");
//        } catch (JSONException e) {
//            Log.i(" Response : "," Requested Array not found in response");
//            e.printStackTrace();
//        }
//
//        for (int i = 0; i < jsonArray.length(); i++) {
//
//            JSONObject response_element = null;
//            try {
//                response_element = new JSONObject(String.valueOf(jsonArray.getJSONObject(i)));
//
//                Topic newTopic = null;
//
//                    newTopic = new Topic( response_element.getString("v_id"),
//                            response_element.getString("title"), response_element.getString("v_id"),
//                            response_element.getString("filter"), response_element.getString("length"),
//                            response_element.getString("v_url"), response_element.getString("v_date"),
//                            response_element.getString("v_uni_no"),response_element.getString("desc"));
//                    recentTopicList.add(newTopic);
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Log.i("JSON Object: ","Some key(s) could not be found");
//            }
//        }
//        Log.i("VIDEOS:::::",recentTopicList.toString());

//

  }