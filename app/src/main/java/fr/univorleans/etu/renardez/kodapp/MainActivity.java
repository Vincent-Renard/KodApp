package fr.univorleans.etu.renardez.kodapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements LocationListener {
    public static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1;

    private LocationManager locationManager;
    private Location currentLocation;
    //private Frigo base = Frigo.getInstance(this.getApplicationContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    public void getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            requestLocationProvider();
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, this);
        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (currentLocation != null) {
            Log.i("BUTTON_POS", "Lat " + currentLocation.getLatitude() + "Long " + currentLocation.getLongitude() + " Alt " + currentLocation.getAltitude());
        }
    }

    public void clickPos(View view) {
        getLocation();
        //Log.i("BUTTON_POS", "Lat " + currentLocation.getLatitude() + "Long " + currentLocation.getLongitude() + " Alt " + currentLocation.getAltitude());
        //base.positionUserDao().insertPos(new PositionUser(currentLocation, "AUTO"));
        //System.out.println(base.positionUserDao().getAllPU().get(0).toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        requestLocationProvider();
    }

    public void requestLocationProvider() {
        new AlertDialog.Builder(this)
            .setMessage(R.string.enable_location)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            })
            .show();
    }
}
