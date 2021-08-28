package com.spacECE.spaceceedu.Consultants;

public class UserAppointments {
    private String date, month, year, time, c_name, p_name,user_id,category,status,b_id,c_contactNumber,p_contactNumber;

    public UserAppointments(String date, String month, String year, String time
            , String c_name, String p_name, String user_id, String category
            , String status, String b_id, String c_contactNumber, String p_contactNumber) {
        this.date = date;
        this.month = month;
        this.year = year;
        this.time = time;
        this.c_name = c_name;
        this.p_name = p_name;
        this.user_id = user_id;
        this.category = category;
        this.status = status;
        this.b_id = b_id;
        this.c_contactNumber = c_contactNumber;
        this.p_contactNumber = p_contactNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getTime() {
        return time;
    }

    public String getC_name() {
        return c_name;
    }

    public String getP_name() {
        return p_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCategory() {
        return category;
    }

    public String getStatus() {
        return status;
    }

    public String getB_id() {
        return b_id;
    }

    public String getC_contactNumber() {
        return c_contactNumber;
    }

    public String getP_contactNumber() {
        return p_contactNumber;
    }
}
