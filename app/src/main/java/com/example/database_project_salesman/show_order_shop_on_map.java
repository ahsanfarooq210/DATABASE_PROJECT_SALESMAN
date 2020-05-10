package com.example.database_project_salesman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class show_order_shop_on_map extends AppCompatActivity implements OnMapReadyCallback
{

    //making a latlang object to store a location
    private LatLng latLng;
    //to initialize google map fragments
    private GoogleMap mMap;
    private Geocoder geo;
    private List<Address> address;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order_shop_on_map);

        //getting the coordinates from the previous activity
        Intent intent=getIntent();
        double latitiude=intent.getDoubleExtra("latitude",0.00);
        double longitude=intent.getDoubleExtra("longitude",0.00);
        latLng=new LatLng(latitiude,longitude);
        //initializing the maps fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.show_order_shop_on_map_mapfragment);
        mapFragment.getMapAsync( this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.clear();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        geo = new Geocoder(show_order_shop_on_map.this, Locale.getDefault());

        try
        {
            address= geo.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        mMap.addMarker(new MarkerOptions().position(latLng).title(address.toString()));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        if (mMap != null) {




            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Toast.makeText(show_order_shop_on_map.this, marker.getTitle().toString() + " Lat:" + marker.getPosition().latitude + " Long:" + marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();


    }
}
