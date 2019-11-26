package fr.univorleans.etu.renardez.kodapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fr.univorleans.etu.renardez.kodapp.db.Frigo;
import fr.univorleans.etu.renardez.kodapp.entities.PositionUser;

public class PositionDisplayFragment extends Fragment {
    private Frigo base;
    private TextView labelPos;
    private TextView datePos;
    private TextView detailsPos;
    private TextView dressPos;
    private TextView coordsPos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_position_display, container, false);
        labelPos = view.findViewById(R.id.position_display_label);
        detailsPos = view.findViewById(R.id.position_display_details);
        coordsPos = view.findViewById(R.id.position_display_coords);
        base = Frigo.getInstance(getActivity().getApplicationContext());
        return view;
    }

    public void updateText(PositionUser position) {
        labelPos.setText(position.getLabel());
        detailsPos.setText(position.getDetails());
        String coords = position.getLongitude() + "\n" + position.getLatitude() + "\n" + position.getAltitude() + "m";
        coordsPos.setText(coords);

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
