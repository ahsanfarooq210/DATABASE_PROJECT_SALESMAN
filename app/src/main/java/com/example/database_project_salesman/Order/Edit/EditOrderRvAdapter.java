package com.example.database_project_salesman.Order.Edit;

import android.app.Activity;
import android.content.Intent;
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

public class EditOrderRvAdapter extends RecyclerView.Adapter<EditOrderRvAdapter.viewHolder>
{
    private List<Orders> shop;
    private Activity context;
//    private EditOrderInterface editOrderInterface;
//
//    public interface EditOrderInterface
//    {
//        public void onItemClick(int position);
//    }

    public EditOrderRvAdapter(List<Orders> shop, Activity context)
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position)
    {

        holder.shopName.setText(shop.get(position).getShopName());
        holder.skuName.setText(shop.get(position).getSku().toString());
        holder.quantity.setText(Integer.toString(shop.get(position).getQuantity()));

        holder.status.setText(shop.get(position).getOrderStatus());
        if(shop.get(position).getOrderStatus().equals(context.getString(R.string.delivered)))
        {
            holder.status.setTextColor(context.getColor(R.color.green));
        }
        else
        {
            if(shop.get(position).getOrderStatus().equals(context.getString(R.string.in_progress)))
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
                int qunatity=orders.get(i).getQuantity();

                 if (orders.get(i).getSku().getProductName().compareToIgnoreCase(search)==0)
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

                    itemView.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                    String id=shop.get(getAdapterPosition()).getId();
                    Intent intent=new Intent(context, edit_order_form_activity.class);
                    intent.putExtra("order_id",id);

                    context.startActivity(intent);
                }
            });

        }
    }
}
