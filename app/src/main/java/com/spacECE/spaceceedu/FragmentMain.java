package com.spacECE.spaceceedu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.spacECE.spaceceedu.Authentication.Account;
import com.spacECE.spaceceedu.Authentication.LoginActivity;
import com.spacECE.spaceceedu.Consultants.Consultant_Main;
import com.spacECE.spaceceedu.Consultants.ConsultantsLibrary;
import com.spacECE.spaceceedu.VideoLibrary.VideoLibrary_Activity;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class FragmentMain extends Fragment {

    static Account account ;

    Button sign_signUp;
    Button signOut;
    private final int[] mImages = new int[]{
            R.drawable.view1, R.drawable.view2, R.drawable.view3,
            R.drawable.img
    };


    CardView cd_videoLibrary;
    CardView cd_consultation;
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
        cd_consultation=v.findViewById(R.id.CardView_Consultation);
        cd_videoLibrary=v.findViewById(R.id.CardView_VideoLibrary);

        cd_videoLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent =new Intent(getContext(), VideoLibrary_Activity.class);
                myIntent.putExtra("account_id","2");
                startActivity(myIntent);
            }
        });

        cd_consultation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Consultant_Main.class);
                startActivity(intent);
            }
        });
    return v;
    }
}


