package fr.univorleans.etu.renardez.kodapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fr.univorleans.etu.renardez.kodapp.db.Frigo;
import fr.univorleans.etu.renardez.kodapp.entities.PositionUser;
import fr.univorleans.etu.renardez.kodapp.entities.daos.PositionUserDao;

public class MenuActivity extends AppCompatActivity {

    private Frigo base;

    //TODO quand ca marche mettre un popup 'ok'
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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

            }
        });
        Toast.makeText(getApplicationContext(), R.string.db_now_empty, Toast.LENGTH_SHORT).show();
        Log.i("CLEAR_Main", "Cleared");
    }


    public void goToAddPos(View view) {
        startActivity(new Intent(this, AddLocActivity.class));
    }

    public void goToListPos(View view) {
        //TODO GOTO LIST POS
        startActivity(new Intent(this, PositionListActivity.class));

    }
}
