package com.dot.thievescity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import com.parse.FindCallback;
//mport com.parse.LiveQueryException;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
//import com.parse.ParseLiveQueryClient;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
//import com.parse.SubscriptionHandling;
import android.os.Handler;

import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import java.util.logging.Handler;


public class SecondGamePlayActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    GpsTracker gpsTracker;
    //public LatLng lastKnownLocation;
    String username = "myUserName";
    List<Gem> myGems = new ArrayList<Gem>(), allGems = new ArrayList<Gem>();
    int gemIndex=0;
   // CountDownTimer myTimer;
    Date date;
    boolean loaded = true;
    List<Gem> grabGems = new ArrayList<Gem>();
    List<Gem> nearGems = new ArrayList<Gem>();
    float minDistance = 10.0f;
    ListView listView;
    NotificationCompat.Builder notification;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_game_play);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        listView = (ListView)findViewById(R.id.grab_gem_list);
        username  = ParseUser.getCurrentUser().getUsername();
        sharedPreferences = this.getSharedPreferences("com.dot.thievescity", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean("secStarted", true);
        editor.apply();
        loadMyGems();
        loadAllGems();
        initializeListView();
        initializeBag();
        //allGems.get(allGems.indexOf("Sanath"));
        //startGemPlacementProcess();

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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Add a marker in Sydney and move the camera
       // LatLng silver = new LatLng(13.022775, 76.102263);
      //  mMap.addMarker(new MarkerOptions().position(silver).title("Marker in Silver Jubliee").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(silver,15));

        //LatLng home1 = new LatLng(13.023611, 76.102263);
        //mMap.addMarker(new MarkerOptions().position(home1).title("Marker in Main Gate").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home1,15));

        //LatLng stage = new LatLng(13.024427, 76.103561);
        //mMap.addMarker(new MarkerOptions().position(stage).title("Marker in Stage"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stage,15));
        gpsTracker =new GpsTracker(this, mMap, SecondGamePlayActivity.this);



        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);


        }
        else{
            Location lastKnownLocation =null;
            if(gpsTracker.canGetLocation())
            {
                lastKnownLocation = gpsTracker.getLocation();
                LatLng yourLoc = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                //mMap.addMarker(new MarkerOptions().position(yourLoc).title("Marker in User Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yourLoc,15));

               // Location loc1 = gpsTracker.getLocation();
                //Location loc2 = gpsTracker.getLocation();



                //float distanceInMeters = loc1.distanceTo(loc2);

                //Toast.makeText(this,distanceInMeters+"",Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(this,"Can't get location",Toast.LENGTH_SHORT).show();


        }



    }

    void startSubscription()
    {
       /* ParseLiveQueryClient parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Gem");

        SubscriptionHandling<ParseObject> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

       /* subscriptionHandling.handleEvents(new SubscriptionHandling.HandleEventsCallback<ParseObject>() {
            @Override
            public void onEvents(ParseQuery<ParseObject> query, SubscriptionHandling.Event event, ParseObject object) {
                // HANDLING all events
                Gem gem = parseObjectToGem(object);
                Gem requiredGem = findGem(gem.id);
                updateGem(requiredGem, gem);
                Log.i("Changes", requiredGem.id);
                Toast.makeText(getApplicationContext(),"Subscribe successful", Toast.LENGTH_LONG).show();

            }
        });

        subscriptionHandling.handleError(new SubscriptionHandling.HandleErrorCallback<ParseObject>() {
            @Override
            public void onError(ParseQuery<ParseObject> query, LiveQueryException exception) {
                Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG).show();
            }
        });
        */

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int time = 4000;
                if(loaded) {
                    loaded = false;
                    //Log.i("Here", "218");
                    ParseQuery query = new ParseQuery<ParseObject>("Gem");
                    query.orderByAscending("updatedAt");
                    query.whereGreaterThan("updatedAt", date);
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            for (ParseObject object : objects) {
                                date = object.getUpdatedAt();
                                Gem gem = parseObjectToGem(object);
                                //Toast.makeText(getApplicationContext(),"Username:"+gem.username ,Toast.LENGTH_SHORT).show();
                                Gem requiredGem = findGem(gem.id);
                                //boolean yours = requiredGem.username.equals(username);
                                if(requiredGem!=null && requiredGem.username.equals(username) && !gem.username.equals(username))
                                    notifyGemLost(gem);
                                if(requiredGem == null)
                                {
                                    requiredGem = new Gem(gem.type, gem.username);
                                    allGems.add(requiredGem);
                                }
                                updateGem(requiredGem, gem);
                                if(!requiredGem.username.equals(username) && requiredGem.marker != null)
                                {
                                    requiredGem.marker.remove();
                                    requiredGem.marker = null;
                                }
                                if(requiredGem.username.equals(username) && requiredGem.marker == null)
                                    addMarker(requiredGem);
                                Log.i("Here", "StartSubscription");

                            }

                            loaded = true;
                        }

                    });
                }
                else
                    time = 1000;

                handler.postDelayed(this, time);
            }
        }, 4000);

    }

    void notifyGemLost(Gem gem)
    {
        String type = gemType(gem.type);
        notification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Gem Lost")
                .setContentText("You lost a " + type + " gem")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification.build());
    }



    void updateGem(Gem requiredGem, Gem gem)
    {
        if(requiredGem == null)
            return;
        requiredGem.id = gem.id;
        requiredGem.isPlaced = gem.isPlaced;
        requiredGem.location = gem.location;
        requiredGem.username = gem.username;
        requiredGem.type = gem.type;
    }

    Gem findGem(String objectId)
    {
       for(Gem gem : allGems){
           if(gem.id!=null)
           if(gem.id.equals(objectId))
               return gem;

       }
       return null;
    }



   /* void startGemPlacementProcess()
    {
        final TextView timerText = (TextView)findViewById(R.id.timer_text);
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

    } */


    void placeGem(Gem gem){
        //When user presses place button
        final int type = gem.type;
        final String objectId = gem.id;
        //GpsTracker gpsTracker = new GpsTracker(this);
        ParseQuery query = new ParseQuery("Gem");
        query.whereEqualTo("objectId", gem.id);
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if(e!=null)
                {
                    Toast.makeText(getApplicationContext(),"Network Error!", Toast.LENGTH_SHORT).show();
                   // return;
                }
                else
                {
                    ParseObject parseObject = objects.get(0);
                    ParseGeoPoint point = new ParseGeoPoint(gpsTracker.location.getLatitude(), gpsTracker.location.getLongitude());
                    parseObject.put("location", point);
                    parseObject.put("isPlaced", true);
                    parseObject.put("type", type);
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e!= null)
                                Toast.makeText(getApplicationContext(),"Place failed",Toast.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(getApplicationContext(), "Place Successful!", Toast.LENGTH_SHORT).show();
                                Gem gem = findGem(objectId);
                                if(gem!=null)
                                gem.isPlaced = true;
                                addMarker(gem);
                                updateBagUI(gem,false);
                            }

                        }
                    });
                }

            }
        });


        //Location currentLocation = gpsTracker.location;

    }


    void loadSecondGamePlayActivity()
    {
        Intent secondGamePlayActivity = new Intent(getApplicationContext(),SecondGamePlayActivity.class);
        startActivity(secondGamePlayActivity);
    }
    // Display layout
    void loginDetails(){
        //Fetching user id and password
    }



    void loadMyGems(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Gem");
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e== null){
                    myGems.clear();
                    for(ParseObject object : objects){
                        Gem gem = parseObjectToGem(object);
                        LatLng latLng = new LatLng(gem.location.getLatitude(), gem.location.getLongitude());
                        //gem.marker = marker;
                        myGems.add(gem);
                    }
                }

                else
                    Toast.makeText(getApplicationContext(),"Network Error!", Toast.LENGTH_SHORT).show();
            }

        });

    }

    Gem parseObjectToGem(ParseObject object)
    {
        ParseGeoPoint parseGeoPoint = object.getParseGeoPoint("location");
        Location location = new Location("dummyProvider");
        location.setLatitude(parseGeoPoint.getLatitude());
        location.setLongitude(parseGeoPoint.getLongitude());
        Gem gem = new Gem(object.getObjectId(),object.getInt("type"), object.getString("username"), location, object.getBoolean("isPlaced"));
        return gem;
    }
