package com.example.database_project_salesman.SHOP;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.database_project_salesman.R;

import java.util.ArrayList;
import java.util.List;

public class show_shops_rv_adapter extends RecyclerView.Adapter<show_shops_rv_adapter.viewolder>
{
    private Activity context;
    private List<ShopDetails> list;

    public show_shops_rv_adapter(Activity context, List<ShopDetails> list)
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

    public void updateList(String search, List<ShopDetails> searchList ) {
        if(search.equals(""))
        {
            if(!(list.size()==searchList.size()))
            {
                this.list.clear();
                List<ShopDetails> empty = new ArrayList<>();
                for (int i=0; i< searchList.size(); i++) {
                    empty.add(searchList.get(i));
                }
                this.list=empty;
                notifyDataSetChanged();
            }

        }
        if(!search.equals(""))
        {

            List<ShopDetails>  temps = new ArrayList<>();
            for (int i=0; i< searchList.size(); i++) {

                if (searchList.get(i).getOwnerMobile().contains(search)) {
                    temps.add(searchList.get(i));
                }
                if (searchList.get(i).getOwnerName().toLowerCase().contains(search.toLowerCase()))
                {
                    temps.add(searchList.get(i));
                }
                if (searchList.get(i).getShopName().toLowerCase().contains(search.toLowerCase()))
                {
                    temps.add(searchList.get(i));
                }

            }
            //
            this.list = temps;
            notifyDataSetChanged();
        }

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
