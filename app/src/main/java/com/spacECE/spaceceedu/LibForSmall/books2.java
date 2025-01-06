package com.spacECE.spaceceedu.LibForSmall;

import org.jetbrains.annotations.NotNull;

public class books2 {
    String cart_id;
    String product_id;
    String user_id;
    String quantity;
    String status;
    String exchange_product;
    String exchange_price;
    String product_title;
    String product_quantity;
    String product_price;
    String product_brand;
    String product_image;
    String product_cat;
    public books2(String cart_id,String product_id,String user_id,String quantity,String status,String exchange_product, String exchange_price,String product_title,String product_quantity ,  String product_price,  String product_brand,String product_image,String product_cat) {
        this.cart_id = cart_id;
        this.product_id = product_id;
        this.user_id = user_id;
        this.quantity = quantity;
        this.status = status;
        this.exchange_product = exchange_product;
        this.exchange_price = exchange_price;
        this.product_title = product_title;
        this.product_quantity = product_quantity;
        this.product_price = product_price;
        this.product_brand = product_brand;
        this.product_image = product_image;
        this.product_cat = product_cat;
    }

    public String getProduct_brand() {
        return product_brand;
    }

    public void setProduct_brand(String product_brand) {
        this.product_brand = product_brand;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getExchange_price() {
        return exchange_price;
    }

    public void setExchange_price(String exchange_price) {
        this.exchange_price = exchange_price;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExchange_product() {
        return exchange_product;
    }

    public void setExchange_product(String exchange_product) {
        this.exchange_product = exchange_product;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getProduct_cat() {
        return product_cat;
    }

    public void setProduct_cat(String product_cat) {
        this.product_cat = product_cat;
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
        return  "cart_id= " + cart_id +
                "product_id= " + product_id +
                "user_id= " + user_id +
                "quantity= " + quantity +
                "status= " + status +
                "exchange_product= " + exchange_product +
                "\nexchange_price= "+ exchange_price +
                " product_title= " + product_title +
                "\nproduct_quantity= " + product_quantity +
                "\nproduct_price= " + product_price +
                "\nproduct_brand= "+ product_brand+
                "\nproduct_image= " + product_image +
                "\nproduct_cat= "+product_cat;

    }

    public int getItemTotalPrice() {
        return Integer.parseInt(product_price) * Integer.parseInt(quantity);
    }
}