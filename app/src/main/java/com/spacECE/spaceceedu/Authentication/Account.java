package com.spacECE.spaceceedu.Authentication;

import android.util.Log;
import androidx.annotation.Nullable;

public class Account {
    static private String account_id=null;
    static private String username=null;
    static private String contact_number=null;
    static private String profile_pic=null;
    static private boolean CONSULTANT=false;
    static private String U_ID;
    static private String Consultant_Category;
    static private String Consultant_Office;
    static private String Consultant_StartTime;
    static private String Consultant_EndTime;
    static private String Consultant_Language;
    static private String Consultant_Fee;
    static private String Consultant_Qualification;

    public Account(String account_id, String username, String contact_number, boolean CONSULTANT, String profile_pic) {
        this.account_id = account_id;
        this.username = username;
        this.contact_number = contact_number;
        this.CONSULTANT = CONSULTANT;
        this.profile_pic= profile_pic;
        Log.i("ACCOUNT:", " GENERATED :- "+account_id+" / "+username+" / "+contact_number+ " / "+profile_pic+" / ");
    }

    public Account(String account_id, String username, String contact_number, boolean CONSULTANT, String profile_pic,
                   String Consultant_Category, @Nullable String Consultant_Office, String Consultant_StartTime, String Consultant_EndTime,
                   String Consultant_Language, String Consultant_Fee, String Consultant_Qualification) {

        this.account_id = account_id;
        this.username = username;
        this.contact_number = contact_number;
        this.CONSULTANT = CONSULTANT;
        this.profile_pic= profile_pic;
        this.Consultant_Category = Consultant_Category;
        this.Consultant_Office = Consultant_Office;
        this.Consultant_StartTime = Consultant_StartTime;
        this.Consultant_EndTime = Consultant_EndTime;
        this.Consultant_Language = Consultant_Language;
        this.Consultant_Fee = Consultant_Fee;
        this.Consultant_Qualification = Consultant_Qualification;

        Log.i("ACCOUNT:", " GENERATED :- "+account_id+" / "+username+" / "+contact_number+ " /"+profile_pic+ " /"+
                Consultant_Category+ " /"+Consultant_Office+ " /"+Consultant_StartTime+ " /"+Consultant_EndTime+ " /"+
                Consultant_Language+ " /"+Consultant_Fee+ " /"+Consultant_Qualification);
    }

    public String getuId() {
        return U_ID;
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

    public String getProfile_pic() {
        return profile_pic;
    }

    public boolean isCONSULTANT() {
        return CONSULTANT;
    }

    public static String getConsultant_Category() {
        return Consultant_Category;
    }

    public static String getConsultant_Office() {
        return Consultant_Office;
    }

    public static String getConsultant_StartTime() {
        return Consultant_StartTime;
    }

    public static String getConsultant_EndTime() {
        return Consultant_EndTime;
    }

    public static String getConsultant_Language() {
        return Consultant_Language;
    }

    public static String getConsultant_Fee() {
        return Consultant_Fee;
    }

    public static String getConsultant_Qualification() {
        return Consultant_Qualification;
    }
}
