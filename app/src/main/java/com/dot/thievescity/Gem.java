package com.dot.thievescity;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Sanath on 10/6/2017.
 */
//@ParseClassName("Gem")
public class Gem  {
    public int type = 0;
    public String id;
    public boolean isPlaced = false;
    public Location location= new Location("dummyProvider");
    public String username;
    public Marker marker;

    public Gem(int type, String username){
        this.type = type;
        this.username = username;
        this.isPlaced = false;
        this.id = "00";
        marker = null;
    }


    public Gem(String id, int type, String username, Location location, boolean isPlaced)
    {
        this.id = id;
        this.type = type;
        this.username = username;
        this.isPlaced = isPlaced;
        this.location = location;
        marker = null;
    }

}
