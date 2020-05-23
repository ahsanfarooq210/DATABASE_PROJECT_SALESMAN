package com.example.database_project_salesman;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.location.LocationManagerCompat;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
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
        Intent intent=new Intent(salesname_main_dashboard.this,add_order_activity.class);
        startActivity(intent);

    }

    public void showorder(View view)
    {
        Intent intent=new Intent(salesname_main_dashboard.this,show_orders_activity.class);
        startActivity(intent);
    }
    public void showshops(View view)
    {
        Intent intent=new Intent(salesname_main_dashboard.this,show_shop_activity.class);
        startActivity(intent);

    }

    public void showShopsOnMap(View view)
    {
        if(isLocationEnabled(this)) {
            Intent intent = new Intent(salesname_main_dashboard.this, show_shop_on_map_activity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    private boolean isLocationEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return LocationManagerCompat.isLocationEnabled(locationManager);
    }

    public void editOrder(View view)
    {
        startActivity(new Intent(salesname_main_dashboard.this,edit_order_rv_activity.class));
    }
}

