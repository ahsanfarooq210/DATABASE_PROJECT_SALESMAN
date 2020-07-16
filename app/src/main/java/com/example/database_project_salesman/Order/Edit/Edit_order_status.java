package com.example.database_project_salesman.Order.Edit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import android.os.Bundle;
import android.widget.Toast;

import com.example.database_project_salesman.Order.Order_status_filter.OrderStatusRvAdapter;
import com.example.database_project_salesman.Order.Orders;
import com.example.database_project_salesman.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Edit_order_status extends AppCompatActivity
{
    //recycler view
    private RecyclerView recyclerView;

    //recycler view adapter
    private OrderStatusRvAdapter rvAdapter;

    //database reference
    private DatabaseReference reference;

    //list to store data
    private ArrayList<Orders> ordersArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order_status);
        recyclerView=findViewById(R.id.edit_order_status_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reference= FirebaseDatabase.getInstance().getReference("ORDERS");

        ordersArrayList=new ArrayList<>();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                ordersArrayList.clear();
                for(DataSnapshot order:snapshot.getChildren())
                {
                    ordersArrayList.add(order.getValue(Orders.class));
                }

                OrderStatusRvAdapter orderStatusRvAdapter=new OrderStatusRvAdapter(ordersArrayList,Edit_order_status.this);
                recyclerView.setAdapter(orderStatusRvAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(Edit_order_status.this,"Error in downloading the data",Toast.LENGTH_SHORT).show();
            }
        });

    }
}