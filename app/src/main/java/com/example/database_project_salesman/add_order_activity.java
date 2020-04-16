package com.example.database_project_salesman;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add_order_activity extends AppCompatActivity
{


    //database reference
    private DatabaseReference skuReference,shopReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order_activity);

        //initializing databasee reference for importing the data
        skuReference= FirebaseDatabase.getInstance().getReference("SKU");
        shopReference=FirebaseDatabase.getInstance().getReference("SHOP");
        //synchronizing the database for the offline use
        skuReference.keepSynced(true);
        shopReference.keepSynced(true);


    }

    @Override
    protected void onStart()
    {
        super.onStart();


    }
}
