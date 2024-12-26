package com.spacECE.spaceceedu.Authentication;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalStore {

    public static final String DETAILS = "UserDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(DETAILS, 0);
    }

    //Get user Logged in data
    public Account getLoggedInAccount() {
        String name = userLocalDatabase.getString("username", null);
        String account_id = userLocalDatabase.getString("account_id", null);
        String contact_number = userLocalDatabase.getString("contact_number", null);
        String UID = userLocalDatabase.getString("UID", null);
        String profile_pic = userLocalDatabase.getString("profile_pic", null);
        System.out.println("TESTTTT"+profile_pic);
        String user_email = userLocalDatabase.getString("user_email", null);
        boolean isConsultant = userLocalDatabase.getBoolean("isConsultant", false);

        Account account = new Account(account_id, name, contact_number,isConsultant,profile_pic,user_email);
        return account;
    }

    //Set user Logged in data
    public void setUserLoggedIn(boolean loggedIn, Account account) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        if(account.isCONSULTANT()){
            spEditor.putBoolean("loggedIn", loggedIn);
            spEditor.putString("account_id", account.getAccount_id());
            spEditor.putString("username", account.getUsername());
            spEditor.putString("contact_number", account.getContact_number());
            spEditor.putString("UID", account.getuId());
            spEditor.putBoolean("isConsultant", account.isCONSULTANT());
            spEditor.putString("profile_pic", account.getProfile_pic());
            spEditor.putString("consultant_category", account.getConsultant_Category());
            spEditor.putString("consultant_office", account.getConsultant_Office());
            spEditor.putString("consultant_start_time", account.getConsultant_StartTime());
            spEditor.putString("consultant_end_time", account.getConsultant_Language());
            spEditor.putString("consultant_fee", account.getConsultant_Fee());
            spEditor.putString("consultant_qualification", account.getConsultant_Qualification());
            spEditor.commit();
        } else {
            spEditor.putBoolean("loggedIn", loggedIn);
            spEditor.putString("account_id", account.getAccount_id());
            spEditor.putString("username", account.getUsername());
            spEditor.putString("contact_number", account.getContact_number());
            spEditor.putString("UID", account.getuId());
            spEditor.putBoolean("isConsultant", account.isCONSULTANT());
            spEditor.putString("profile_pic", account.getProfile_pic());
            spEditor.putString("user_email", account.getUser_email()); // Store the user_email value

            System.out.println("profile_pic"+account.getProfile_pic());
            System.out.println("profile_pic"+account.getUser_email());
            System.out.println("profile_pic"+account.isCONSULTANT());
            System.out.println("profile_pic"+account.getContact_number());
            System.out.println("profile_pic"+account.getUsername());
            System.out.println("profile_pic"+account.getAccount_id());
            spEditor.commit();
        }
    }

    //Set user logged in
    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    //Clear User Data from Shared Preferences when logged out
    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }

    //Check if user is logged in or not
    public boolean getUserLoggedIn(){
        if(userLocalDatabase.getBoolean("loggedIn", false)==true){
            return true;
        }
        return false;
    }
}

