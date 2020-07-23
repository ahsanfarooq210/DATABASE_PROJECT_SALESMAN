package com.example.database_project_salesman.Order.Order_status_filter;

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

public class OrderStatusRvAdapter extends RecyclerView.Adapter<OrderStatusRvAdapter.viewHolder>
{
    private List<Orders> orderList;
    private Activity context;
//    private EditOrderInterface editOrderInterface;
//
//    public interface EditOrderInterface
//    {
//        public void onItemClick(int position);
//    }

    public OrderStatusRvAdapter(List<Orders> orderList, Activity context)
    {
        this.orderList = orderList;
        this.context = context;
        //editOrderInterface= (EditOrderInterface) context;
    }



    @NonNull
    @Override
    public OrderStatusRvAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(context).inflate(R.layout.show_orders_rv_layout,parent,false);
        return new OrderStatusRvAdapter.viewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull OrderStatusRvAdapter.viewHolder holder, int position)
    {

        holder.shopName.setText(orderList.get(position).getShopName());
        holder.skuName.setText(orderList.get(position).getSku().toString());
        holder.quantity.setText(Integer.toString(orderList.get(position).getQuantity()));

        holder.status.setText(orderList.get(position).getOrderStatus());
        if(orderList.get(position).getOrderStatus().equals(context.getString(R.string.delivered)))
        {
            holder.status.setTextColor(context.getColor(R.color.green));
        }
        else
        {
            if(orderList.get(position).getOrderStatus().equals(context.getString(R.string.in_progress)))
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
        return orderList.size();
    }

    public void updateList(String search, List<Orders> searchList ) {
        if(search.equals(""))
        {
            if(!(orderList.size()==searchList.size()))
            {
                this.orderList.clear();
                List<Orders> empty = new ArrayList<>();
                for (int i=0; i< searchList.size(); i++) {
                    empty.add(searchList.get(i));
                }
                this.orderList=empty;
                notifyDataSetChanged();
            }

        }
        if(!search.equals(""))
        {

            List<Orders>  temps = new ArrayList<>();
            for (int i=0; i< searchList.size(); i++) {

                if (searchList.get(i).getShopName().toLowerCase().contains(search.toLowerCase())) {
                    temps.add(searchList.get(i));
                }
                if (searchList.get(i).getSku().getProductName().toLowerCase().contains(search.toLowerCase()))
                {
                    temps.add(searchList.get(i));
                }
                if (searchList.get(i).getQuantity()==Integer.parseInt(search))
                {
                    temps.add(searchList.get(i));
                }

            }
            //
            this.orderList = temps;
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
                    String id=orderList.get(getAdapterPosition()).getId();
                    Intent intent=new Intent(context, Order_filter_status_form.class);
                    intent.putExtra("order_id",id);
                    context.startActivity(intent);
                }
            });

        }
    }
}
