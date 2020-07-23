package com.example.database_project_salesman.Order.Show_Order;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.database_project_salesman.Order.Entity.Orders;
import com.example.database_project_salesman.R;

import java.util.ArrayList;
import java.util.List;

public class show_order_rv_adaprter extends RecyclerView.Adapter<show_order_rv_adaprter.viewHolder>
{
    private List<Orders> shop;
    private Activity context;

    public show_order_rv_adaprter(List<Orders> shop, Activity context)
    {
        this.shop = shop;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view=LayoutInflater.from(context).inflate(R.layout.show_orders_rv_layout,parent,false);
        return new viewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position)
    {
        holder.shopName.setText(shop.get(position).getShopName());
        holder.skuName.setText(shop.get(position).getSku().toString());
        holder.quantity.setText(Integer.toString(shop.get(position).getQuantity()));
        holder.status.setText(shop.get(position).getOrderStatus());
        if(shop.get(position).getOrderStatus().equals("Delivered"))
        {
            holder.status.setTextColor(context.getColor(R.color.green));
        }
        else
        {
            if(shop.get(position).getOrderStatus().equals("In progress"))
            {
                holder.status.setTextColor(context.getColor(R.color.yellow));
            }
            else
            {
                holder.status.setTextColor(context.getColor(R.color.red));
            }
        }

    }

    @Override
    public int getItemCount()
    {
        return shop.size();
    }
    public void updateList(String search, List<Orders> orders ) {
        if(search.equals(""))
        {
            if(!(shop.size()==orders.size()))
            {
                this.shop.clear();
                List<Orders> empty = new ArrayList<>();
                for (int i=0; i< orders.size(); i++) {
                    empty.add(orders.get(i));
                }
                this.shop=empty;
                notifyDataSetChanged();
            }

        }
        if(!search.equals(""))
        {

            List<Orders>  temps = new ArrayList<>();
            for (int i=0; i< orders.size(); i++) {
                if (orders.get(i).getQuantity()==(Integer.parseInt(search.toLowerCase()))) {
                    temps.add(orders.get(i));
                }
                if (orders.get(i).getOrderStatus().toLowerCase().contains(search.toLowerCase())) {
                    temps.add(orders.get(i));
                }
                if (orders.get(i).getShopName().toLowerCase().contains(search.toLowerCase()))
                {
                    temps.add(orders.get(i));
                }
                if (orders.get(i).getSku().getProductName().toLowerCase().contains(search.toLowerCase()))
                {
                    temps.add(orders.get(i));
                }

            }
            //
            this.shop = temps;
            notifyDataSetChanged();
        }

    }
    public class viewHolder extends RecyclerView.ViewHolder
    {
        TextView shopName,skuName,quantity,status;
        public viewHolder(@NonNull View itemView)
        {
            super(itemView);
            shopName=itemView.findViewById(R.id.show_order_rv_layout_shopname);
            skuName=itemView.findViewById(R.id.show_order_rv_layout_skuname);
            quantity=itemView.findViewById(R.id.show_order_rv_layout_quantity);
            status=itemView.findViewById(R.id.show_order_rv_layout_status);
        }
    }
}
