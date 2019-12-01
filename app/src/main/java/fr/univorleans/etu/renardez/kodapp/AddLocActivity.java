package fr.univorleans.etu.renardez.kodapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

    private BroadcastReceiver networkReceiver;

    private LocationManager locationManager;
    private Location currentLocation;
    private Frigo base;

    private Button saveButton;
    private Spinner spinner;
    private EditText otherLabelEditText;
    private EditText detailsEditText; //Can be optional
    private TextView networkTextView;

    private MapView map;

    private String[] labelsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_add_loc);

        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                networkTextView.setVisibility(
                        (networkInfo == null || !networkInfo.isConnected())
                        ? View.VISIBLE
                        : View.GONE
                );
            }
        };

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        base = Frigo.getInstance(getApplicationContext());
        saveButton = findViewById(R.id.save_position_button);
        spinner = findViewById(R.id.detail_loc_spinner);
        otherLabelEditText = findViewById(R.id.other_edit_text);
        detailsEditText = findViewById(R.id.details_edit_text);
        networkTextView = findViewById(R.id.network_text_view);
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

        otherLabelEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    detailsEditText.requestFocus();
                    return true;
                }
                return false;
            }
        });

        detailsEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    saveButton.callOnClick();
                    return true;
                }
                return false;
            }
        });

        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        map.setMultiTouchControls(true);
        map.setVerticalMapRepetitionEnabled(false);
        map.getController().setZoom(2.5);
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        unregisterReceiver(networkReceiver);
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

    public void clickSearchPosition(View view) {
        Toast.makeText(getApplicationContext(), R.string.searching_position, Toast.LENGTH_SHORT).show();
        getLocation();
    }

    public void clickSavePosition(View view) {
        if (spinner.getSelectedItemPosition() == labelsList.length - 1 && otherLabelEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.warn_other_empty, Toast.LENGTH_LONG).show();
            otherLabelEditText.requestFocus();
            return;
        }

        if (currentLocation == null) {
            Toast.makeText(getApplicationContext(), R.string.warn_no_position, Toast.LENGTH_LONG).show();
            return;
        }

        final AddLocActivity self = this;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String label = (
                        (spinner.getSelectedItemPosition() != labelsList.length - 1)
                                ? spinner.getSelectedItem()
                                : otherLabelEditText.getText()
                ).toString();
                long id = base.positionUserDao().insertPos(new PositionUser(currentLocation, label, detailsEditText.getText().toString()));
                Log.i("BD", "dernier insert >" + id + " " + base.positionUserDao().getAllPU().get(base.positionUserDao().getAllPU().size() - 1).toString());
                Log.i("BD", base.positionUserDao().getSomePu(id).get(0).toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(self, PositionListActivity.class));
                    }
                });
            }
        });
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
        saveButton.setEnabled(true);
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_LOCATION_SOURCE_SETTINGS_RESULT) {
            getLocation();
        }
    }
}
