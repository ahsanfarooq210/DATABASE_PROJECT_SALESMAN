package com.example.database_project_salesman.Order.Order_status_filter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.database_project_salesman.Order.Orders;
import com.example.database_project_salesman.Order.show_order_rv_adaprter;
import com.example.database_project_salesman.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Order_filter_status extends AppCompatActivity
{

    //list to store filtered orders
    private ArrayList<Orders> orderList;
    //initializing the recycler view
    private RecyclerView recyclerView;

    private FirebaseAuth auth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_filter_status);




        //initializing the order list
        orderList=new ArrayList<>();

        //initialiing the recycler view
        recyclerView=findViewById(R.id.filter_status_revuvler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        Handler handler=new Handler();
        handler.postDelayed(runnable,0);


    }


    Runnable runnable=new Runnable()
    {
        @Override
        public void run()
        {

            Query  query=FirebaseDatabase.getInstance().getReference("ORDERS").orderByChild("salesman").equalTo(user.getEmail());
            query.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    orderList.clear();
                    for(DataSnapshot orderSnap:snapshot.getChildren())
                    {
                        orderList.add(orderSnap.getValue(Orders.class));
                    }
                    show_order_rv_adaprter showOrderRvAdaprter=new show_order_rv_adaprter(orderList,Order_filter_status.this);
                    recyclerView.setAdapter(showOrderRvAdaprter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {
                    Toast.makeText(Order_filter_status.this, "Error in downloading the data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
}