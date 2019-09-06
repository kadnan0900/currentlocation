package com.example.currentlocation.Activities;
import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.currentlocation.extras.Constants;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static android.content.ContentValues.TAG;

public class GeoCoderService extends IntentService
{

    private static final int SUCCESS_RESULT = 0;
    private static final int FAILURE_RESULT = 1;
    protected ResultReceiver mReceiver;


    public GeoCoderService() {
        super("name");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {

        String errorMessage = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());


        LatLng latLng = (LatLng) intent.getParcelableExtra(Constants.LOCATION_KEY);
        mReceiver = intent.getParcelableExtra(Constants.Receiver_key);


        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            errorMessage = "Service not available";
            Log.e(TAG, errorMessage, e);
            e.printStackTrace();
        }
        catch (IllegalArgumentException illegalArgumentException) {
            //catch invalid latitude and longitude
            errorMessage = "Invalid latitude and longitude used";
            Log.e(TAG, errorMessage + ". " + "Latitude = " + latLng.latitude + ", Longitude = " + latLng.longitude);
        }

        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "No address found";
                Log.e(TAG, errorMessage);
            }
            deliverResultsToReceiver(FAILURE_RESULT, errorMessage);
        }
        else
            {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            //Fetch the address lines using getAddressLine
            //join them and send them to the MainActivity

            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(TAG, "Address Found");
            deliverResultsToReceiver(SUCCESS_RESULT, TextUtils.join(System.getProperty("line.separator"), addressFragments));
        }



    }

    private void deliverResultsToReceiver(int result, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Result_key, message);
        mReceiver.send(result, bundle);
    }

}





