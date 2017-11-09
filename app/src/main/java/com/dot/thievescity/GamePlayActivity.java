package com.dot.thievescity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.Polygon;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GamePlayActivity extends AppCompatActivity implements OnMapReadyCallback {

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
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    List<Polygon> permittedPolygons, restrictedPolygons;
    boolean isRedOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
       // Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);
        //myToolbar.setTitleTextColor(Color.WHITE);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        username  = ParseUser.getCurrentUser().getUsername();
        sharedPreferences = GamePlayActivity.this.getSharedPreferences("com.dot.thievescity",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        loadMyGems();
        initializeRandomLocations();
        if(sharedPreferences.getInt("activityOrder",0)==1)
        {
            isRedOn = true;
            return;
        }
        //startGemPlacementProcess();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                gpsTracker =new GpsTracker(this, mMap, GamePlayActivity.this);
                startLocationService();
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
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        if(sharedPreferences.getInt("activityOrder",0)==1)
        {
            restartFromLocation();
        }
        editor.putInt("activityOrder", 1);

       // if(Build.VERSION.SDK_INT < 23)
        //gpsTracker =new GpsTracker(this, mMap, GamePlayActivity.this);
        checkPermissionAndStart();
        resizedMarker();

    }


    public void restartGPS()
    {
        gpsTracker.stopUsingGPS();
        gpsTracker = null;
        //if(Build.VERSION.SDK_INT < 23)
          //  gpsTracker =new GpsTracker(this, mMap, GamePlayActivity.this);
        checkPermissionAndStart();
    }

     public void checkPermissionAndStart()
    {
        //gpsTracker =new GpsTracker(this, mMap, GamePlayActivity.this);
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else{
            gpsTracker =new GpsTracker(this, mMap, GamePlayActivity.this);
            startLocationService();
        }
    }

    void startLocationService()
    {
       // if(gpsTracker.location == null)
         //   gpsTracker.getLocation();
        Location lastKnownLocation;
            //LatLng yourLoc = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            //mMap.addMarker(new MarkerOptions().position(yourLoc).title("Marker in User Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(13.023727,76.102132),18));
            // our col loc here
            PolygonClass polygonClass = new PolygonClass(mMap);
            polygonClass.drawPolygons();
            permittedPolygons = polygonClass.permittedPolygons;
            restrictedPolygons = polygonClass.restrictedPolygons;
            //Location loc1 = gpsTracker.getLocation();
            //Location loc2 = gpsTracker.getLocation();
            if(!isRedOn)
                startGemPlacementProcess();


            //float distanceInMeters = loc1.distanceTo(loc2);

            //Toast.makeText(this,distanceInMeters+"",Toast.LENGTH_LONG).show();


    }

    void restartFromLocation()
    {
        String s = sharedPreferences.getString("locationCurrent", "");
        gemIndex = sharedPreferences.getInt("pepeJeans", 0);
        if(s.equals(""))
        {
            createToast("Restarting app");
            finish();
        }
        Gson gson = new Gson();
        LatLng location = gson.fromJson(s,LatLng.class);
       //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.i("Latln",location.longitude+"");
        Marker marker = mMap.addMarker(new MarkerOptions().position(location).title("Marker in User Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        createToast("Go the location with red marker");
        createToast("to continue the game");
        handlerTracker(location, marker);
    }

    void handlerTracker(final LatLng latLng, final Marker marker)
    {
        final Handler handler =  new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               Location location = new Location("thisProvider");
                location.setLatitude(latLng.latitude);
                location.setLongitude(latLng.longitude);
                Location location2 = gpsTracker.location;
                if(location2.distanceTo(location) < 15.0f)
                {
                    startGemPlacementProcess();
                    marker.remove();
                    isRedOn = false;
                    handler.removeCallbacksAndMessages(null);
                }
                else
                    handler.postDelayed(this, 3000);

            }
        }, 4000);
    }
    ImageView imageView;

    void setViewImage(int type)
    {
        imageView = (ImageView) findViewById(R.id.image_view_gem);
        imageView.setImageResource(getGemImageId(type));
    }

    int getGemImageId (int type)
    {
        switch (type)
        {
            case 0: return R.drawable.gem_diamondd;
            case 1: return R.drawable.gem_ruby;
            case 2: return R.drawable.gem_em;
        }
        return 0;
    }

    public boolean isLocationInPolygon(Polygon polygon, Location location)
    {
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        return PolyUtil.containsLocation(latLng,polygon.getPoints(), false);

    }
    @Override
    public void onBackPressed() {
        // Simply Do nothing!
    }
