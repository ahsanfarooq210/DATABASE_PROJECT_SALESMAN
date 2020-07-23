package com.example.database_project_salesman.Order.Add_Order;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.location.LocationManagerCompat;


import com.example.database_project_salesman.BrodcastReceivers.LocationBrodcast;
import com.example.database_project_salesman.Order.Entity.Orders;
import com.example.database_project_salesman.Order.Show_Order.show_order_shop_on_map;
import com.example.database_project_salesman.R;
import com.example.database_project_salesman.SHOP.Entity.ShopDetails;
import com.example.database_project_salesman.SKU.Sku;

import com.example.database_project_salesman.Target.Enity.Target_SalesMen;
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
    private DatabaseReference skuReference, shopReference, orderReference, targetSaleseMenRefernce;

    //splash screen relative layout
    private RelativeLayout rellay1, rally3, rellay2;

    //array adapters for the dropdown lists
    private ArrayAdapter<Sku> skuArrayAdapter;
    private ArrayAdapter<ShopDetails> shopDetailsArrayAdapter;

    //message text view
    private TextView message;

    //dropdowns spinners
    private Spinner shopSpinner, skuSpinner,orderStatusSpinner;

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
    private  List<Target_SalesMen> target_salesMenList;
    //buttons and edit texts
    private Button save, showShop;
    private EditText quantity;
    //to get the email id of the salesman
    private FirebaseAuth auth;
    private FirebaseUser user;
    //variables to store current location latlang
    private double latitude, longitude;
    //location managet ot get the current location
    // private LocationManager locationManager;
    private FusedLocationProviderClient client;
    ShopDetails shopDetails;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 911;
    //arraylist for the order status spinner
    private ArrayList<String> orderStatusList;
    //array aadapter for the order status spinner
    private ArrayAdapter<String> orderstatusArrayAdapter;

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
        target_salesMenList =new ArrayList<>();

        //initializing databasee reference for downloading and uploading the data the data
        skuReference = FirebaseDatabase.getInstance().getReference("SKU");
        shopReference = FirebaseDatabase.getInstance().getReference("SHOP");
        orderReference = FirebaseDatabase.getInstance().getReference("ORDERS");

        targetSaleseMenRefernce=FirebaseDatabase.getInstance().getReference("TargetSalesMan");
        //synchronizing the database for the offline use
        skuReference.keepSynced(true);
        shopReference.keepSynced(true);
        orderReference.keepSynced(true);

        targetSaleseMenRefernce.keepSynced(true);
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
        orderStatusSpinner=findViewById(R.id.add_order_status);

        //setting the array adapters
        skuArrayAdapter = new ArrayAdapter(this, R.layout.spinner_text, skuList);
        skuArrayAdapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
        shopDetailsArrayAdapter = new ArrayAdapter(this, R.layout.spinner_text, shopDetailsList);
        shopDetailsArrayAdapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
        //initializing the location manager to get the current latitude and longitude
        // locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        client = LocationServices.getFusedLocationProviderClient(this);
        //adding strings in the order status list
        orderStatusList=new ArrayList<>();
    //    orderStatusList.add(getString(R.string.delivered));
        orderStatusList.add(getString(R.string.in_progress));
      //  orderStatusList.add(getString(R.string.cancelled));
        //initializing the orderstatus array adapter
        orderstatusArrayAdapter=new ArrayAdapter(this,R.layout.spinner_text,orderStatusList);
        orderstatusArrayAdapter.setDropDownViewResource(R.layout.spinner_text_dropdown);
        orderStatusSpinner.setAdapter(orderstatusArrayAdapter);

        //save button initialization and on click listinner
        save = findViewById(R.id.add_order_save_btn);
        save.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                requestPermission();
                //comment
                //hggh

                if (ActivityCompat.checkSelfPermission(add_order_activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(add_order_activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                client.getLastLocation().addOnSuccessListener(add_order_activity.this, new OnSuccessListener<Location>()
                {
                    @Override
                    public void onSuccess(Location location)
                    {
                        if (location != null)
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
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
                    quantity.setError(getString(R.string.enter_quantity));
                    progressBarh.postDelayed(runnable1,500);
                    return;
                }

                Sku sk=(Sku)skuSpinner.getSelectedItem();
                ShopDetails shopDetails=(ShopDetails)shopSpinner.getSelectedItem();
                double dist=distance(shopDetails.getLatitude(),shopDetails.getLongitude(),latitude,longitude);
                if(dist>500.00)
                {
                    Toast.makeText(add_order_activity.this, "you need to be in 500 meters of the shop \n current distance "+distance(latitude,longitude,shopDetails.getLatitude(),shopDetails.getLongitude()), Toast.LENGTH_SHORT).show();
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

                String OrderStatus=orderStatusSpinner.getSelectedItem().toString();
                //orders
                String order_id=orderReference.push().getKey();
                Orders order =new Orders(order_id,username,shopDetails,sk,Integer.parseInt(quantity.getText().toString().trim()),OrderStatus);
              //Target_salesMen
                String targetSalesmenID=targetSaleseMenRefernce.push().getKey();
                int previousTargetAchieved=0;
                int targetAchieved=0;
                if(target_salesMenList.size()==0)
                {
                    previousTargetAchieved=targetAchieved=Integer.parseInt(quantity.getText().toString().trim());

                    Target_SalesMen target_salesMen=new Target_SalesMen(targetSalesmenID,sk.getId(),targetAchieved,username,sk,"Active");

                    if(targetSalesmenID!=null)
                    {

                        targetSaleseMenRefernce.child(targetSalesmenID).setValue(target_salesMen);
                    }
                    else
                    {
                        Toast.makeText(add_order_activity.this, "Error \n string id=null \n contact developer immediately", Toast.LENGTH_SHORT).show();
                    }
                    quantity.setText("");
                    skuSpinner.setSelection(0);
                    shopSpinner.setSelection(0);
                    progressBarh.postDelayed(runnable1,500);
                    return;
                }
                if(target_salesMenList.size()!=0) {
                    boolean notFound=true;
                    for(int sku=0; sku<target_salesMenList.size(); sku++)
                    {
                        if (sk.getId().equals(target_salesMenList.get(sku).getSKU_ID())) {

                            targetAchieved   = target_salesMenList.get(sku).getAchieved();
                            targetAchieved += Integer.parseInt(quantity.getText().toString().trim());


                            String target_salesMenID = target_salesMenList.get(sku).getTARGET_ID();

                            if (target_salesMenID != null && order_id != null) {
                                orderReference.child(order_id).setValue(order);
                                targetSaleseMenRefernce.child(target_salesMenID).child("achieved").setValue(targetAchieved);
                            } else {
                                Toast.makeText(add_order_activity.this, "Error \n string id=null \n contact developer immediately", Toast.LENGTH_SHORT).show();
                            }
                           notFound=false;
                            break;
                        }
                    }


                    if(notFound)
                    {

                        Target_SalesMen target_salesMen=new Target_SalesMen(targetSalesmenID,sk.getId(),targetAchieved,username,sk,"Active");

                        if(order_id!=null&&targetSalesmenID!=null)
                        {
                            orderReference.child(order_id).setValue(order);
                            targetSaleseMenRefernce.child(targetSalesmenID).setValue(target_salesMen);
                        }
                        else
                        {
                            Toast.makeText(add_order_activity.this, "Error \n string id=null \n contact developer immediately", Toast.LENGTH_SHORT).show();
                        }

                    }
                    quantity.setText("");
                    skuSpinner.setSelection(0);
                    shopSpinner.setSelection(0);
                    progressBarh.postDelayed(runnable1, 500);
                    return;
                }



            }
        });


//        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        onLocationChanged(location);

        //initializing the show shop button
        showShop=findViewById(R.id.shop_shop_btn);
        showShop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                shopDetails=(ShopDetails)shopSpinner.getSelectedItem();
                if(isLocationEnabled(add_order_activity.this)) {

                    if (checkPermission()) {
                        Intent intent = new Intent(add_order_activity.this, show_order_shop_on_map.class);
                        intent.putExtra("latitude",shopDetails.getLatitude());
                        intent.putExtra("longitude",shopDetails.getLongitude());
                        startActivity(intent);

                    } else {

                        requestPermission();
                    }
                }else{
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }


            }
        });



    }
    private boolean isLocationEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return LocationManagerCompat.isLocationEnabled(locationManager);
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);

        return result == PackageManager.PERMISSION_GRANTED ;
    }

    private void requestLoctionPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, },MY_PERMISSIONS_REQUEST_LOCATION);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (locationAccepted ) {
                        Intent intent=new Intent(add_order_activity.this, show_order_shop_on_map.class);
                        intent.putExtra("latitude",shopDetails.getLatitude());
                        intent.putExtra("longitude",shopDetails.getLongitude());
                        startActivity(intent);
                        //Snackbar.make(getCurrentFocus(), "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
                    }else {

                        // Snackbar.make(getCurrentFocus(), "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow the permissions access to Location",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(add_order_activity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
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
targetSaleseMenRefernce.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        target_salesMenList.clear();
        for (DataSnapshot targetSalesman:snapshot.getChildren()) {
            if ( (targetSalesman.getValue(Target_SalesMen.class).getSaleMenEmail().equals(user.getEmail()))
            && targetSalesman.getValue(Target_SalesMen.class).getStatus().equals("Active")){
                target_salesMenList.add(targetSalesman.getValue(Target_SalesMen.class));
            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

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

    private boolean isGpsEnabled(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        // Find out what the settings say about which providers are enabled
        //  String locationMode = "Settings.Secure.LOCATION_MODE_OFF";
        int mode = Settings.Secure.getInt(
                contentResolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF);
        if (mode != Settings.Secure.LOCATION_MODE_OFF) {
            return true;
               /* if (mode == Settings.Secure.LOCATION_MODE_HIGH_ACCURACY) {
                    locationMode = "High accuracy. Uses GPS, Wi-Fi, and mobile networks to determine location";
                } else if (mode == Settings.Secure.LOCATION_MODE_SENSORS_ONLY) {
                    locationMode = "Device only. Uses GPS to determine location";
                } else if (mode == Settings.Secure.LOCATION_MODE_BATTERY_SAVING) {
                    locationMode = "Battery saving. Uses Wi-Fi and mobile networks to determine location";
                }*/
        } else {
            return false;
        }
    }

}