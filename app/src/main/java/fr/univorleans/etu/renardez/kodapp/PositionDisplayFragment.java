package fr.univorleans.etu.renardez.kodapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fr.univorleans.etu.renardez.kodapp.entities.PositionUser;

public class PositionDisplayFragment extends Fragment {
    private TextView namePos;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_position_display, container, false);
        namePos = view.findViewById(R.id.position_display_text);
        return view;
    }

    public void updateText(PositionUser position) {
        namePos.setText(position.getDetails());
    }
}
