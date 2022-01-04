package com.spacECE.spaceceedu.ConsultUS;

public class ConsultantCategory {
    private String CategoryName, icon;

    public ConsultantCategory(String categoryName, String icon) {
        this.CategoryName = categoryName;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public String getCategoryName() {
        return CategoryName;
    }
}
