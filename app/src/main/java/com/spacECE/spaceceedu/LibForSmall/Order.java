package com.spacECE.spaceceedu.LibForSmall;

public class Order {
    private String bookName;
    private String orderQuantity;
    private String orderPrice;

    public Order(String bookName, String orderQuantity, String orderPrice){
        this.bookName = bookName;
        this.orderQuantity = orderQuantity;
        this.orderPrice = orderPrice;
    }
    public String getBookName(){
        return bookName;
    }
    public void setBookName(String bookName){
        this.bookName = bookName;
    }
    public String getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(String orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }
}
