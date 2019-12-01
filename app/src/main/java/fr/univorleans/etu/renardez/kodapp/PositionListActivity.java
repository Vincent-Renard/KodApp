package fr.univorleans.etu.renardez.kodapp;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import fr.univorleans.etu.renardez.kodapp.db.Frigo;
import fr.univorleans.etu.renardez.kodapp.entities.PositionUser;

public class PositionListActivity extends AppCompatActivity implements PositionListFragment.OnPositionListItemClickListener, PositionDisplayFragment.OnPositionDisplayItemDeletedListener {
    private Frigo base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.position_list_title);
        setContentView(R.layout.activity_position_list);
        base = Frigo.getInstance(getApplicationContext());
    }

    @Override
    public void onItemClick(int position, PositionUser positionUser) {
        ((PositionDisplayFragment) getSupportFragmentManager().findFragmentById(R.id.position_display_fragment)).updateText(position, positionUser);
    }

    @Override
    public void onItemDeleted(int position, final PositionUser positionUser) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                base.positionUserDao().deleteItem(positionUser.getId());
            }
        });
        ((PositionListFragment) getSupportFragmentManager().findFragmentById(R.id.position_list_fragment)).deletePosition(position);
    }
}
