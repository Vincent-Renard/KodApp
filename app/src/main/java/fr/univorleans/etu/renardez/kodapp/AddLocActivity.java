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
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import fr.univorleans.etu.renardez.kodapp.db.Frigo;
import fr.univorleans.etu.renardez.kodapp.entities.PositionUser;

public class AddLocActivity extends AppCompatActivity implements LocationListener, AdapterView.OnItemSelectedListener {
    public static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static final int ACTION_LOCATION_SOURCE_SETTINGS_RESULT = 2;

    private LocationManager locationManager;
    private Location currentLocation;
    private Frigo base;

    private Spinner spinner;
    private EditText otherLabelEditText;
    private EditText detailsEditText; //Can be optional

    private MapView map;

    private String[] labelsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_add_loc);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        base = Frigo.getInstance(getApplicationContext());
        spinner = findViewById(R.id.detail_loc_spinner);
        otherLabelEditText = findViewById(R.id.other_edit_text);
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        labelsList = getResources().getStringArray(R.array.labels_list);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.labels_list, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        map.setMultiTouchControls(1 == 1);
        map.setVerticalMapRepetitionEnabled(false);
        map.setHorizontalMapRepetitionEnabled(false);
        map.getController().setZoom(2.5);
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
        map.onPause();
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

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30, 0, this);
        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (currentLocation != null) {
            Log.i("BUTTON_POS", " Lat " + currentLocation.getLatitude() + " Long " + currentLocation.getLongitude() + " Alt " + currentLocation.getAltitude());
        }
    }

    public void clickPos(View view) {
        getLocation();
        //TODO si le mec entre une chaine vide ("est con" - Romain Guidez)
        if (currentLocation != null) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    String details = (
                            (spinner.getSelectedItemPosition() != labelsList.length - 1)
                            ? spinner.getSelectedItem()
                                    : otherLabelEditText.getText()
                    ).toString();
                    long id = base.positionUserDao().insertPos(new PositionUser(currentLocation, details));
                    System.out.println("dernier insert >" + id + " " + base.positionUserDao().getAllPU().get(base.positionUserDao().getAllPU().size() - 1).toString());
                    System.out.println(base.positionUserDao().getSomePu(id).get(0));
                }
            });

            otherLabelEditText.getText().clear();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        otherLabelEditText.setVisibility(
                (position == labelsList.length - 1)
                ? View.VISIBLE
                : View.GONE
        );
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        IMapController controller = map.getController();
        controller.setZoom(18.0);
        GeoPoint startPoint = new GeoPoint(currentLocation);
        controller.setCenter(startPoint);
        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().clear();
        map.getOverlays().add(startMarker);
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
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), ACTION_LOCATION_SOURCE_SETTINGS_RESULT);
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            getLocation();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ACTION_LOCATION_SOURCE_SETTINGS_RESULT) {
            getLocation();
        }
    }
}
