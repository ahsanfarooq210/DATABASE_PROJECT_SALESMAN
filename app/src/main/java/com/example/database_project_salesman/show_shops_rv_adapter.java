package com.example.database_project_salesman;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class show_shops_rv_adapter extends RecyclerView.Adapter<show_shops_rv_adapter.viewolder>
{
    private Activity context;
    private ArrayList<ShopDetails> list;

    public show_shops_rv_adapter(Activity context, ArrayList<ShopDetails> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(context).inflate(R.layout.show_orders_rv_layout,parent,false);
        return new show_shops_rv_adapter.viewolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewolder holder, int position)
    {
        holder.shopName.setText(list.get(position).getShopName());
        holder.ownerName.setText(list.get(position).getOwnerName());
        holder.ownerCnic.setText(list.get(position).getOwnerCnic());

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class viewolder extends RecyclerView.ViewHolder
    {
        TextView shopName,ownerName,ownerCnic;

        public viewolder(@NonNull View itemView)
        {
            super(itemView);
            shopName=itemView.findViewById(R.id.show_order_rv_layout_shopname);
            ownerName=itemView.findViewById(R.id.show_order_rv_layout_skuname);
            ownerCnic=itemView.findViewById(R.id.show_order_rv_layout_quantity);
        }
    }

}
