package fr.univorleans.etu.renardez.kodapp;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import fr.univorleans.etu.renardez.kodapp.db.Frigo;
import fr.univorleans.etu.renardez.kodapp.entities.PositionUser;
import fr.univorleans.etu.renardez.kodapp.entities.daos.PositionUserDao;

public class PositionListActivity extends AppCompatActivity {
    private Frigo base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position_list);
        base = Frigo.getInstance(getApplicationContext());
    }

    public void populatePositionList() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                PositionUserDao dao = base.positionUserDao();
                for (PositionUser pu : dao.getAllPU()) {
                    //positionList.
                }
            }
        });
    }
}
