package fr.univorleans.etu.renardez.kodapp;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import fr.univorleans.etu.renardez.kodapp.entities.PositionUser;

public class PositionDisplayFragment extends Fragment {
    public static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/reverse?lat=%f&lon=%f&format=json";

    private Context context;

    private TextView labelPos;
    private TextView datePos;
    private TextView detailsPos;
    private TextView addressPos;
    private TextView coordsPos;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_position_display, container, false);
        labelPos = view.findViewById(R.id.position_display_label);
        datePos = view.findViewById(R.id.position_display_date);
        detailsPos = view.findViewById(R.id.position_display_details);
        addressPos = view.findViewById(R.id.position_display_address);
        coordsPos = view.findViewById(R.id.position_display_coords);
        base = Frigo.getInstance(getActivity().getApplicationContext());
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void updateText(PositionUser position) {
        labelPos.setText(position.getLabel());
        detailsPos.setText(position.getDetails());
        datePos.setText(
            DateFormat.format(
                DateFormat.getBestDateTimePattern(Locale.getDefault(), "ddMMyyyy HHmmss"),
                position.getDate()
            )
        );
        String coords = position.getLongitude() + "\n" + position.getLatitude() + "\n" + position.getAltitude() + "m";
        coordsPos.setText(coords);

        addressPos.setText(R.string.fetching_address);
        String url = String.format(Locale.ENGLISH, NOMINATIM_URL, position.getLatitude(), position.getLongitude());
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    addressPos.setText(response.getString("display_name"));
                } catch (JSONException e) {
                    addressPos.setText(R.string.address_request_error);
                    Log.e("GET-ADDR", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                addressPos.setText(R.string.address_request_error);
                Log.e("GET-ADDR", error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
    }


    public void delete(final PositionUser position) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                base.positionUserDao().deleteItem(position.getId());


            }
        });

    }
}
