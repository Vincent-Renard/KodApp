package fr.univorleans.etu.renardez.kodapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import fr.univorleans.etu.renardez.kodapp.db.Frigo;
import fr.univorleans.etu.renardez.kodapp.entities.PositionUser;
import fr.univorleans.etu.renardez.kodapp.entities.daos.PositionUserDao;

public class MainActivity extends AppCompatActivity {

    private Frigo base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        base = Frigo.getInstance(getApplicationContext());
    }


    public void clickClear(View view) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                PositionUserDao p = base.positionUserDao();
                for (PositionUser pu : p.getAllPU()) {
                    p.deleteItem(pu.getId());
                }
                Log.i("CLEAR_Main", "Cleared");
            }
        });
    }


    public void goToAddPos(View view) {
        startActivity(new Intent(this, AddLocActivity.class));
    }

    public void goToListPos(View view) {
        //TODO GOTO LIST POS
        startActivity(new Intent(this, PositionListActivity.class));

    }
}