boolean first;
    void startGemPlacementProcess()
    {
      //  startLocationService = gpsTracker.location;
        final TextView timerText = (TextView)findViewById(R.id.timer_text);
        int timeForEachGem = 60*1000;
        first = true;
       // Gem myGem = myGems.get(gemIndex);
            myTimer = new CountDownTimer(timeForEachGem, 1000) {
                public void onTick(long millisUntilFinished) {
                    if(first)
                    {
                        Gson gson = new Gson();
                        LatLng latLng = new LatLng(gpsTracker.location.getLatitude(),gpsTracker.location.getLongitude());
                        String s = gson.toJson(latLng);
                        editor.putString("locationCurrent", s);
                        editor.apply();
                        first = false;
                        setViewImage(myGems.get(gemIndex).type);
                    }
                    timerText.setText("00:"+millisUntilFinished / 1000 + "");
                    Log.i("Time:", millisUntilFinished / 1000 + "");
                }

                public void onFinish() {
                    Toast.makeText(getApplicationContext(), "Automatically placing at current location", Toast.LENGTH_LONG).show();
                    first = true;
                    if(!isPlaceable(gpsTracker.location))
                    {
                        createToast("Cannot place here!! Placing at random location!");
                        placeAtRandomLocation();
                        return;
                    }
                    placeGem();
                }
            }.start();



        //Log.i("DoneTimer", "Done");

    }
    boolean isRandom = false;
    int x;
    List<LatLng> randomLocations = new ArrayList<>();
    void placeAtRandomLocation()
    {
        int size = randomLocations.size();
        Random random = new Random();
        x = random.nextInt(size);
        isRandom = true;
        myTimer.cancel();
        placeGem();


    }

    void initializeRandomLocations()
    {
        randomLocations.add(new LatLng(13.024176, 76.101687));
        randomLocations.add(new LatLng(13.023696, 76.101655));
        randomLocations.add(new LatLng(13.023864, 76.101669));
        randomLocations.add(new LatLng(13.023709, 76.101688));
        randomLocations.add(new LatLng(13.023923, 76.101855));
        randomLocations.add(new LatLng(13.024112, 76.101845));
        randomLocations.add(new LatLng(13.023604, 76.102467));
        randomLocations.add(new LatLng(13.023586, 76.102505));
        randomLocations.add(new LatLng(13.024632, 76.102221));
        randomLocations.add(new LatLng(13.024821, 76.102215));
        randomLocations.add(new LatLng(13.024979, 76.102220));
        randomLocations.add(new LatLng(13.024482, 76.102704));
        randomLocations.add(new LatLng(13.024619, 76.102777));
        randomLocations.add(new LatLng(13.023548, 76.104195));
        randomLocations.add(new LatLng(13.023777, 76.104390));

    }
    boolean buttonPressed =false;
    public void onGemPlace(View view)
    {
        if(buttonPressed)
            return;
        buttonPressed = true;
        if(isRedOn)
        {
            createToast("Go to red marker location!");
            buttonPressed = false;
            return;
        }
        if(gpsTracker.location == null || !isPlaceable(gpsTracker.location))
        {
            createToast("Cannot place here!!");
            buttonPressed = false;
            return;
        }
        first = true;
        myTimer.cancel();
        placeGem();

    }


    void createToast(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    void placeGem(){
        //When user presses place button
        Gem gem = myGems.get(gemIndex);
        final String id = gem.id;
        final int type = gem.type;
        //GpsTracker gpsTracker = new GpsTracker(this);
        ParseObject parseObject = new ParseObject("Gem");
        parseObject.put("username", username);

        Location location = gpsTracker.location;
        if(location == null) {
            Toast.makeText(getApplicationContext(), "Place failed! Try again", Toast.LENGTH_SHORT).show();
            //gpsTracker.getLocation();
            buttonPressed = false;
            checkPermissionAndStart();
            return;
        }
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        //Location currentLocation = gpsTracker.location;
        ParseGeoPoint point = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
        if(isRandom){
            latLng = randomLocations.get(x);
            point = new ParseGeoPoint(latLng.latitude, latLng.longitude);
            isRandom = false;
            randomLocations.remove(x);
        }
        final LatLng l2 = new LatLng(latLng.latitude, latLng.longitude);
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
                    Marker marker =  mMap.addMarker(new MarkerOptions().position(l2).icon(BitmapDescriptorFactory.fromBitmap(forMarker(type))));
                    markers.put(id, marker);
                    gemIndex++;
                    editor.putInt("pepeJeans", gemIndex);
                    editor.apply();
                    if(gemIndex<15)
                        startGemPlacementProcess();
                    else
                        loadSecondGamePlayActivity();
                }

                buttonPressed = false;
            }
        });

        //TODO: Save gemIndex locally
    }

    Bitmap forMarker(int type)
    {
        switch (type)
        {
            case 0: return diamond;
            case 1: return ruby;
            case 2: return emerald;
        }
        return emerald;
    }
    Bitmap diamond,ruby,emerald;
    void resizedMarker()
    {
        int height = 80;
        int width = 80;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.gem_diamondd);
        Bitmap b=bitmapdraw.getBitmap();
        diamond = Bitmap.createScaledBitmap(b, width, height, false);
        BitmapDrawable bitmapdraw2=(BitmapDrawable)getResources().getDrawable(R.drawable.gem_ruby);
        Bitmap b2=bitmapdraw2.getBitmap();
        ruby = Bitmap.createScaledBitmap(b2, width, height, false);
        BitmapDrawable bitmapdraw3=(BitmapDrawable)getResources().getDrawable(R.drawable.gem_em);
        Bitmap b3=bitmapdraw3.getBitmap();
        emerald = Bitmap.createScaledBitmap(b3, width, height, false);

    }

    boolean isPlaceable(Location location)
    {
        boolean isInPermittedRegion = false, isInRestrictedRegion = false;
        for(Polygon polygon : permittedPolygons)
        {
            if(isLocationInPolygon(polygon,location))
            {
                isInPermittedRegion = true;
                break;
            }
        }
        for(Polygon polygon : restrictedPolygons)
        {
            if(isLocationInPolygon(polygon,location))
            {
                isInRestrictedRegion = true;
                break;
            }
        }

        return isInPermittedRegion && !isInRestrictedRegion;
    }


    void loadSecondGamePlayActivity()
    {
        //gpsTracker.stopUsingGPS();
        //gpsTracker=null;
        Intent secondGamePlayActivity = new Intent(getApplicationContext(),SecondGamePlayActivity.class);
        startActivity(secondGamePlayActivity);
        finish();
    }

    void loadMyGems(){
 //  assignnment of gems and values of them(3 types)
        for(int type = 2; type >= 0; type--){
            for(int i=0;i<5;i++)
                myGems.add(new Gem(type,username));
        }
    }


}

