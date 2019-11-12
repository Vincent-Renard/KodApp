package fr.univorleans.etu.renardez.kodapp.entities.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.univorleans.etu.renardez.kodapp.entities.PositionUser;

@Dao
public interface PositionUserDao {
    String TABLE_NAME = "positions";

    @Query("SELECT * FROM positions")
    List<PositionUser> getAllPU();

    @Query("SELECT * FROM positions WHERE id = :ids")
    List<PositionUser> getSomePu(long... ids);

    @Insert
    long insertPos(PositionUser positionUser);

    @Update
    int updatePos(PositionUser positionUser);

    @Query("DELETE FROM positions WHERE id = :posId")
    int deleteItem(int posId);

}
