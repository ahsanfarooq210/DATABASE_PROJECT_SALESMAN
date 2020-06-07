package com.example.database_project_salesman.ShopActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.database_project_salesman.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class show_shop_activity extends AppCompatActivity
{
    private DatabaseReference shops;
    private ArrayList<ShopDetails> list;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_shop_activity);

        shops= FirebaseDatabase.getInstance().getReference("SHOP");
        shops.keepSynced(true);
        list=new ArrayList<>();
        recyclerView=findViewById(R.id.show_shops_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart()
    {
        super.onStart();

        shops.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                list.clear();
                for(DataSnapshot shop:dataSnapshot.getChildren())
                {
                    list.add(shop.getValue(ShopDetails.class));
                }

                show_shops_rv_adapter adapter=new show_shops_rv_adapter(show_shop_activity.this,list);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(show_shop_activity.this, "Error in downloading the data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
