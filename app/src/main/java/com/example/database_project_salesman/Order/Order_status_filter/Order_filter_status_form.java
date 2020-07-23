package com.example.database_project_salesman.Order.Order_status_filter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.database_project_salesman.Order.Entity.Orders;
import com.example.database_project_salesman.R;

import com.example.database_project_salesman.Target.Enity.Target_SalesMen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Order_filter_status_form extends AppCompatActivity
{
    private Spinner spinner;

    private ArrayList<String> orderStatusList;
    //array aadapter for the order status spinner
    private ArrayAdapter<String> orderstatusArrayAdapter;
    //order id
    private String order_id;
    private  List<Orders> ordersList;
    //database reference
    private DatabaseReference reference, targetSalesMenReference;
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
    //target salesman
    private List<Target_SalesMen> target_salesMenList;
    //to get the email id of the salesman
    private FirebaseAuth auth;
    private FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_filter_status_form);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        spinner=findViewById(R.id.order_filter_status_form_status_spinner);

        orderStatusList=new ArrayList<>();
        orderStatusList.add(getString(R.string.delivered));
        orderStatusList.add(getString(R.string.in_progress));
        orderStatusList.add(getString(R.string.cancelled));
        progressBar=findViewById(R.id.order_filter_my_progress_bar);
        progressBarh.postDelayed(runnable1,0);
        orderstatusArrayAdapter=new ArrayAdapter(this,R.layout.spinner_text,orderStatusList);
        orderstatusArrayAdapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
        spinner.setAdapter(orderstatusArrayAdapter);
        Intent intent=getIntent();
        order_id=intent.getStringExtra("order_id");
        target_salesMenList=new ArrayList<>();
        ordersList=new ArrayList<>();
        reference= FirebaseDatabase.getInstance().getReference().child("ORDERS");
        targetSalesMenReference=FirebaseDatabase.getInstance().getReference().child("TargetSalesMan");

    }

    public void save(View view)
    {
        progressBar.setVisibility(View.VISIBLE);
        String status=spinner.getSelectedItem().toString();


        if(status.equals(ordersList.get(0).getOrderStatus()))
        {
            Toast.makeText(Order_filter_status_form.this,"Nothing is changed ",Toast.LENGTH_LONG).show();
            progressBarh.postDelayed(runnable1,200);
            return;
        }
        if(!(status.equals(ordersList.get(0).getOrderStatus())) )
        {
            String target_SalesManID=null;
            int previousTargetAchieved=0;
            int targetAchieved=0;
            int ts;
            String orderSkuId="";

            orderSkuId=ordersList.get(0).getSku_id();

            for (ts = 0; ts < target_salesMenList.size(); ts++) {
                if (target_salesMenList.get(ts).getSKU_ID().equals(orderSkuId)) {
                    target_SalesManID = target_salesMenList.get(ts).getTARGET_ID();
                    targetAchieved = target_salesMenList.get(ts).getAchieved();
                    break;
                }
            }

            if(status.equals(getString(R.string.cancelled)))
            {
                targetAchieved-=ordersList.get(0).getQuantity();

            }

            if ( (status.equals(getString(R.string.delivered))) ||  (status.equals(getString(R.string.in_progress))) )
            {
                targetAchieved+=ordersList.get(0).getQuantity();
            }

           targetSalesMenReference.child(target_SalesManID).child("achieved").setValue(targetAchieved);
            reference.child(order_id).child("orderStatus").setValue(status);
            progressBarh.postDelayed(runnable1,200);
            Toast.makeText(this, "Status changed successfully", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        targetSalesMenReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                target_salesMenList.clear();
                for (DataSnapshot targetSalesman:snapshot.getChildren()) {
                    if ((targetSalesman.getValue(Target_SalesMen.class).getSaleMenEmail().equals(user.getEmail() ) )
                            && (targetSalesman.getValue(Target_SalesMen.class).getStatus().equals("Active") ) ){
                        target_salesMenList.add(targetSalesman.getValue(Target_SalesMen.class));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ordersList.clear();
                for (DataSnapshot orderStatus : snapshot.getChildren()) {
                    if (orderStatus.getValue(Orders.class).getId().equals(order_id)) {
                        ordersList.add(orderStatus.getValue(Orders.class));
                    }
                }
                if(ordersList.get(0).getOrderStatus().equals(getString(R.string.delivered)))
                {
                    spinner.setSelection(0);
                }
                if(ordersList.get(0).getOrderStatus().equals(getString(R.string.in_progress)))
                {
                    spinner.setSelection(1);
                }
                if(ordersList.get(0).getOrderStatus().equals(getString(R.string.cancelled)))
                {
                    spinner.setSelection(2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}