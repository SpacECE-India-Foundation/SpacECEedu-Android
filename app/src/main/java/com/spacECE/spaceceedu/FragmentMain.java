package com.spacECE.spaceceedu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.window.SplashScreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.spacECE.spaceceedu.Authentication.Account;
import com.spacECE.spaceceedu.Authentication.LoginActivity;
import com.spacECE.spaceceedu.Authentication.UserLocalStore;
import com.spacECE.spaceceedu.ConsultUS.ConsultUs_SplashScreen;
import com.spacECE.spaceceedu.GrowthTracker.GrowthTrackerHome;
import com.spacECE.spaceceedu.LearnOnApp.LearnOn_List_SplashScreen;
import com.spacECE.spaceceedu.LibForSmall.library_splash_screen;
import com.spacECE.spaceceedu.VideoLibrary.VideoLibrary_Activity_SplashScreen;
import com.spacECE.spaceceedu.space_active.ActivitiesListActivity;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class FragmentMain extends Fragment {

    static Account account ;

    Button sign_signUp;
    Button signOut;
    private final int[] mImages = new int[]{
            R.drawable.view1, R.drawable.view2, R.drawable.view3,
            //R.drawable.view4,R.drawable.view5
    };


    CardView cv_videoLibrary;
    CardView cv_consultation;
    CardView cv_dailyActivities;
    CardView cv_libraryBooks;
    CardView cv_learnOnApp;
    CardView growthTracker;

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

        //Navigating to Daily Activities
        cv_dailyActivities = v.findViewById(R.id.CardView_MyActivities);

        //Navigating to Library Books
        cv_libraryBooks = v.findViewById(R.id.CardView_Library);

        cv_learnOnApp = v.findViewById(R.id.CardView_LearnOnApp);

        growthTracker = v.findViewById(R.id.CardView_GrowthTracker);

        cv_videoLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getContext(), VideoLibrary_Activity_SplashScreen.class);
                intent.putExtra("account_id","2");
                startActivity(intent);
            }
        });

        cv_consultation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ConsultUs_SplashScreen.class);
                startActivity(intent);
            }
        });

        cv_dailyActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ActivitiesListActivity.class);
                startActivity(intent);
            }
        });

        cv_libraryBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), library_splash_screen.class);
                startActivity(intent);
            }
        });

        cv_learnOnApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LearnOn_List_SplashScreen.class);
                startActivity(intent);
            }
        });

        growthTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLocalStore userLocalStore = new UserLocalStore(requireContext());
                Account account = userLocalStore.getLoggedInAccount();
                // Check if the user is logged in (you can replace this condition with your actual login check)
                if (account != null) {
                    Intent intent = new Intent(getContext(), GrowthTrackerHome.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Please LogIn/SignUp first", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                }
            }
        });

    return v;
    }

}


