package com.spacECE.spaceceedu.LibForSmall;

import org.jetbrains.annotations.NotNull;

public class books {
    String product_id;
    String product_title;
    String product_price;
    String product_keywords;
    String product_image;
    String product_brand;
    String product_desc;
    String exchange_price;
    String rent_price;
    String deposit;

    public books(String product_id, String product_title, String product_price, String product_keywords, String product_image, String product_brand, String product_desc, String exchange_price, String rent_price, String deposit) {
        this.product_id = product_id;
        this.product_title = product_title;
        this.product_price = product_price;
        this.product_keywords = product_keywords;
        this.product_image = product_image;
        this.product_brand = product_brand;
        this.product_desc = product_desc;
        this.exchange_price = exchange_price;
        this.rent_price = rent_price;
        this.deposit = deposit;
    }

    public String getProduct_brand() {
        return product_brand;
    }

    public void setProduct_brand(String product_brand) {
        this.product_brand = product_brand;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getExchange_price() {
        return exchange_price;
    }

    public void setExchange_price(String exchange_price) {
        this.exchange_price = exchange_price;
    }

    public String getRent_price() {
        return rent_price;
    }

    public void setRent_price(String rent_price) {
        this.rent_price = rent_price;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getProduct_price() {
        return product_price;
    }

    private void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_keywords() {
        return product_keywords;
    }

    public void setProduct_keywords(String product_keywords) {
        this.product_keywords = product_keywords;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    @NotNull
    @Override
    public String toString() {
        return "product_id= " + product_id +
                " product_title= " + product_title +
                "\nproduct_keywords= " + product_keywords +
                "\nproduct_price= " + product_price +
                "\nproduct_image= " + product_image +
                "\nproduct_brand= "+ product_brand+
                "\nproduct_desc= "+ product_desc +
                "\nexchange_price= "+ exchange_price +
                "\nrent_price= "+ rent_price+
                "\ndeposit= "+deposit;
    }
}