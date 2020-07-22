package com.example.database_project_salesman.Order.Edit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.database_project_salesman.Order.Orders;
import com.example.database_project_salesman.R;
import com.example.database_project_salesman.SKU.Sku;
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
    private Spinner skuSpinner,statusSpinner;
    //array adapters for the dropdown lists
    private ArrayAdapter<Sku> skuArrayAdapter;

    private DatabaseReference skuReference,  orderReference;
    //array lists for the array adapters
    private List<Sku> skuList;

    private EditText quantity;

    private ArrayList<String> orderStatusList;
    //array aadapter for the order status spinner
    private ArrayAdapter<String> orderstatusArrayAdapter;


TextView edit_order_form_shop_TV;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order_form_activity);
        ordersList=new ArrayList<>();
        Intent intent=getIntent();
        progressBar=findViewById(R.id.edit_order_my_progress_bar);
        progressBarh.postDelayed(runnable1, 0);
        orderId=intent.getStringExtra("order_id");
        edit_order_form_shop_TV=findViewById(R.id.edit_order_form_shop_TV);
        skuSpinner=findViewById(R.id.edit_order_form_sku_spinner);
        saveButton=findViewById(R.id.edit_order_form_save_button);
        //initializing databasee reference for downloading and uploading the data the data
        skuReference = FirebaseDatabase.getInstance().getReference("SKU");

        orderReference = FirebaseDatabase.getInstance().getReference("ORDERS");

        //setting the array adapters
        statusSpinner=findViewById(R.id.edit_order_form_status_edit_text);
        orderStatusList=new ArrayList<>();
        orderStatusList.add(getString(R.string.delivered));
        orderStatusList.add(getString(R.string.in_progress));
        orderStatusList.add(getString(R.string.cancelled));

        orderstatusArrayAdapter=new ArrayAdapter(this,R.layout.spinner_text,orderStatusList);
        orderstatusArrayAdapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
        statusSpinner.setAdapter(orderstatusArrayAdapter);


        //initializing lists
        skuList = new ArrayList<>();


        //synchronizing the database for the offline use
        skuReference.keepSynced(true);

        orderReference.keepSynced(true);

        //initializing the quantity edit text
        quantity=findViewById(R.id.edit_order_form_quantity_edit_text);
        // quantity.setText();

        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progressBar.setVisibility(View.VISIBLE);
                if(quantity.getText().toString().trim().length()==0)
                {
                    quantity.setError("Please enter the quantity");
                    return;
                }

                Sku sku= (Sku) skuSpinner.getSelectedItem();

                int quant=Integer.parseInt(quantity.getText().toString().trim());
                String status=statusSpinner.getSelectedItem().toString();


                orderReference.child(orderId).child("sku").setValue(sku);
                orderReference.child(orderId).child("quantity").setValue(quant);
                orderReference.child(orderId).child("sku_id").setValue(sku.getId());
                orderReference.child(orderId).child("orderStatus").setValue(status);
                Toast.makeText(edit_order_form_activity.this,"Data Updated successfully",Toast.LENGTH_LONG).show();
                progressBarh.postDelayed(runnable1,200);


            }
        });
    }





    @Override
    protected void onStart()
    {
        super.onStart();

        //getting teh data of the particular order that the user clicked in the previous rcycler view
        Query query=FirebaseDatabase.getInstance().getReference().child("ORDERS").orderByKey().equalTo(orderId);

        query.addListenerForSingleValueEvent(new ValueEventListener()
        {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                ordersList.clear();
                for(DataSnapshot shop:dataSnapshot.getChildren())
                {
                    ordersList.add(shop.getValue(Orders.class));
                }

            quantity.setText(String.valueOf(ordersList.get(0).getQuantity()));
                String shopDetails="Shop Name : "+ordersList.get(0).getShopName()+"\n"
                                  +"Owner Name : "+ordersList.get(0).getShop().getOwnerName()+"\n"
                                  +"Owner Number : "+ordersList.get(0).getShop().getOwnerMobile();
                edit_order_form_shop_TV.setText(shopDetails);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(edit_order_form_activity.this, "Error in downloading the data", Toast.LENGTH_SHORT).show();
            }
        });

        // orders=ordersList.get(0);

        ////getting all the sku
        skuReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                skuList.clear();

                for(DataSnapshot sku:dataSnapshot.getChildren())
                {
                    skuList.add(sku.getValue(Sku.class));
                }
                skuArrayAdapter = new ArrayAdapter(edit_order_form_activity.this, R.layout.spinner_text, skuList);
                skuArrayAdapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
                skuSpinner.setAdapter(skuArrayAdapter);
                int sku;
                for ( sku=0; sku<skuList.size();sku++)
                {
                    if(skuList.get(sku).getId().equals(ordersList.get(0).getSku_id()))
                    {
                        break;
                    }
                }
                skuSpinner.setSelection(sku);


                    if(ordersList.get(0).getOrderStatus().equals(getResources().getString(R.string.delivered)))
                    {
                        statusSpinner.setSelection(0);
                    }
               else if(ordersList.get(0).getOrderStatus().equals(getResources().getString(R.string.in_progress)))
                {
statusSpinner.setSelection(1);
                }else  if(ordersList.get(0).getOrderStatus().equals(getResources().getString(R.string.cancelled)))
            {
statusSpinner.setSelection(2);

            }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(edit_order_form_activity.this, "Error in download ing the data", Toast.LENGTH_SHORT).show();
            }
        });







    }

}