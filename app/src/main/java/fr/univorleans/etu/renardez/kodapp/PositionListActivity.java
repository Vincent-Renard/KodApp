package fr.univorleans.etu.renardez.kodapp;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import fr.univorleans.etu.renardez.kodapp.db.Frigo;
import fr.univorleans.etu.renardez.kodapp.entities.PositionUser;
import fr.univorleans.etu.renardez.kodapp.entities.daos.PositionUserDao;

public class PositionListActivity extends AppCompatActivity {
    private Frigo base;

    private RecyclerView positionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position_list);
        base = Frigo.getInstance(getApplicationContext());
        positionList = findViewById(R.id.position_list);
    }

    public void populatePositionList() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                PositionUserDao dao = base.positionUserDao();
                for (PositionUser pu : dao.getAllPU()) {
                    positionList.
                }
            }
        });
    }
}
