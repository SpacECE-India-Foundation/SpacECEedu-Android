package com.spacECE.spaceceedu.Authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.service.autofill.UserData;
import android.util.Log;

public class Account {
    static private String account_id=null;
    static private String username=null;
    static private String contact_number=null;

    public Account(String account_id, String username, String contact_number) {
        this.account_id = account_id;
        this.username = username;
        this.contact_number = contact_number;
        Log.i("ACCOUNT:", " GENERATED");
    }

    public String getAccount_id() {
        return account_id;
    }

    public String getUsername() {
        return username;
    }

    public String getContact_number() {
        return contact_number;
    }
}

