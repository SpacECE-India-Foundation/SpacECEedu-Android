package com.spacECE.spaceceedu.ConsultUS;

public class Consultant {
    private String name,consultant_id,profilePic_src,categories,address,language,timing_start,timing_end,qualification;
    private String price;
    private String c_available_from,c_available_to,c_aval_days;

    public String getC_available_from() {
        return c_available_from;
    }

    public void setC_available_from(String c_available_from) {
        this.c_available_from = c_available_from;
    }

    public String getC_available_to() {
        return c_available_to;
    }

    public void setC_available_to(String c_available_to) {
        this.c_available_to = c_available_to;
    }

    public String getC_aval_days() {
        return c_aval_days;
    }

    public void setC_aval_days(String c_aval_days) {
        this.c_aval_days = c_aval_days;
    }
//TODO in this class days on which consultant is available is missing and needs to implemented from the server side and proper string array to be added.

    public Consultant(String name, String consultant_id, String profilePic_src, String categories
            , String address, String language, String timing_start, String timing_end,
                      String qualification, String price) {
        this.name = name;
        this.consultant_id = consultant_id;
        this.profilePic_src = profilePic_src;
        this.categories = categories;
        this.address = address;
        this.language = language;
        this.timing_start = timing_start;
        this.timing_end = timing_end;
        this.qualification = qualification;
        this.price = price;
    }

    public String getQualification() {
        return qualification;
    }

    public String getName() {
        return name;
    }

    public String getConsultant_id() {
        return consultant_id;
    }

    public String getProfilePic_src() {
        return profilePic_src;
    }

    public String getCategories() {
        return categories;
    }

    public String getAddress() {
        return address;
    }

    public String getLanguage() {
        return language;
    }

    public String getTiming_start() {
        return timing_start;
    }

    public String getTiming_end() {
        return timing_end;
    }

    public String getPrice() {
        return price;
    }
}
