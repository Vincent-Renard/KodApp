package fr.univorleans.etu.renardez.kodapp.entities;

import android.location.Location;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
@Entity(tableName = "positions")
public class PositionUser {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "longitude")
    private double longitude;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "altitude")
    private double altitude;


    @ColumnInfo(name = "label")
    private String label;

    @ColumnInfo(name = "date")
    private long date;

    @ColumnInfo(name = "details")
    private String details;


    public PositionUser() {
    }

    //TODO suppr si no need
    public PositionUser(double longitude, double latitude, double altitude, Date date, String details) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.date = date.getTime();
        this.details = details;
    }


    public PositionUser(Location location, String label, String details) {
        this.id = 0;
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
        this.altitude = location.hasAltitude() ? location.getAltitude() : 0.0;
        this.date = System.currentTimeMillis();
        this.details = details;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "PositionUser{" +
                "{ " + id +
                "} , longitude=" + longitude +
                ", latitude=" + latitude +
                ", altitude=" + altitude +
                ", label='" + label + '\'' +
                ", date=" + date +
                ", details='" + details + '\'' +
                '}';
    }
}
