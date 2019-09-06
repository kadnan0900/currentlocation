package com.example.currentlocation.Activities;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.currentlocation.R;
import com.example.currentlocation.extras.Constants;
import com.example.currentlocation.interfaces.onLocationListner;
import com.example.currentlocation.utils.LocationHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback
{
    private GoogleMap mMap;
    private MapView mapView;
    private TextView address;
    public AddressResultReceiver mResultReceiver;
    private String mAddressOutput;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    private static final int Request_code = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initViews(savedInstanceState);
        checkLocationPermission();
        configureCameraIdle();
    }

    private void initViews(Bundle savedInstanceState)

    {
        mapView = findViewById(R.id.mapview);
        address = (TextView) findViewById(R.id.dragg_result);
        mapView.onCreate(savedInstanceState);
       mapView.getMapAsync(this );
    }

    private void configureCameraIdle()

    {
                onCameraIdleListener = new GoogleMap.OnCameraIdleListener()
        {
            @Override
            public void onCameraIdle()
            {
                LatLng location = mMap.getCameraPosition().target;
                //launch intent service
                startIntentService(location);

            }
        };
    }

    private void startIntentService(LatLng mlatlng)
    {

        Intent intent = new Intent(this, GeoCoderService.class);
        mResultReceiver = new AddressResultReceiver (new Handler());
        intent.putExtra (Constants.Receiver_key, mResultReceiver);
        intent.putExtra(Constants.LOCATION_KEY,mlatlng);
        startService (intent);
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(final GoogleMap googleMap)
    {

        mMap=googleMap;
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        mMap.setOnCameraIdleListener(onCameraIdleListener);

       new LocationHelper(this, new onLocationListner() {
            @Override
            public void onLocationChange(Location location) {
                LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,13f));
            }
        });

        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlp.setMargins(0, 1500, 180, 0);

        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Request_code: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                break;
            }
        }
    }
    public boolean checkLocationPermission() {


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?

            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},

                    Request_code);

            return false;
        } else {
            return true;
        }
    }


    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    private class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler)
        {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData)
        {
            super.onReceiveResult(resultCode, resultData);

            //Display the address string
            //or error message sent from Intent service
            mAddressOutput = resultData.getString(Constants.Result_key);
            displayAddressOutput();

        }
    }

    private void displayAddressOutput()
    {
        address.setText(mAddressOutput);
    }


}
