package com.spacECE.spaceceedu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.spacECE.spaceceedu.Authentication.Account;
import com.spacECE.spaceceedu.Consultants.ConsultantCategory;
import com.spacECE.spaceceedu.Consultants.Consultant_Main;
import com.spacECE.spaceceedu.VideoLibrary.VideoLibrary_Activity;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentMain extends Fragment {

    static Account account ;

    Button sign_signUp;
    Button signOut;
    private final int[] mImages = new int[]{
            R.drawable.view1, R.drawable.view2, R.drawable.view3,
            R.drawable.img
    };


    CardView cv_videoLibrary;
    CardView cv_consultation;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_main,container,false);

        CarouselView carouselView = v.findViewById(R.id.MainFragement_NewsCarousel);
        carouselView.setPageCount(mImages.length);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(mImages [position]);
            }
        });

        //Navigating to VideoLibrary/Consultation activity via OnClick
        cv_consultation=v.findViewById(R.id.CardView_Consultation);
        cv_videoLibrary=v.findViewById(R.id.CardView_VideoLibrary);

        cv_videoLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent =new Intent(getContext(), VideoLibrary_Activity.class);
                myIntent.putExtra("account_id","2");
                startActivity(myIntent);
            }
        });

        cv_consultation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateMyCatList();
                Intent intent = new Intent(getContext(), Consultant_Main.class);
                startActivity(intent);
            }
        });
    return v;
    }
    public void generateMyCatList() {
        final boolean[] COMPLETED = {false};
        final JSONObject[] apiCall = {null};

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try{
                    apiCall[0] = UsefulFunctions.UsingGetAPI("http://educationfoundation.space/ConsultUs/api_category?category=all");

                    JSONArray jsonArray = null;
                    try {
                        jsonArray = apiCall[0].getJSONArray("data");
                        Log.i("API : ", jsonArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ConsultantCategory newCategory = new ConsultantCategory((String) jsonArray.get(i), "nice");

                            Consultant_Main.categoryList.add(newCategory);
                        }
                        COMPLETED[0] = true;
                        Log.i("My CATEGORIES:::::", Consultant_Main.categoryList.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }   catch (RuntimeException runtimeException){
                    Log.i("RUNTIME EXCEPTION:::", "Server did not respons");
                }
            }
        });

        thread.start();
    }

}


