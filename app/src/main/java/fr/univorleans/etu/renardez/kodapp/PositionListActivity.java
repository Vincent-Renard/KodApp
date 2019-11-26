package fr.univorleans.etu.renardez.kodapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import fr.univorleans.etu.renardez.kodapp.entities.PositionUser;

public class PositionListActivity extends AppCompatActivity implements PositionListFragment.OnPositionListItemClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.position_list_title);
        setContentView(R.layout.activity_position_list);
    }

    @Override
    public void onItemClick(PositionUser position) {
        ((PositionDisplayFragment) getSupportFragmentManager().findFragmentById(R.id.position_display_fragment)).updateText(position);
    }
}
