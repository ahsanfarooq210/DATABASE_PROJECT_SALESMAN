package com.example.database_project_salesman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class show_shop_on_map_activity extends AppCompatActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;
    private Geocoder geo;
    private List<Address> address;
    private DatabaseReference shopReference;
    private ArrayList<ShopDetails> shopArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_shop_on_map_activity);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.show_shop_map);
        mapFragment.getMapAsync( this);

        shopReference= FirebaseDatabase.getInstance().getReference("SHOP");
        shopReference.keepSynced(true);
        shopArrayList=new ArrayList<>();

    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.clear();
        geo = new Geocoder(show_shop_on_map_activity.this, Locale.getDefault());

        if (mMap != null) {




            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Toast.makeText(show_shop_on_map_activity.this, marker.getTitle().toString() + " Lat:" + marker.getPosition().latitude + " Long:" + marker.getPosition().longitude, Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        shopReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                shopArrayList.clear();
                for (DataSnapshot shop : dataSnapshot.getChildren())
                {
                    shopArrayList.add(shop.getValue(ShopDetails.class));
                }
                for(int i=0;i<shopArrayList.size();i++)
                {
                    LatLng coordinates=new LatLng(shopArrayList.get(i).getLatitude(),shopArrayList.get(i).getLongitude());
                    try
                    {

                        address= geo.getFromLocation(coordinates.latitude, coordinates.longitude, 1);
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    mMap.addMarker(new MarkerOptions().position(coordinates).title(address.toString()));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(show_shop_on_map_activity.this, "error in downloading the data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
