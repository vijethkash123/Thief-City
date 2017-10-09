package com.dot.thievescity;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Admin on 10/6/2017.
 */

public class Gem {
    public int type = 0;
    public boolean isPlaced;
    public LatLng location;
    public Gem(int type){
        this.type = type;
    }
}
