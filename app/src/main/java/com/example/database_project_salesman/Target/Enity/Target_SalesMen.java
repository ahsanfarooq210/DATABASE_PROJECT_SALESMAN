package com.example.database_project_salesman.Target.Enity;


import com.example.database_project_salesman.Order.Entity.Orders;
import com.example.database_project_salesman.SKU.Sku;


public class Target_SalesMen
{
    String TARGET_ID;
    String SKU_ID;
    int achieved;

    String SaleMenEmail;
    Sku Sku;
    Orders orders;
    String status;
    String status_skuId;

    public Target_SalesMen()
    {
    }

    public Target_SalesMen(String TARGET_ID, String SKU_ID, int achieved, String saleMenEmail, Sku sku, Orders orders, String status)
    {
        this.TARGET_ID = TARGET_ID;
        this.SKU_ID = SKU_ID;

        this.achieved = achieved;

        SaleMenEmail = saleMenEmail;
        this.status = status;
        Sku = sku;
        this.orders = orders;

        status_skuId=status+SKU_ID;

    }

    public String getStatus_skuId()
    {
        return status_skuId;
    }

    public void setStatus_skuId(String status_skuId)
    {
        this.status_skuId = status_skuId;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getTARGET_ID()
    {
        return TARGET_ID;
    }

    public void setTARGET_ID(String TARGET_ID)
    {
        this.TARGET_ID = TARGET_ID;
    }

    public String getSKU_ID()
    {
        return SKU_ID;
    }

    public void setSKU_ID(String SKU_ID)
    {
        this.SKU_ID = SKU_ID;
    }


    public int getAchieved()
    {
        return achieved;
    }

    public void setAchieved(int achieved)
    {
        this.achieved = achieved;
    }

    public String getSaleMenEmail()
    {
        return SaleMenEmail;
    }

    public void setSaleMenEmail(String saleMenEmail)
    {
        SaleMenEmail = saleMenEmail;
    }


    public com.example.database_project_salesman.SKU.Sku getSku()
    {
        return Sku;
    }

    public void setSku(com.example.database_project_salesman.SKU.Sku sku)
    {
        Sku = sku;
    }

    public Orders getOrders()
    {
        return orders;
    }

    public void setOrders(Orders orders)
    {
        this.orders = orders;
    }

}
