package com.dot.thievescity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dot.thievescity.utils.GpsTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;

public class GamePlayActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    GpsTracker gpsTracker;
    public LatLng lastKnownLocation;
    String username = "myUserName";
    ArrayList<Gem> myGems = new ArrayList<Gem>();
    int gemIndex=0;
    CountDownTimer myTimer;
    public HashMap<String , Marker> markers = new HashMap<>();
    Location startLocation;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        username  = ParseUser.getCurrentUser().getUsername();
        sharedPreferences = GamePlayActivity.this.getSharedPreferences("com.dot.thievescity",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        loadMyGems();
        startGemPlacementProcess();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
          //  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Add a marker in Sydney and move the camera
       // LatLng silver = new LatLng(13.022775, 76.102263);
        //mMap.addMarker(new MarkerOptions().position(silver).title("Marker in Silver Jubliee").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(silver,15));

        //LatLng home1 = new LatLng(13.023611, 76.102263);
        //mMap.addMarker(new MarkerOptions().position(home1).title("Marker in Main Gate").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home1,15));

        //LatLng stage = new LatLng(13.024427, 76.103561);
        //mMap.addMarker(new MarkerOptions().position(stage).title("Marker in Stage"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stage,15));
        gpsTracker =new GpsTracker(this, mMap);


        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);


        }
        else{
            Location lastKnownLocation =null;
            if(gpsTracker.canGetLocation())
            {
                lastKnownLocation = gpsTracker.getLocation();
                LatLng yourLoc = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(yourLoc).title("Marker in User Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yourLoc,15));

                //Location loc1 = gpsTracker.getLocation();
                //Location loc2 = gpsTracker.getLocation();



                //float distanceInMeters = loc1.distanceTo(loc2);

                //Toast.makeText(this,distanceInMeters+"",Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(this,"Can't get location",Toast.LENGTH_SHORT).show();


        }



    }

    void startGemPlacementProcess()
    {
      //  startLocation = gpsTracker.location;
        final TextView timerText = findViewById(R.id.timer_text);
        int timeForEachGem = 60*1000;
       // Gem myGem = myGems.get(gemIndex);
            myTimer = new CountDownTimer(timeForEachGem, 1000) {
                public void onTick(long millisUntilFinished) {
                    timerText.setText(millisUntilFinished / 1000 + "");
                    Log.i("Time:", millisUntilFinished / 1000 + "");
                }

                public void onFinish() {
                    Toast.makeText(getApplicationContext(), "Automatically placing at current location", Toast.LENGTH_LONG).show();
                    placeGem();
                }
            }.start();



        //Log.i("DoneTimer", "Done");

    }

    public void onGemPlace(View view)
    {
        myTimer.cancel();
        placeGem();

    }

    void placeGem(){
        //When user presses place button
        Gem gem = myGems.get(gemIndex);
        final String id = gem.id;
        int type = gem.type;
        //GpsTracker gpsTracker = new GpsTracker(this);
        ParseObject parseObject = new ParseObject("Gem");
        parseObject.put("username", username);

        Location location = gpsTracker.location;
        final LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        //Location currentLocation = gpsTracker.location;
        ParseGeoPoint point = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
        parseObject.put("location", point);
        parseObject.put("type", type);
        parseObject.put("isPlaced", true);
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!= null)
                    Toast.makeText(getApplication(),"Save failed",Toast.LENGTH_LONG).show();
                else
                {
                    Toast.makeText(getApplication(),"Gem Placed successfully!",Toast.LENGTH_SHORT).show();
                    Marker marker =  mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    markers.put(id, marker);
                    gemIndex++;
                    editor.putInt("avusfq", gemIndex);
                    editor.apply();
                    if(gemIndex<15)
                        startGemPlacementProcess();
                    else
                        loadSecondGamePlayActivity();
                }
            }
        });

        //TODO: Save gemIndex locally
    }


    void loadSecondGamePlayActivity()
    {
        //gpsTracker = null;
        Intent secondGamePlayActivity = new Intent(getApplicationContext(),SecondGamePlayActivity.class);
        startActivity(secondGamePlayActivity);
        finish();
    }
    // Display layout
    void loginDetails(){
        //Fetching user id and password
    }

    void loadMyGems(){
 //  assignnment of gems and values of them(3 types)
        for(int type = 0; type < 3; type++){
            for(int i=0;i<5;i++)
                myGems.add(new Gem(type,username));
        }
    }

    void loadOtherGems()
    {

    }
    //using multithreading implement timer1 and display map
    void timer1(){
      /*timer set on for 15 mins*/
    }
    void unplacedGems(){
        //when timer1 is 0 and gemarray!=0,place the remaining gems in current location
    }
// server time starts(actual timer)
    //Play begins
    void initialiseGems(){
    }

    void displayMap (){

    }

    void updateGemDisplay(){
    //info about placed gems and displaying it on the map
    }

    void startLocationService() {

    }

    void getUserLocation() {

    }

    void findGemsNearBy()
    {

    }

    void onGemFound(){

    }


    void onClickBag()
    {

    }


}

