package fr.univorleans.etu.renardez.kodapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import fr.univorleans.etu.renardez.kodapp.entities.PositionUser;
import fr.univorleans.etu.renardez.kodapp.entities.daos.PositionUserDao;

@Database(entities = {PositionUser.class/*, autreTable.class*/}, version = 1, exportSchema = false)
public abstract class Frigo extends RoomDatabase {

    private static final String DB_NAME = "frigo";
    private static volatile Frigo THAT_BASE;

    public static Frigo getInstance(Context c) {
        if (THAT_BASE == null) {
            synchronized (Frigo.class) {
                if (THAT_BASE == null)
                    THAT_BASE = Room.databaseBuilder(c.getApplicationContext(), Frigo.class, DB_NAME + ".sqlite3").build();
            }
        }
        return THAT_BASE;
    }

    public abstract PositionUserDao positionUserDao();
}
