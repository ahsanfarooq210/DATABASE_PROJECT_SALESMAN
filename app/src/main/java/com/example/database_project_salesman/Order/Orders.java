package com.example.database_project_salesman.Order;

import com.example.database_project_salesman.SHOP.ShopDetails;
import com.example.database_project_salesman.SKU.Sku;

public class Orders
{
    String id;
    String salesman;
    ShopDetails shop;
    Sku sku;
    int quantity;
    String orderStatus;

    public Orders()
    {
    }

    public Orders(String id, String salesman, ShopDetails shop, Sku sku, int quantity,String orderStatus)
    {
        this.id = id;
        this.salesman = salesman;
        this.shop = shop;
        this.sku = sku;
        this.quantity = quantity;
        this.orderStatus=orderStatus;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getSalesman()
    {
        return salesman;
    }

    public void setSalesman(String salesman)
    {
        this.salesman = salesman;
    }

    public ShopDetails getShop()
    {
        return shop;
    }

    public void setShop(ShopDetails shop)
    {
        this.shop = shop;
    }

    public Sku getSku()
    {
        return sku;
    }

    public void setSku(Sku sku)
    {
        this.sku = sku;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public String getShopName()
    {
        return shop.getShopName();
    }

    public String getOrderStatus()
    {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus)
    {
        this.orderStatus = orderStatus;
    }
}
