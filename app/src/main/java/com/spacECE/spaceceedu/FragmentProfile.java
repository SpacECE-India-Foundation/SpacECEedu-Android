package com.spacECE.spaceceedu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.spacECE.spaceceedu.Authentication.Account;
import com.spacECE.spaceceedu.Authentication.LoginActivity;

public class FragmentProfile extends Fragment {

    private TextView nameTextView, emailTextView, phoneTextView;
    private Button signOutButton, loginButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameTextView = view.findViewById(R.id.ShowName_Profile);
        emailTextView = view.findViewById(R.id.ShowEmail_Profile);
        phoneTextView = view.findViewById(R.id.ShowMobileNo_profile);
        signOutButton = view.findViewById(R.id.Signout_btn_profile);
        loginButton = view.findViewById(R.id.Login_btn_profile);

        Account account = MainActivity.ACCOUNT;
        if (account != null) {
            // User is logged in
            nameTextView.setText(account.getUsername());
            emailTextView.setText(account.getUser_email());
            phoneTextView.setText(account.getContact_number());
            signOutButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);

            signOutButton.setOnClickListener(v -> signOut());
        } else {
            // User is not logged in
            displayLoggedOutState();
        }

        return view;
    }

    private void signOut() {
        // Clear the user's data and log them out
        MainActivity.ACCOUNT = null;

        // Navigate to the login screen or perform any other necessary actions
        startActivity(new Intent(requireContext(), LoginActivity.class));
        requireActivity().finish();
    }

    private void displayLoggedOutState() {
        nameTextView.setText("Not Logged In");
        nameTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        emailTextView.setVisibility(View.GONE);
        phoneTextView.setVisibility(View.GONE);
        signOutButton.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);

        loginButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Please log in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireContext(), LoginActivity.class));
        });
    }
}
