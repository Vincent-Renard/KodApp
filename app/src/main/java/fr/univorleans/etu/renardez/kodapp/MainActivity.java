package fr.univorleans.etu.renardez.kodapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import fr.univorleans.etu.renardez.kodapp.db.Frigo;
import fr.univorleans.etu.renardez.kodapp.entities.PositionUser;

public class MainActivity extends AppCompatActivity implements LocationListener {
    public static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1;

    private LocationManager locationManager;
    private Location currentLocation;
    private Frigo base;
    //(MainActivity.this.getApplicationContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        base = Frigo.getInstance(getApplicationContext());
    }

    public void getLocation() {
        Log.i("BUTTON_POS", "getLocation");
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
            Log.i("BUTTON_POS", " Lat " + currentLocation.getLatitude() + " Long " + currentLocation.getLongitude() + " Alt " + currentLocation.getAltitude());
        }
    }

    public void clickPos(View view) {
        getLocation();
        if (currentLocation != null) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    long id = base.positionUserDao().insertPos(new PositionUser(currentLocation, "AUTO"));
                    System.out.println("dernier insert >" + id + " " + base.positionUserDao().getAllPU().get(base.positionUserDao().getAllPU().size() - 1).toString());
                    System.out.println(base.positionUserDao().getSomePu(id).get(0));
                }
            });


        }
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
