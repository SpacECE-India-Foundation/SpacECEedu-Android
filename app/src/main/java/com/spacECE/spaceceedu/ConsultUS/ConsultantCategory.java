package com.spacECE.spaceceedu.ConsultUS;

import android.graphics.drawable.Drawable;

public class ConsultantCategory {
    private String CategoryName, icon;
    private Drawable drawable;

    public ConsultantCategory(Drawable drawable) {
        this.drawable = drawable;
    }

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
