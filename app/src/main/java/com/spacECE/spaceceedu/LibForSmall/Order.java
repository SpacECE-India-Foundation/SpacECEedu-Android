package com.spacECE.spaceceedu.LibForSmall;

public class Order {
    private String orderId;
    private String orderStatus;
    private String orderDate;

    public Order(String orderId, String orderStatus, String orderDate){
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
    }
    public String getOrderId(){
        return orderId;
    }
    public void setOrderId(String orderId){
        this.orderId = orderId;
    }
    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
