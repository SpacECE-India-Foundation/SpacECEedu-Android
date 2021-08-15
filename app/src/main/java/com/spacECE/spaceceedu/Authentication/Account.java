package com.spacECE.spaceceedu.Authentication;

import android.util.Log;

public class Account {
    static private String account_id=null;
    static private String username=null;
    static private String contact_number=null;

    public Account(String account_id, String username, String contact_number) {
        this.account_id = account_id;
        this.username = username;
        this.contact_number = contact_number;
        Log.i("ACCOUNT:", " GENERATED :- "+account_id+" / "+username+" / "+contact_number);
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

