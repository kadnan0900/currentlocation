package com.example.currentlocation.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import com.example.currentlocation.interfaces.onLocationListner;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * last location provider
 */
public class LocationHelper {

    private Context mContext;
    Location mlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private onLocationListner listner;
    private static LocationHelper mInstance;

    public LocationHelper(Context mContext, onLocationListner mOnLocationListner) {
        this.mContext = mContext;
        this.listner = mOnLocationListner;
        setGoogleApiClient();
        getLocation();

    }


    /**
     * init api client
     */
    void setGoogleApiClient() {
        if (fusedLocationProviderClient == null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
        }

    }

    @SuppressLint("MissingPermission")
    private void getLocation() {

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mlocation = location;
                    listner.onLocationChange(location);

//                    Toast.makeText(mContext.getApplicationContext(), mlocation.getLatitude() + "" + mlocation.getLongitude(),
//                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
