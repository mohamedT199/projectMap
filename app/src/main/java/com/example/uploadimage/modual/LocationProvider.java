package com.example.uploadimage.modual;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.core.content.ContextCompat;

import java.util.List;

@SuppressLint("MissingPermission")
public class LocationProvider {
    LocationManager manger ;
    Location location1 ;
    final long TIME_BETWEEN_UPDET = 300000 ;
    final long DISTANCE_BETWEEN_UPDET = 500 ;

    public LocationProvider(Context con ) {
        this.manger  = (LocationManager) con.getSystemService(Context.LOCATION_SERVICE);
        location1 = null ;
    }

    protected boolean IfHaveProviderOnline()
    {
        boolean gbs = manger.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean internet = manger.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return  gbs || internet ;
    }


    public   Location getLocationCurrently(LocationListener locLostener)
    {
        if (!IfHaveProviderOnline())
        {
            return null ;
        }
        String provider  =  LocationManager.GPS_PROVIDER ;
        if (!manger.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            provider = LocationManager.NETWORK_PROVIDER ;
        }
        location1 = manger.getLastKnownLocation(provider);
        if (location1 == null)
        {
            Location loc1 = location1;
            location1 = getBestLocation();
            if (location1==null)
            {
                location1 = loc1 ;
            }
        }
        if (locLostener!=null)
        {
            manger.requestLocationUpdates(provider , TIME_BETWEEN_UPDET , DISTANCE_BETWEEN_UPDET , locLostener);
        }
        return location1  ;




    }

    private Location getBestLocation() {
        Location temp = null ,  bestLoc = null  ;
        List<String> allProvider = manger.getAllProviders();
        for (String providerItem : allProvider)
        {
            temp = manger.getLastKnownLocation(providerItem);
            if (temp==null)
            {
                continue;
            }
            if (bestLoc==null)
            {
                bestLoc = temp ;
            }
            else
            {
                if (bestLoc.getAccuracy()  <  temp.getAccuracy())
                {
                    bestLoc = temp ;

                }
            }
        }

        return bestLoc ;
    }
}
