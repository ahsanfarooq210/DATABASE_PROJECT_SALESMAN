package com.example.database_project_salesman.Order.Edit;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.database_project_salesman.Order.Orders;
import com.example.database_project_salesman.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class edit_order_rv_activity extends AppCompatActivity
{
    //making the instance of the recycler view
    private RecyclerView recyclerView;
    private DatabaseReference shops;
    private ArrayList<Orders> orderList;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order_activity);

        recyclerView=findViewById(R.id.edit_order_recycler_view);
        orderList=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        shops= FirebaseDatabase.getInstance().getReference("SHOP");

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        Query query= FirebaseDatabase.getInstance().getReference("ORDERS").orderByChild("salesman").equalTo(user.getEmail());

        query.addListenerForSingleValueEvent(new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                orderList.clear();
                for(DataSnapshot shop:dataSnapshot.getChildren())
                {
                    orderList.add(shop.getValue(Orders.class));
                }
                EditOrderRvAdapter editOrderRvAdapter=new EditOrderRvAdapter(orderList, edit_order_rv_activity.this);
                recyclerView.setAdapter(editOrderRvAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(edit_order_rv_activity.this, "Error in downloading the data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
