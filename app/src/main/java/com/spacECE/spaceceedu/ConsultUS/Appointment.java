package com.spacECE.spaceceedu.ConsultUS;

public class Appointment {
    private String consult_id;
    private String c_name, u_name, c_pic, u_pic;
    private String bookedAt;
    private String duration;
    private String time;
    private String mobile;

    //get phone number
    private String consultantPhoneNumber;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Appointment(String consult_id, String c_name, String u_name, String c_pic,
                       String u_pic, String Mobile, String bookedAt, String duration) {
        this.consult_id = consult_id;
        this.c_name = c_name;
        this.u_name = u_name;
        this.c_pic = c_pic;
        this.u_pic = u_pic;
        this.mobile = Mobile;
        this.bookedAt = bookedAt;
        this.duration = duration;
    }

    public String getConsult_id() {
        return consult_id;
    }

    public String getC_name() {
        return c_name;
    }

    public String getU_name() {
        return u_name;
    }

    public String getC_pic() {
        return c_pic;
    }

    public String getU_pic() {
        return u_pic;
    }

    public String getBookedAt() {
        return bookedAt;
    }

    public String getDuration() {
        return duration;
    }

    public String getMobile() {
        return mobile;
    }

    //get consultant phone number
    public String getConsultantPhoneNumber() {
        return consultantPhoneNumber;
    }

    public void setConsultantPhoneNumber(String consultantPhoneNumber){
        this.consultantPhoneNumber = consultantPhoneNumber;
    }
}
