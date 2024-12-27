package com.spacECE.spaceceedu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.spacECE.spaceceedu.Authentication.Account;
import com.spacECE.spaceceedu.Authentication.LoginActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FragmentProfile extends Fragment {

    private TextView nameTextView, emailTextView, phoneTextView;
    private ImageView profileImageView;
    private Button signOutButton, loginButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameTextView = view.findViewById(R.id.ShowName_Profile);
        emailTextView = view.findViewById(R.id.ShowEmail_Profile);
        phoneTextView = view.findViewById(R.id.ShowMobileNo_profile);
        profileImageView = view.findViewById(R.id.profile_pic);
        signOutButton = view.findViewById(R.id.Signout_btn_profile);
        loginButton = view.findViewById(R.id.Login_btn_profile);

        Account account = MainActivity.ACCOUNT;
        if (account != null) {
            // User is logged in
            nameTextView.setText(account.getUsername());
            emailTextView.setText(account.getUser_email());
            phoneTextView.setText(account.getContact_number());

            // Test loading image from Google Drive (public URL)
            String profilePicUrl = "https://drive.google.com/uc?export=view&id=1euwFojCEAStP9mXVaUH3eLZDx39nW7eg"; // Replace with your actual image ID
            if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                LoadProfilePicture loadProfilePicture = new LoadProfilePicture();
                loadProfilePicture.loadImage(profilePicUrl, profileImageView);
            } else {
                profileImageView.setImageResource(R.drawable.profile); // Default profile picture
            }

            signOutButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);

            signOutButton.setOnClickListener(v -> signOut());
        } else {
            displayLoggedOutState();
        }

        return view;
    }

    private void signOut() {
        MainActivity.ACCOUNT = null;

        startActivity(new Intent(requireContext(), LoginActivity.class));
        requireActivity().finish();
    }

    private void displayLoggedOutState() {
        nameTextView.setText("Not Logged In");
        nameTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        emailTextView.setVisibility(View.GONE);
        phoneTextView.setVisibility(View.GONE);
        profileImageView.setImageResource(R.drawable.profile); // Default profile picture
        signOutButton.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);

        loginButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Please log in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireContext(), LoginActivity.class));
        });
    }

    // Inner class for loading profile pictures
    public class LoadProfilePicture {
        private static final String TAG = "LoadProfilePicture";
        private final ExecutorService executorService = Executors.newSingleThreadExecutor();
        private final Handler mainHandler = new Handler(Looper.getMainLooper());

        public void loadImage(String imageUrl, ImageView imageView) {
            executorService.execute(() -> {
                Bitmap bitmap = null;

                try {
                    URL url = new URL(imageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } catch (Exception e) {
                    Log.e(TAG, "Error loading image: " + e.getMessage());
                }

                Bitmap finalBitmap = bitmap;
                mainHandler.post(() -> {
                    if (finalBitmap != null) {
                        imageView.setImageBitmap(finalBitmap);
                    } else {
                        imageView.setImageResource(R.drawable.profile); // Fallback image
                    }
                });
            });
        }
    }
}
