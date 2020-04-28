package com.example.database_project_salesman.BrodcastReceivers;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

public class LocationBrodcast extends BroadcastReceiver
{
    public boolean status;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        ContentResolver contentResolver=context.getContentResolver();
        int mode= Settings.Secure.getInt(contentResolver,Settings.Secure.LOCATION_MODE,Settings.Secure.LOCATION_MODE_OFF);
        if(mode==Settings.Secure.LOCATION_MODE_OFF)
        {
            status=false;
        }
        else
        {
            status=true;
        }
    }
}
