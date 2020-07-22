package com.example.database_project_salesman.Target.Enity;


import com.example.database_project_salesman.Order.Orders;
import  com.example.database_project_salesman.SKU.Sku;


public class Target_SalesMen {
    String TARGET_ID;
    String SKU_ID;
    int achieved;
    int previousAchieved;
    String SaleMenEmail;
    String Order_ID;
    Sku Sku;
    Orders orders;

    public Target_SalesMen() {
    }

    public Target_SalesMen(String TARGET_ID, String SKU_ID, int achieved, int previousAchieved,String saleMenEmail, String order_ID, Sku sku, Orders orders) {
        this.TARGET_ID = TARGET_ID;
        this.SKU_ID = SKU_ID;

        this.achieved = achieved;
        this.previousAchieved=previousAchieved;
        SaleMenEmail = saleMenEmail;
        Order_ID = order_ID;
        Sku = sku;
        this.orders = orders;

    }

    public String getTARGET_ID() {
        return TARGET_ID;
    }

    public void setTARGET_ID(String TARGET_ID) {
        this.TARGET_ID = TARGET_ID;
    }

    public String getSKU_ID() {
        return SKU_ID;
    }

    public void setSKU_ID(String SKU_ID) {
        this.SKU_ID = SKU_ID;
    }

    public int getPreviousAchieved() {
        return previousAchieved;
    }

    public void setPreviousAchieved(int previousAchieved) {
        this.previousAchieved = previousAchieved;
    }

    public int getAchieved() {
        return achieved;
    }

    public void setAchieved(int achieved) {
        this.achieved = achieved;
    }

    public String getSaleMenEmail() {
        return SaleMenEmail;
    }

    public void setSaleMenEmail(String saleMenEmail) {
        SaleMenEmail = saleMenEmail;
    }

    public String getOrder_ID() {
        return Order_ID;
    }

    public void setOrder_ID(String order_ID) {
        Order_ID = order_ID;
    }

    public com.example.database_project_salesman.SKU.Sku getSku() {
        return Sku;
    }

    public void setSku(com.example.database_project_salesman.SKU.Sku sku) {
        Sku = sku;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

}
