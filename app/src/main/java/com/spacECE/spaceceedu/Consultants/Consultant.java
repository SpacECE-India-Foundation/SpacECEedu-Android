package com.spacECE.spaceceedu.Consultants;

public class Consultant {
    private String name,consultant_id,profilePic_src,about,categories,rating;
    private int price;

    public Consultant(String name, String consultant_id, String profilePic_src, String categories, int price, String rating) {
        this.name = name;
        this.consultant_id = consultant_id;
        this.profilePic_src = profilePic_src;
        this.categories = categories;
        this.price = price;
        this.rating = rating;
    }

    public String getProfilePic_src() {
        return profilePic_src;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAbout() {
        return about;
    }

    public String getName() {
        return name;
    }

    public String getConsultant_id() {
        return consultant_id;
    }

    public String getCategories() {
        return categories;
    }

    public int getPrice() {
        return price;
    }

    public String getRating() {
        return rating;
    }
}
