package com.example.database_project_salesman.OrderActicities;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.database_project_salesman.R;

import java.util.ArrayList;

public class EditOrderRvAdapter extends RecyclerView.Adapter<EditOrderRvAdapter.viewHolder>
{
    private ArrayList<Orders> shop;
    private Activity context;
//    private EditOrderInterface editOrderInterface;
//
//    public interface EditOrderInterface
//    {
//        public void onItemClick(int position);
//    }

    public EditOrderRvAdapter(ArrayList<Orders> shop, Activity context)
    {
        this.shop = shop;
        this.context = context;
        //editOrderInterface= (EditOrderInterface) context;
    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(context).inflate(R.layout.show_orders_rv_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position)
    {

        holder.shopName.setText(shop.get(position).getShopName());
        holder.skuName.setText(shop.get(position).getSku().toString());
        holder.quantity.setText(Integer.toString(shop.get(position).getQuantity()));
    }

    @Override
    public int getItemCount()
    {
        return shop.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder
    {

        TextView shopName,skuName,quantity;
        public viewHolder(@NonNull View itemView)
        {
            super(itemView);
            shopName=itemView.findViewById(R.id.show_order_rv_layout_shopname);
            skuName=itemView.findViewById(R.id.show_order_rv_layout_skuname);
            quantity=itemView.findViewById(R.id.show_order_rv_layout_quantity);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String id=shop.get(getAdapterPosition()).id;
                    Intent intent=new Intent(context,edit_order_form_activity.class);
                    intent.putExtra("order_id",id);
                    context.startActivity(intent);
                }
            });

        }
    }
}
