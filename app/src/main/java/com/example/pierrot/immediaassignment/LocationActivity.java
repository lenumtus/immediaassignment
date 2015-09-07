package com.example.pierrot.immediaassignment;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.example.pierrot.immediaassignment.utils.LoadLocation;
import com.example.pierrot.immediaassignment.utils.LoadVenues;

public class LocationActivity extends ActionBarActivity implements
        LocationListener {
    private static final String TAG = VenueListActivity.class.getSimpleName();

    private static final int GOOGLE_API_ERROR   = 1000;
    private static final int THRESHOLD          = 10;

    private GoogleApiClient mGoogleApiClient;
    private GoogleApiHandler mGoogleApiHandler = new GoogleApiHandler();
    private Location mLastUpdateLocation = null;

    private LocationFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location2);
        mFragment = (LocationFragment)getFragmentManager().findFragmentById(R.id.venue_list);
        buildGoogleApiClient();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkGoogleApiAvailability();
    }



    private synchronized void buildGoogleApiClient() {
        Log.d(TAG, "buildGoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(mGoogleApiHandler)
                .addOnConnectionFailedListener(mGoogleApiHandler)
                .addApi(LocationServices.API)
                .build();
    }

    //so that users whose device don't support this app will not be left hanging
    public void checkGoogleApiAvailability() {
        int availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        switch (availability) {
            case ConnectionResult.SUCCESS:
                Log.d(TAG, "Google API is available");
                mGoogleApiClient.connect();
                break;
            //if we need to handle different ConnectionResults differently, do it here
            default:
                Log.d(TAG, "Google API is not available: " + availability);
                GoogleApiAvailability.getInstance().getErrorDialog(this, availability, GOOGLE_API_ERROR).show();
                break;
        }
    }

    //set the location update frequency to moderate (5 seconds) and at high accuracy because I
    //assume this app will mainly be used by pedestrians walking at a regular pace with the need
    //to locate a specific address
    public LocationRequest createLocationRequest() {
        Log.d(TAG, "createLocationRequest");
        LocationRequest request = new LocationRequest();
        request.setInterval(5000);
        request.setFastestInterval(1000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return request;
    }

    @Override
    public void onLocationChanged(Location newLocation) {
        String locationString
                = String.valueOf(newLocation.getLatitude()) + ","
                + String.valueOf(newLocation.getLongitude());

        Log.d(TAG, "location=" + locationString);

        if (newLocation != null && !this.isFinishing()) {
            //update distances locally on the device every time the location is updated
            mFragment.updateDistances(newLocation);

            //refresh list of coffee shops shown only if it is either the first time we get a location,
            //or if the user has moved more than THRESHOLD (default=10) metres since the last update
            if (mLastUpdateLocation == null ||
                    mLastUpdateLocation.distanceTo(newLocation) > THRESHOLD) {
                mLastUpdateLocation = newLocation;
                new LoadLocation(mFragment).execute(locationString);
            }
        }
    }

    private class GoogleApiHandler implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

        @Override
        public void onConnected(Bundle bundle) {
            Log.d(TAG, "onConnected");
            onLocationChanged(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient,
                    createLocationRequest(),
                    LocationActivity.this);
        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.d(TAG, "onConnectionSuspended(" + i + ")");
        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.d(TAG, "onConnectionFailed: " + connectionResult.toString());
            Toast.makeText(LocationActivity.this, "Unable to connect to Google for location services, sorry.", Toast.LENGTH_SHORT).show();
        }
    }
}
