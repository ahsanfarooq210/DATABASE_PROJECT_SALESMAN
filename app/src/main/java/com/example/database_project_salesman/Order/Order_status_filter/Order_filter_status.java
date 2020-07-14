package com.example.database_project_salesman.Order.Order_status_filter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.database_project_salesman.R;

import java.util.ArrayList;

public class Order_filter_status extends AppCompatActivity
{
    private Spinner statisSpinner;
    //string to store all the status
    private ArrayList<String> orderStatusList;
    //array aadapter for the order status spinner
    private ArrayAdapter<String> orderstatusArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_filter_status);

        //initializing the status spinner
        statisSpinner=findViewById(R.id.filter_status_spinner);
        //adding strings in the order status list
        orderStatusList=new ArrayList<>();
        orderStatusList.add(getString(R.string.delivered));
        orderStatusList.add(getString(R.string.in_progress));
        orderStatusList.add(getString(R.string.cancelled));

        //initializing the orderstatus array adapter
        orderstatusArrayAdapter=new ArrayAdapter(this,R.layout.spinner_text,orderStatusList);
        orderstatusArrayAdapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
        statisSpinner.setAdapter(orderstatusArrayAdapter);


    }

    public void filter(View view)
    {

    }
}