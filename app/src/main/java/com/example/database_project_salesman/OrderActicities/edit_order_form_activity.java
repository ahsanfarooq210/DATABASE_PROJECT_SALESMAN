package com.example.database_project_salesman.OrderActicities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.database_project_salesman.R;
import com.example.database_project_salesman.ShopActivities.ShopDetails;
import com.example.database_project_salesman.SKUActivities.Sku;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class edit_order_form_activity extends AppCompatActivity
{
    private String orderId;
    private List<Orders> ordersList;
    private Orders orders;
    //handler for the progress bar
    private ProgressBar progressBar;
    private Handler progressBarh = new Handler();
    private Runnable runnable1 = new Runnable()
    {
        @Override
        public void run()
        {

            progressBar.setVisibility(View.GONE);
        }
    };

    private Button saveButton;
    private Spinner shopSpinner,skuSpinner;
    //array adapters for the dropdown lists
    private ArrayAdapter<Sku> skuArrayAdapter;
    private ArrayAdapter<ShopDetails> shopDetailsArrayAdapter;
    //database reference
    private DatabaseReference skuReference, shopReference, orderReference;
    //array lists for the array adapters
    private List<Sku> skuList;
    private List<ShopDetails> shopDetailsList;
    private EditText quantity;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order_form_activity);
        ordersList=new ArrayList<>();
        Intent intent=getIntent();
        orderId=intent.getStringExtra("order_id");
        shopSpinner=findViewById(R.id.edit_order_form_shop_spiner);
        skuSpinner=findViewById(R.id.edit_order_form_sku_spinner);
        saveButton=findViewById(R.id.edit_order_form_save_button);
        //initializing databasee reference for downloading and uploading the data the data
        skuReference = FirebaseDatabase.getInstance().getReference("SKU");
        shopReference = FirebaseDatabase.getInstance().getReference("SHOP");
        orderReference = FirebaseDatabase.getInstance().getReference("ORDERS");

        //setting the array adapters
        skuArrayAdapter = new ArrayAdapter(this, R.layout.spinner_text, skuList);
        skuArrayAdapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
        shopDetailsArrayAdapter = new ArrayAdapter(this, R.layout.spinner_text, shopDetailsList);
        shopDetailsArrayAdapter.setDropDownViewResource(R.layout.spinner_text_dropdown);

        //initializing lists
        skuList = new ArrayList<>();
        shopDetailsList = new ArrayList<>();

        //synchronizing the database for the offline use
        skuReference.keepSynced(true);
        shopReference.keepSynced(true);
        orderReference.keepSynced(true);

        //initializing the quantity edit text
        quantity=findViewById(R.id.edit_order_form_quantity_edit_text);


        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(quantity.getText().toString().trim().length()==0)
                {
                    quantity.setError("Please enter the quantity");
                    return;
                }

                Sku sku= (Sku) skuSpinner.getSelectedItem();
                ShopDetails shopDetails= (ShopDetails) shopSpinner.getSelectedItem();
                int quant=Integer.parseInt(quantity.getText().toString().trim());

                if (!isSkuSame())
                {
                    orderReference.child(orderId).child("sku").setValue(sku);
                    Toast.makeText(edit_order_form_activity.this, "sku updated", Toast.LENGTH_SHORT).show();
                }
                if(!isShopSame())
                {
                    orderReference.child(orderId).child("shop").setValue(shopDetails);
                    Toast.makeText(edit_order_form_activity.this, "shop updated", Toast.LENGTH_SHORT).show();
                }
                if(!isQuantitySame())
                {
                    orderReference.child(orderId).child("quantity").setValue(quant);
                    Toast.makeText(edit_order_form_activity.this, "quantity updated", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }





    @Override
    protected void onStart()
    {
        super.onStart();

        //getting teh data of the particular order that the user clicked in the previous rcycler view
        Query query= FirebaseDatabase.getInstance().getReference("ORDERS").orderByChild("id").equalTo(orderId);
        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                ordersList.clear();
                for(DataSnapshot orders:dataSnapshot.getChildren())
                {
                    ordersList.add(orders.getValue(Orders.class));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(edit_order_form_activity.this, "Error in downloading the data", Toast.LENGTH_SHORT).show();
            }
        });

        orders=ordersList.get(0);

        ////getting all the sku
        skuReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                skuList.clear();
                skuList.add(ordersList.get(0).getSku());
                for(DataSnapshot sku:dataSnapshot.getChildren())
                {
                    skuList.add(sku.getValue(Sku.class));
                }
                skuSpinner.setAdapter(skuArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(edit_order_form_activity.this, "Error in download ing the data", Toast.LENGTH_SHORT).show();
            }
        });

        //getting all the shops
        shopReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                shopDetailsList.clear();
                shopDetailsList.add(ordersList.get(0).getShop());
                for(DataSnapshot shop:dataSnapshot.getChildren())
                {
                    shopDetailsList.add(shop.getValue(ShopDetails.class));
                }
                shopSpinner.setAdapter(shopDetailsArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(edit_order_form_activity.this,"Error in downloading the data",Toast.LENGTH_SHORT).show();
            }

        });


    }


    private boolean isSkuSame()
    {
        return (Sku) skuSpinner.getSelectedItem() == ordersList.get(0).getSku();
    }
    private boolean isShopSame()
    {
        return (ShopDetails) shopSpinner.getSelectedItem() == ordersList.get(0).getShop();
    }
    private boolean isQuantitySame()
    {
        return Integer.parseInt(quantity.getText().toString().trim()) == ordersList.get(0).getQuantity();
    }
}
