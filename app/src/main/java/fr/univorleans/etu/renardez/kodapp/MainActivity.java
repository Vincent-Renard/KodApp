package fr.univorleans.etu.renardez.kodapp;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;
    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    void getLocation() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 5, this);

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void clickPos(View view) {
        //TODO
        getLocation();
        //Log.i("BUTTON_POS","Lat "+currentLocation.getLatitude()+"Long "+currentLocation.getLongitude()+" Alt "+currentLocation.getAltitude());

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        Log.i("BUTTON_POS", "Lat " + currentLocation.getLatitude() + "Long " + currentLocation.getLongitude() + " Alt " + currentLocation.getAltitude());

        //currentLocation=location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
