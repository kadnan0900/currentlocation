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
import androidx.annotation.Nullable;

import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import static android.content.ContentValues.TAG;

public class Intentservice extends IntentService {

    private static final int SUCCESS_RESULT = 0;
    private static final int FAILURE_RESULT = 1;
    private static final String PACKAGE_NAME = "com.nbstutorials.reversegeocoder";
    private static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    private static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    private static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
    protected ResultReceiver mReceiver;

    public Intentservice() {
        super("name");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String errorMessage = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        //get the location passed to this service through extras


        double latitiude = (double) intent.getExtras().get(LOCATION_DATA_EXTRA);



        List<Address> addresses = null;

//        try {
//            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
//        } catch (IOException e) {
//            errorMessage = "Service not available";
//            Log.e(TAG, errorMessage, e);
//            e.printStackTrace();
//        } catch (IllegalArgumentException illegalArgumentException) {
//            //catch invalid latitude and longitude
//            errorMessage = "Invalid latitude and longitude used";
//            Log.e(TAG, errorMessage + ". " + "Latitude = " + location.latitude + ", Longitude = " + location.longitude);
//        }
//
//        if (addresses == null || addresses.size() == 0) {
//            if (errorMessage.isEmpty()) {
//                errorMessage = "No address found";
//                Log.e(TAG, errorMessage);
//            }
//            deliverResultsToReceiver(FAILURE_RESULT, errorMessage);
//        } else {
//            Address address = addresses.get(0);
//            ArrayList<String> addressFragments = new ArrayList<String>();
//
//            //Fetch the address lines using getAddressLine
//            //join them and send them to the MainActivity
//
//            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
//                addressFragments.add(address.getAddressLine(i));
//            }
//            Log.i(TAG, "Address Found");
//            deliverResultsToReceiver(SUCCESS_RESULT, TextUtils.join(System.getProperty("line.separator"), addressFragments));
//        }
//

    }

    private void deliverResultsToReceiver(int failureResult, String message) {

        Bundle bundle = new Bundle();
        bundle.putString(RESULT_DATA_KEY, message);
        mReceiver.send(failureResult, bundle);
    }

}




//    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
//    private GoogleMap mMap;
//    private TextView resutText;
//
//
//    public Intentservice(String tag) {
//        super("Myservice");
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//
//        Log.d("Tag","Service is start now");
//
//        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
//            @Override
//            public void onCameraIdle() {
//
//                LatLng latLng = mMap.getCameraPosition().target;
//                Geocoder geocoder = new Geocoder(Intentservice.this);
//
//                try {
//                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//                    if (addressList != null && addressList.size() > 0) {
//                        String locality = addressList.get(0).getAddressLine(0);
////                        String country = addressList.get(0).getCountryName();
//                        if (!locality.isEmpty() ) {
//                            resutText.setText(locality + "  ");
//                        }
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        };
//
//    }
//
//
//}