//TODO: Call this periodically
    void loadAllGems()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Gem");
        //query.whereNotEqualTo("username", username);
        query.orderByAscending("updatedAt");
        //query.whereEqualTo("isPlaced", true);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e== null){
                    allGems = new ArrayList<Gem>();
                    for(ParseObject object : objects){
                        Gem gem = parseObjectToGem(object);
                        if(gem.username.equals(username))
                        addMarker(gem);
                        allGems.add(gem);
                        date = object.getUpdatedAt();
                    }
                    startSubscription();
                    findGemsAtCurrentLocation();
                }

                else
                    Toast.makeText(getApplicationContext(),"Data loading failed", Toast.LENGTH_SHORT).show();
            }

        });



    }

    void addMarker(Gem gem)
    {
        Location gemLocation = gem.location;
        LatLng latLng = new LatLng(gemLocation.getLatitude(), gemLocation.getLongitude());
        gem.marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
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

    public void loadGemsNearBy()
    {
        float distanceWithin = 50.0f;
        nearGems.clear();
        //
        Location location = gpsTracker.location;
        for(Gem gem : allGems)
        {
            if(location.distanceTo(gem.location) <= distanceWithin)
                nearGems.add(gem);
        }

    }

    void findGemsAtCurrentLocation()
    {
        minDistance = 10.0f;

        final Location location  = gpsTracker.location;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
            // grabGems.clear();
             for(Gem gem : nearGems){
                if(location.distanceTo(gem.location) <= minDistance) {
                    if (findGrabGem(gem.id) == null && !gem.username.equals(username)) {
                        grabGems.add(gem);
                        updateGrabGemUI(gem, true);
                    }
                }
                else
                {
                    if(findGrabGem(gem.id)!=null) {
                        grabGems.remove(gem);
                        updateGrabGemUI(gem, false);
                    }
                }
             }

             handler.postDelayed(this,200);
            }
        }, 100);
    }

    Gem findGrabGem(String objectId)
    {
        for(Gem gem : grabGems){
            if(gem!=null && gem.id != null)
            if(gem.id.equals(objectId))
                return gem;

        }
        return null;
    }

    void verifyGrabGems()
    {
        minDistance = 10.0f;
        final Location location  = gpsTracker.location;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //grabGems.clear();
                for(Gem gem : grabGems){
                    if(location.distanceTo(gem.location) > minDistance || !gem.isPlaced)
                        grabGems.remove(gem);
                         updateGrabGemUI(gem, false);
                }
                handler.postDelayed(this,100);
            }
        }, 100);

    }


    List<String> grabGemObjectIds = new ArrayList<String>();

    void updateGrabGemUI(Gem gem , boolean add)
    {
        if(gem == null)
            return;
        String gemName = gemType(gem.type);

        if(add){
            adapter.add(gemName);
            grabGemObjectIds.add(gem.id);
        }
        else
        {
            try {
                int position = grabGemObjectIds.indexOf(gem.id);
                ids.remove(position);
                adapter.notifyDataSetChanged();
                grabGemObjectIds.remove(gem.id);
                Log.i("Here", "566");
            }
            catch (Exception e)
            {
                Log.i("MyException", e.toString());
            }


        }
    }

    ArrayList<String> ids;
    ArrayAdapter adapter;

    void initializeListView()
    {
        listView = (ListView)findViewById(R.id.grab_gem_list);
        ids = new ArrayList<String>();
        ids.add("first");
        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, ids);
        listView.setAdapter(adapter);
        adapter.remove("first");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

               // Toast.makeText(getApplicationContext(), "Clicked!", Toast.LENGTH_SHORT).show();
                onGrabGem(view,i);

            }
        });
    }
    ListView bagListView;
    List<String> bagIds;
    ArrayAdapter bagAdapter;

    void initializeBag()
    {
        bagListView = (ListView)findViewById(R.id.bag_list_view);
        bagIds = new ArrayList<String>();
        bagIds.add("first");
        bagAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, bagIds);
        bagListView.setAdapter(bagAdapter);
        bagAdapter.remove("first");
        bagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

               // Toast.makeText(getApplicationContext(), "Clicked!", Toast.LENGTH_SHORT).show();
                placeGem(bagGems.get(i));


            }
        });
    }

    List<Gem> bagGems = new ArrayList<>();
    List<String> bagGemObjectIds = new ArrayList<>();
    void updateBagUI(Gem gem , boolean add)
    {
        if(gem == null)
            return;
        String gemName = gemType(gem.type);

        if(add){
            bagAdapter.add(gemName);
            bagGemObjectIds.add(gem.id);
            bagGems.add(gem);
        }
        else
        {
            try {
                int position = bagGemObjectIds.indexOf(gem.id);
               // bagAdapter.remove(position);
                bagIds.remove(position);
                bagAdapter.notifyDataSetChanged();
                bagGemObjectIds.remove(gem.id);
                bagGems.remove(gem);
            }
            catch (Exception e)
            {
                Log.i("Not found", "Element not found");
            }

        }
    }

    String gemType(int type){
        if(type == 0)
            return "Gold";
        else if(type == 1)
            return "Silver";
        else
            return "Bronze";
    }

    int bagCapacity = 3;
    void onGrabGem(View view , int position){
        if(bagGems.size()>= bagCapacity){
            Toast.makeText(getApplicationContext(), "Bag Full!", Toast.LENGTH_SHORT).show();
            return;
        }
        String id = grabGemObjectIds.get(position);
        ParseQuery query = ParseQuery.getQuery("Gem");
        query.whereEqualTo("objectId", id);
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.get(0).getBoolean("isPlaced"))
                    grabGem(objects.get(0));
                else
                    Toast.makeText(getApplicationContext(), "Someone grabbed it already!", Toast.LENGTH_LONG).show();

            }
        });



       // loadAllGems();
    }


    void grabGem(ParseObject object)
    {
        final String objectId = object.getObjectId();
        object.put("isPlaced", false);
        object.put("location", new ParseGeoPoint(0,0));
        object.put("username", username);
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null) {
                    Toast.makeText(getApplicationContext(), "Successfully grabbed!", Toast.LENGTH_LONG).show();
                    Gem gem  = findGrabGem(objectId);
                    updateGrabGemUI(gem, false);
                    gem.isPlaced = false;
                    gem.username = username;
                    updateBagUI(gem, true);
                }
                    else
                    Toast.makeText(getApplicationContext(), "Network Error!", Toast.LENGTH_LONG).show();
            }
        });
    }

    void onClickBag()
    {

    }


}

