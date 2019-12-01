package fr.univorleans.etu.renardez.kodapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/reverse?lat=%f&lon=%f&format=json";

    private OnPositionDisplayItemDeletedListener listener;
    private Context context;

    private TextView labelPos;
    private TextView datePos;
    private TextView detailsPos;
    private TextView addressPos;
    private TextView coordsPos;

    private Button deleteButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_position_display, container, false);

        labelPos = view.findViewById(R.id.position_display_label);
        datePos = view.findViewById(R.id.position_display_date);
        detailsPos = view.findViewById(R.id.position_display_details);
        addressPos = view.findViewById(R.id.position_display_address);
        coordsPos = view.findViewById(R.id.position_display_coords);

        deleteButton = view.findViewById(R.id.del_pos_from_display_frag);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof OnPositionDisplayItemDeletedListener) {
            this.listener = (OnPositionDisplayItemDeletedListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                + " must implement OnPositionDisplayItemDeletedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void updateText(final int position, final PositionUser positionUser) {
        if (position == -1) {
            labelPos.setText("");
            detailsPos.setText("");
            datePos.setText("");
            coordsPos.setText("");
            addressPos.setText("");
            deleteButton.setVisibility(View.GONE);
            return;
        }

        labelPos.setText(getString(R.string.display_label, positionUser.getLabel()));
        detailsPos.setText(getString(R.string.display_details, positionUser.getDetails()));
        datePos.setText(getString(
            R.string.display_date,
            DateFormat.format(
                DateFormat.getBestDateTimePattern(Locale.getDefault(), "ddMMyyyy HHmmss"),
                positionUser.getDate()
            )
        ));
        coordsPos.setText(getString(R.string.display_coords, positionUser.getLongitude(), positionUser.getLatitude()));

        addressPos.setText(R.string.fetching_address);
        addressPos.setTextColor(Color.LTGRAY);
        String url = String.format(Locale.ENGLISH, NOMINATIM_URL, positionUser.getLatitude(), positionUser.getLongitude());
        RequestQueue queue = Volley.newRequestQueue(context);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    addressPos.setText(getString(R.string.display_address, response.getString("display_name")));
                    addressPos.setTextColor(Color.parseColor("#808080"));
                } catch (JSONException e) {
                    addressPos.setText(R.string.address_request_error);
                    addressPos.setTextColor(Color.RED);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                addressPos.setText(R.string.address_request_error);
                addressPos.setTextColor(Color.RED);
            }
        });
        queue.add(jsonObjectRequest);

        deleteButton.setVisibility(View.VISIBLE);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonObjectRequest.cancel();
                listener.onItemDeleted(position, positionUser);
            }
        });
    }

    public interface OnPositionDisplayItemDeletedListener {
        void onItemDeleted(int position, PositionUser positionUser);
    }
}
