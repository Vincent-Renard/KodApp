package fr.univorleans.etu.renardez.kodapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import fr.univorleans.etu.renardez.kodapp.db.Frigo;
import fr.univorleans.etu.renardez.kodapp.entities.PositionUser;
import fr.univorleans.etu.renardez.kodapp.entities.daos.PositionUserDao;

public class MenuActivity extends AppCompatActivity {

    private Frigo base;

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
                if (p.getAllPU().isEmpty()) runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), R.string.db_already_empty, Toast.LENGTH_SHORT).show();
                    }
                });
                else {
                    for (PositionUser pu : p.getAllPU()) {
                        p.deleteItem(pu.getId());
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), R.string.db_now_empty, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }


    public void goToAddPos(View view) {
        startActivity(new Intent(this, AddLocActivity.class));
    }

    public void goToListPos(View view) {
        startActivity(new Intent(this, PositionListActivity.class));

    }
}
