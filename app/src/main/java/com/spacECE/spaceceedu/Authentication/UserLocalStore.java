package com.spacECE.spaceceedu.Authentication;

import android.content.Context;
import android.content.SharedPreferences;

import com.spacECE.spaceceedu.Authentication.Account;

public class UserLocalStore {

    public static final String DETAILS = "UserDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context) {
        userLocalDatabase = context.getSharedPreferences(DETAILS, 0);
    }

    public void storeUserData(Account account) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("account_id", account.getAccount_id());
        spEditor.putString("username", account.getUsername());
        spEditor.putString("contact_number", account.getContact_number());
        spEditor.commit();

    }

    public Account getLoggedInAccount() {
        String name = userLocalDatabase.getString("username", null);
        String account_id = userLocalDatabase.getString("account_id", null);
        String contact_number = userLocalDatabase.getString("contact_number", null);
        Account account = new Account(account_id, name, contact_number);
        return account;
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();

    }

    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }


}
