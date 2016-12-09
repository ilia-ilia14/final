package com.example.tiger.mob_tracker;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;


public class SMS extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    private String myGPSLocation;
    private LocationManager myLocationManager;
    //SharedPreferences settings;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        buildGoogleApiClient();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else
            Toast.makeText(this, "Not connected...", Toast.LENGTH_SHORT).show();
    }


    public void onConnectionFailed(ConnectionResult arg0) {
        Toast.makeText(this, "Failed to connect...", Toast.LENGTH_SHORT).show();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onConnected(Bundle arg0) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            String loc = ("I am lost Latitude: "+ String.valueOf(mLastLocation.getLatitude())+" - Longitude: "+
                    String.valueOf(mLastLocation.getLongitude()));
            message(loc);
        }

    }


    @Override
    public void onConnectionSuspended(int arg0) {
        Toast.makeText(this, "Connection suspended...", Toast.LENGTH_SHORT).show();

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

/*
    /////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        message();
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //makeUseOfNewLocation(location);
                myGPSLocation = location.toString();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        myLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        this.finish();


    }

    private String getGPSLocation() {
        String coords;
        if (myGPSLocation == null) {
            Location locationGPS = myLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            coords = locationGPS.toString();
            return coords;
        }
        else {
            return myGPSLocation;
        }
    }


    */


    boolean issent = false;

            //"Latitude: "+ String.valueOf(mLastLocation.getLatitude())+" - Longitude: "+ String.valueOf(mLastLocation.getLongitude());
           // "2085578209";
    SmsManager smsManager = SmsManager.getDefault();
    public String message(String text)
    {

        //PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, SMS.class), 0);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String num = settings.getString("phone_number", "");
        if(num != "") {
            smsManager.sendTextMessage(num, null, text, null, null);
            issent = true;
            this.finish();
        } else { Toast.makeText(this, "Phone is not SET ", Toast.LENGTH_SHORT).show();   }
        this.finish();
        return text;
        }

    /*
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    */
//////////////////////////////////////////////

}
