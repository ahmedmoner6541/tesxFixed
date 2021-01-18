package com.example.myprogect.model;

public class OrdersSold {
    private String totalAmount,name,phone,address,city,time,date,state


                 , amountOrder
                 ,dateOrder
                 ,priceOrder;

    public OrdersSold() {
    }

    public OrdersSold(String amountOrder, String dateOrder, String priceOrder) {
        this.amountOrder = amountOrder;
        this.dateOrder = dateOrder;
        this.priceOrder = priceOrder;
    }

    public String getAmountOrder() {

        return amountOrder;
    }

    public void setAmountOrder(String amountOrder) {
        this.amountOrder = amountOrder;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    public String getPriceOrder() {
        return priceOrder;
    }

    public void setPriceOrder(String priceOrder) {
        this.priceOrder = priceOrder;
    }
}
