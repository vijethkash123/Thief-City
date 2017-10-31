package com.dot.thievescity;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dot.thievescity.utils.GpsTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

public class PolygonDraw extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GpsTracker gpsTracker;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int i =0;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polygon_draw);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sharedPreferences = this.getSharedPreferences("com.dot.thievescity", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        textView = (TextView) findViewById(R.id.locTV);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        gpsTracker = new GpsTracker(this,mMap);
        // Add a marker in Sydney and move the camera
        LatLng mce = new LatLng(13.0173608,76.0923321);
        mMap.addMarker(new MarkerOptions().position(mce).title("Marker in Mce"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mce, 20));
        myList = new ArrayList<>();
    }

    ArrayList<LatLng> myList = new ArrayList<>();


    public void addPoint(View view)
    {
        textView.setText("TV");
        Location location = gpsTracker.location;
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        myList.add(latLng);
        Toast.makeText(getApplicationContext(),"Added Point", Toast.LENGTH_SHORT).show();
    }
Polygon polygon;
    public void finish(View view)
    {
       PolygonOptions polygonOptions = new PolygonOptions();
        for(LatLng latLng : myList)
        {
            polygonOptions.add(latLng);
        }
        polygon = mMap.addPolygon(polygonOptions);
    }

    public void save(View view)
    {
        editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String myListJSON = gson.toJson(myList);

            editor.putString("polygonsMce"+ i,myListJSON);
            editor.apply();
        String text = "";
        for(LatLng latLng: myList)
        {
            text = text + "Location: " + latLng.latitude + ", " + latLng.longitude + "\n";
        }
        textView.setText(text);
            i++;
            myList.clear();
    }

    public void discard(View view)
    {
        polygon.remove();
        mMap.clear();
    }
}
