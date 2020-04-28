package com.example.database_project_salesman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import com.example.database_project_salesman.BrodcastReceivers.LocationBrodcast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class add_order_activity extends AppCompatActivity implements LocationListener
{

    //brodcast receiver
    private LocationBrodcast locationBrodcast;

    //database reference
    private DatabaseReference skuReference, shopReference, orderReference;

    //splash screen relative layout
    private RelativeLayout rellay1, rally3, rellay2;

    //array adapters for the dropdown lists
    private ArrayAdapter<Sku> skuArrayAdapter;
    private ArrayAdapter<ShopDetails> shopDetailsArrayAdapter;

    //message text view
    private TextView message;

    //dropdowns spinners
    private Spinner shopSpinner, skuSpinner;

    //handler for the splash screen
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {

            rellay1.setVisibility(View.VISIBLE);
            rally3.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);

        }
    };

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

    //array lists for the array adapters
    private List<Sku> skuList;
    private List<ShopDetails> shopDetailsList;
    //buttons and edit texts
    private Button save;
    private EditText quantity;
    //to get the email id of the salesman
    private FirebaseAuth auth;
    private FirebaseUser user;
    //variables to store current location latlang
    private double latitude, longitude;
    //location managet ot get the current location
   // private LocationManager locationManager;
    private FusedLocationProviderClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order_activity);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        //initializing the brodcast receiver
        locationBrodcast = new LocationBrodcast();

        //initializing lists
        skuList = new ArrayList<>();
        shopDetailsList = new ArrayList<>();

        //initializing databasee reference for downloading and uploading the data the data
        skuReference = FirebaseDatabase.getInstance().getReference("SKU");
        shopReference = FirebaseDatabase.getInstance().getReference("SHOP");
        orderReference = FirebaseDatabase.getInstance().getReference("ORDERS");
        //synchronizing the database for the offline use
        skuReference.keepSynced(true);
        shopReference.keepSynced(true);
        orderReference.keepSynced(true);

        //initializing the quantity text view
        quantity = findViewById(R.id.add_order_quantity_et);

        //relative layouts
        rellay1 = findViewById(R.id.adding_sku_rellay1);
        rellay2 = findViewById(R.id.adding_sku_rellay2);
        rally3 = findViewById(R.id.adding_sku_bottom_rally2);

        //splash screen
        handler.postDelayed(runnable, 500);

        //message text view
        message = findViewById(R.id.message_text_view);


        //progress bar
        progressBar = findViewById(R.id.my_progress_bar);
        progressBarh.postDelayed(runnable1, 0);

        //initializing the spinners
        shopSpinner = findViewById(R.id.add_order_shop_spinner);
        skuSpinner = findViewById(R.id.add_order_sku_spinner);

        //setting the array adapters
        skuArrayAdapter = new ArrayAdapter(this, R.layout.spinner_text, skuList);
        skuArrayAdapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
        shopDetailsArrayAdapter = new ArrayAdapter(this, R.layout.spinner_text, shopDetailsList);
        shopDetailsArrayAdapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
        //initializing the location manager to get the current latitude and longitude
       // locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        client= LocationServices.getFusedLocationProviderClient(this);

        //save button initialization and on click listinner
        save=findViewById(R.id.add_order_save_btn);
        save.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                requestPermission();
                client.getLastLocation().addOnSuccessListener(add_order_activity.this, new OnSuccessListener<Location>()
                {
                    @Override
                    public void onSuccess(Location location)
                    {
                        if(location!=null)
                        {
                            latitude=location.getLatitude();
                            longitude=location.getLongitude();
                        }
                    }
                });

//                if(!locationBrodcast.status)
//                {
//                    Toast.makeText(add_order_activity.this, "Turn on location", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                progressBar.setVisibility(View.VISIBLE);
                if(quantity.getText().toString().length()==0)
                {
                    quantity.setError("Enter quantity");
                    progressBarh.postDelayed(runnable1,500);
                    return;
                }

                Sku sk=(Sku)skuSpinner.getSelectedItem();
                ShopDetails shopDetails=(ShopDetails)shopSpinner.getSelectedItem();
                if(distance(shopDetails.getLatitude(),shopDetails.getLongitude(),latitude,longitude)>500)
                {
                    Toast.makeText(add_order_activity.this, "you need to be in 500 meters of the shop \n cirrent distance "+distance(shopDetails.getLatitude(),shopDetails.getLongitude(),latitude,longitude), Toast.LENGTH_SHORT).show();
                    progressBarh.postDelayed(runnable1,500);
                    return;
                }
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
                else
                {
                    Toast.makeText(add_order_activity.this, "Error \n string id=null \n contact developer immediately", Toast.LENGTH_SHORT).show();
                }

                progressBarh.postDelayed(runnable1,500);



            }
        });


//        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        onLocationChanged(location);


    }

    @Override
    protected void onStart()
    {
        IntentFilter intentFilter=new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        registerReceiver(locationBrodcast,intentFilter);

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

    @Override
    protected void onStop()
    {
        super.onStop();
        unregisterReceiver(locationBrodcast);
    }

    @Override
    public void onLocationChanged(Location location)
    {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }

    //returns distance in meters
    public  double distance(double lat1, double lng1,
                                  double lat2, double lng2){
        double a = (lat1-lat2)*distPerLat(lat1);
        double b = (lng1-lng2)*distPerLng(lat1);
        return Math.sqrt(a*a+b*b);
    }

    private  double distPerLng(double lat){
        return 0.0003121092*Math.pow(lat, 4)
                +0.0101182384*Math.pow(lat, 3)
                -17.2385140059*lat*lat
                +5.5485277537*lat+111301.967182595;
    }

    private  double distPerLat(double lat){
        return -0.000000487305676*Math.pow(lat, 4)
                -0.0033668574*Math.pow(lat, 3)
                +0.4601181791*lat*lat
                -1.4558127346*lat+110579.25662316;
    }
    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
    }
}
