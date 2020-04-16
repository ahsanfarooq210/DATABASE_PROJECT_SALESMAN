package com.example.database_project_salesman;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class salesname_main_dashboard extends AppCompatActivity
{
    //employeee name
    private FirebaseUser user;
    private FirebaseAuth auth;
    private TextView dashboard_nameplate;
    //scroll view
    private ScrollView dashboardScrollView;
    //shpash screen handler
    private Handler handler=new Handler();
    Runnable runnable=new Runnable()
    {
        @Override
        public void run()
        {
            dashboard_nameplate.setVisibility(View.VISIBLE);
            dashboardScrollView.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesname_main_dashboard);
        dashboard_nameplate=findViewById(R.id.dashboard_name_plate);
        dashboardScrollView=findViewById(R.id.dashboard_scroll_view);
        //splash screen
        handler.postDelayed(runnable,500);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        //displaying the name of the user
        dashboard_nameplate.setText(user.getEmail().trim());
    }

    public void addorder(View view)
    {
    }

    public void showorder(View view)
    {
    }
}
