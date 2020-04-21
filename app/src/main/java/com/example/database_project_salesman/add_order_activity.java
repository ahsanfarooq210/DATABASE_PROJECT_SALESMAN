package com.example.database_project_salesman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class add_order_activity extends AppCompatActivity
{


    //database reference
    private DatabaseReference skuReference,shopReference,orderReference;

    //splash screen relative layout
    private RelativeLayout rellay1,rally3,rellay2;

    //array adapters for the dropdown lists
    private ArrayAdapter<Sku> skuArrayAdapter;
    private ArrayAdapter<ShopDetails> shopDetailsArrayAdapter;

    //message text view
    private TextView message;

    //dropdowns spinners
    private Spinner shopSpinner,skuSpinner;

    //handler for the splash screen
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            rellay1.setVisibility(View.VISIBLE);
            rally3.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);

        }
    };

    //handler for the progress bar
    private ProgressBar progressBar;
    private Handler progressBarh=new Handler();
    private Runnable runnable1=new Runnable()
    {
        @Override
        public void run()
        {

            progressBar.setVisibility(View.GONE);
        }
    };

    //array lists for the array adapters
    private List<Sku> skuList;
    private List<ShopDetails> shopDetailsList;
    //buttons and edit texts
    private Button save;
    private EditText quantity;
    //to get the email id of the salesman
    private FirebaseAuth auth;
    private FirebaseUser user;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order_activity);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        //initializing lists
        skuList=new ArrayList<>();
        shopDetailsList=new ArrayList<>();

        //initializing databasee reference for downloading and uploading the data the data
        skuReference= FirebaseDatabase.getInstance().getReference("SKU");
        shopReference=FirebaseDatabase.getInstance().getReference("SHOP");
        orderReference=FirebaseDatabase.getInstance().getReference("ORDERS");
        //synchronizing the database for the offline use
        skuReference.keepSynced(true);
        shopReference.keepSynced(true);
        orderReference.keepSynced(true);

        //initializing the quantity text view
        quantity=findViewById(R.id.add_order_quantity_et);

        //relative layouts
        rellay1=findViewById(R.id.adding_sku_rellay1);
        rellay2=findViewById(R.id.adding_sku_rellay2);
        rally3 =findViewById(R.id.adding_sku_bottom_rally2);

        //splash screen
        handler.postDelayed(runnable, 500);

        //message text view
        message=findViewById(R.id.message_text_view);


        //progress bar
        progressBar=findViewById(R.id.my_progress_bar);
        progressBarh.postDelayed(runnable1,0);

        //initializing the spinners
        shopSpinner=findViewById(R.id.add_order_shop_spinner);
        skuSpinner=findViewById(R.id.add_order_sku_spinner);

        //setting the array adapters
        skuArrayAdapter=new ArrayAdapter(this, R.layout.spinner_text,skuList);
        skuArrayAdapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
        shopDetailsArrayAdapter=new ArrayAdapter(this,R.layout.spinner_text,shopDetailsList);
        shopDetailsArrayAdapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
        save=findViewById(R.id.add_order_save_btn);
        save.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                progressBar.setVisibility(View.VISIBLE);
                if(quantity.getText().toString().length()==0)
                {
                    quantity.setError("Enter quantity");
                    return;
                }

                Sku sk=(Sku)skuSpinner.getSelectedItem();
                ShopDetails shopDetails=(ShopDetails)shopSpinner.getSelectedItem();
                String username;
                if(user!=null)
                {
                     username=user.getEmail();
                }
                else
                {
                    username="unknown";
                }

                String id=orderReference.push().getKey();
                Orders order =new Orders(id,username,shopDetails,sk,Integer.parseInt(quantity.getText().toString().trim()));
                if(id!=null)
                {
                    orderReference.child(id).setValue(order);

                }

                progressBarh.postDelayed(runnable1,500);

            }
        });


    }

    @Override
    protected void onStart()
    {
        super.onStart();
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
                skuSpinner.setAdapter(skuArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(add_order_activity.this, "Error in download ing the data", Toast.LENGTH_SHORT).show();
            }
        });

        shopReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                shopDetailsList.clear();
                for(DataSnapshot shop:dataSnapshot.getChildren())
                {
                    shopDetailsList.add(shop.getValue(ShopDetails.class));
                }
                shopSpinner.setAdapter(shopDetailsArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(add_order_activity.this,"Error in downloading the data",Toast.LENGTH_SHORT).show();
            }

        });


    }
}
