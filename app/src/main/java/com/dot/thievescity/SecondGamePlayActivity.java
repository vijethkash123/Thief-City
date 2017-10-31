package com.dot.thievescity;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dot.thievescity.utils.GpsTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.FindCallback;
//mport com.parse.LiveQueryException;
import com.parse.LiveQueryException;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
//import com.parse.ParseLiveQueryClient;
import com.parse.ParseLiveQueryClient;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SubscriptionHandling;
//import com.parse.SubscriptionHandling;
import android.os.Handler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import java.util.logging.Handler;


public class SecondGamePlayActivity extends FragmentActivity implements OnMapReadyCallback {

    public GoogleMap mMap;
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
    int notificationIndex = 0;


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
        editor.putInt("activityOrder", 2);
        editor.apply();
        loadMyGems();
        loadAllGems();
        initializeListView();
        initializeBag();
        //bagDataInitialize();
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
            gpsTracker.getLocation();
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
        gpsTracker =new GpsTracker(this, mMap, SecondGamePlayActivity.this);

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

        checkPermissionAndStart();


    }

    public void checkPermissionAndStart()
    {




        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);


        }
        else{
            Location lastKnownLocation =null;
            if(gpsTracker.location==null)
                gpsTracker.getLocation();
            if(gpsTracker.canGetLocation())
            {
               // gpsTracker.location = gpsTracker.getLocation();
                //lastKnownLocation = gpsTracker.location;
                //LatLng yourLoc = new LatLng();
                //mMap.addMarker(new MarkerOptions().position(yourLoc).title("Marker in User Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(13.0173608,76.0923321),15));

                // Location loc1 = gpsTracker.getLocation();
                //Location loc2 = gpsTracker.getLocation();



                //float distanceInMeters = loc1.distanceTo(loc2);

                //Toast.makeText(this,distanceInMeters+"",Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(this,"Can't get location",Toast.LENGTH_SHORT).show();


        }
    }

    List<Gem> liveObjects = new ArrayList<>();

    void startSubscription()
    {
        ParseLiveQueryClient parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Gem");

        SubscriptionHandling<ParseObject> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

        subscriptionHandling.handleEvents(new SubscriptionHandling.HandleEventsCallback<ParseObject>() {
            @Override
            public void onEvents(ParseQuery<ParseObject> query, SubscriptionHandling.Event event, ParseObject object) {
                // HANDLING all events
                Gem gem = parseObjectToGem(object);
                liveObjects.add(gem);
                //Toast.makeText(getApplicationContext(),"Username:"+gem.username ,Toast.LENGTH_SHORT).show();

            }
        });

        subscriptionHandling.handleError(new SubscriptionHandling.HandleErrorCallback<ParseObject>() {
            @Override
            public void onError(ParseQuery<ParseObject> query, LiveQueryException exception) {
                Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG).show();
            }
        });

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(liveObjects.size()==0)
                {
                    handler.postDelayed(this,300);
                    return;
                }
                for(Gem gem : liveObjects) {
                    //Gem gem = liveObjects.get(0);
                    Gem requiredGem = findGem(gem.id);
                    //if(requiredGem == null)
                    //createToast("243");
                    //boolean yours = requiredGem.username.equals(username);
                    if (requiredGem != null && requiredGem.username.equals(username) && !gem.username.equals(username))
                        notifyGemLost(gem);
                    if (requiredGem == null) {
                        requiredGem = new Gem(gem.type, gem.username);
                        allGems.add(requiredGem);
                    }
                    updateGem(requiredGem, gem);
                    if (!requiredGem.username.equals(username) && requiredGem.marker != null) {
                        requiredGem.marker.remove();
                        requiredGem.marker = null;
                    }
                    if (requiredGem.username.equals(username) && requiredGem.marker == null)
                        addMarker(requiredGem);
                    Log.i("Here", gem.username);
                    liveObjects.remove(gem);
                }
                handler.postDelayed(this,300);
            }
        },300);


      /*  final Handler handler = new Handler();
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
                            try {
                                for (ParseObject object : objects) {
                                    date = object.getUpdatedAt();
                                    Gem gem = parseObjectToGem(object);
                                    //Toast.makeText(getApplicationContext(),"Username:"+gem.username ,Toast.LENGTH_SHORT).show();
                                    Gem requiredGem = findGem(gem.id);
                                    if(requiredGem == null)
                                        createToast("243");
                                    //boolean yours = requiredGem.username.equals(username);
                                    if (requiredGem != null && requiredGem.username.equals(username) && !gem.username.equals(username))
                                        notifyGemLost(gem);
                                    if (requiredGem == null) {
                                        requiredGem = new Gem(gem.type, gem.username);
                                        allGems.add(requiredGem);
                                    }
                                    updateGem(requiredGem, gem);
                                    if (!requiredGem.username.equals(username) && requiredGem.marker != null) {
                                        requiredGem.marker.remove();
                                        requiredGem.marker = null;
                                    }
                                    if (requiredGem.username.equals(username) && requiredGem.marker == null)
                                        addMarker(requiredGem);
                                    Log.i("Here", "StartSubscription");

                                }

                                loaded = true;
                            }
                            catch (Exception ex) {
                               createToast("Handler:Done:Query:Background");
                            }
                        }




                    });
                }
                else
                    time = 1000;

                handler.postDelayed(this, time);
            }
        }, 4000);
        */

    }

    void notifyGemLost(Gem gem)
    {
        if(gem ==null) {
            createToast("Something Null gem notify");
            return;
        }
        String type = gemType(gem.type);
        notification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Gem Lost")
                .setContentText("You lost a " + type + " gem")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notificationIndex,notification.build());
        notificationIndex++;
    }



    void updateGem(Gem requiredGem, Gem gem)
    {
        if(requiredGem == null || gem ==null) {
            createToast("Something Null gem or required gem");
            return;
        }
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
       createToast("Returned Null from findGem 324");
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
        try {
            final int type = gem.type;
            final String objectId = gem.id;
            //GpsTracker gpsTracker = new GpsTracker(this);
            ParseQuery query = new ParseQuery("Gem");
            query.whereEqualTo("objectId", gem.id);
            query.setLimit(1);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e != null) {
                        Toast.makeText(getApplicationContext(), "Network Error!", Toast.LENGTH_SHORT).show();
                        // return;
                    } else {
                        ParseObject parseObject = objects.get(0);
                        if (gpsTracker.location == null) {
                            Toast.makeText(getApplicationContext(), "PLace failed! Try again", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ParseGeoPoint point = new ParseGeoPoint(gpsTracker.location.getLatitude(), gpsTracker.location.getLongitude());
                        parseObject.put("location", point);
                        parseObject.put("isPlaced", true);
                        parseObject.put("type", type);
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null)
                                    Toast.makeText(getApplicationContext(), "Place failed", Toast.LENGTH_SHORT).show();
                                else {
                                    Toast.makeText(getApplicationContext(), "Place Successful!", Toast.LENGTH_SHORT).show();
                                    Gem gem = findGem(objectId);
                                    if (gem != null)
                                        gem.isPlaced = true;
                                    addMarker(gem);
                                    updateBagUI(gem, false);
                                }

                            }
                        });
                    }

                }
            });
        }
        catch (Exception e)
        {
            createToast("placeGem exception");
        }


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
        if( object ==null) {
            createToast("ParseObjectToGem:object Null");
            return null;
        }
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
                        if(gem.username.equals(username) && gem.isPlaced)
                        addMarker(gem);
                        if(gem.username.equals(username) && !gem.isPlaced)
                        {
                            if(grabGemObjectIds.indexOf(gem.id)==-1)
                                updateBagUI(gem, true);
                        }
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
        if( gem ==null) {
            createToast("addMarker gem null");
            return;
        }
        Location gemLocation = gem.location;
        LatLng latLng = new LatLng(gemLocation.getLatitude(), gemLocation.getLongitude());
        gem.marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
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
    float distanceToGrab = 5.3f;
    ArrayList<Gem> takeGems = new ArrayList<>();
    void findGemsAtCurrentLocation()
    {
        try {
            minDistance = 10.0f;


            final Location location = gpsTracker.location;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // grabGems.clear();
                    ArrayList<Gem> temp = new ArrayList<Gem>(nearGems);
                    for (Gem gem : temp) {
                        float distance = location.distanceTo(gem.location);
                        if (distance <= minDistance) {
                                if (findGrabGem(gem.id) == null && !gem.username.equals(username)) {
                                    grabGems.add(gem);
                                    if(distance > distanceToGrab)
                                    updateGrabGemUI(gem, true, false);
                                }
                            } else {
                                if (findGrabGem(gem.id) != null) {
                                    grabGems.remove(gem);
                                    takeGems.remove(gem);
                                    updateGrabGemUI(gem, false, false);
                                }
                            }

                    }

                    for(Gem gem : grabGems)
                    {
                        float distance = location.distanceTo(gem.location);
                        if(distance <= distanceToGrab)
                        {
                            if(takeGems.indexOf(gem)==-1){
                                takeGems.add(gem);
                                updateGrabGemUI(gem,true,true);
                            }
                        }
                        else
                        {
                            if(takeGems.indexOf(gem)!=-1) {
                                updateGrabGemUI(gem, true, false);
                                takeGems.remove(gem);
                            }
                        }
                    }

                    handler.postDelayed(this, 200);
                }
            }, 200);
        }
        catch (Exception e)
        {
            createToast("Exception:findGemsAtCurrentLocation");
        }
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

    Gem findTakeGem(String objectId)
    {
        for(Gem gem : takeGems){
            if(gem != null && gem.id != null)
                if(gem.id.equals(objectId))
                    return gem;

        }
        return null;
    }





    List<String> grabGemObjectIds = new ArrayList<String>();

    void updateGrabGemUI(Gem gem , boolean add, boolean takable)
    {
        if(gem == null) {
            createToast("updateGramGemUI: gem null");
            return;
        }
        String gemName = gemType(gem.type);
        if(takable)
            gemName = gemName + " Take";
        if(add){
           if(grabGemObjectIds.indexOf(gem.id)==-1) {
               grabGemObjectIds.add(gem.id);
               ids.add(gemName);
           }
           else
           {
               int position = grabGemObjectIds.indexOf(gem.id);
               ids.set(position,gemName);

           }
           adapter.notifyDataSetChanged();
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

    ArrayList<Gem> bagGems = new ArrayList<>();
    ArrayList<String> bagGemObjectIds = new ArrayList<>();
    void updateBagUI(Gem gem , boolean add)
    {
        if( gem ==null) {
            createToast("gem null UpdateBagUI");
            return;
        }
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
                boolean isThere = bagGems.remove(gem);
                if(!isThere)
                {
                    createToast("NO ELEMENT!!!");
                }
                else
                    totalGemsInBag--;
            }
            catch (Exception e)
            {
                Log.i("Not found", "Element not found");
            }

        }
    }

    void createToast(String message)
    {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    String gemType(int type){
        if(type == 0)
            return "Gold";
        else if(type == 1)
            return "Silver";
        else
            return "Bronze";
    }

    int bagCapacity = 3, totalGemsInBag = 0;
    void onGrabGem(View view , int position){

        try {
            String id = grabGemObjectIds.get(position);
            Gem gem = findGem(id);
            if(takeGems.indexOf(gem) == -1)
            {
                createToast("Cannot Grab!");
                return;
            }
            if (totalGemsInBag >= bagCapacity) {
                Toast.makeText(getApplicationContext(), "Bag Full!", Toast.LENGTH_SHORT).show();
                return;
            }
            totalGemsInBag++;

            updateGrabGemUI(gem, false, false);
            ParseQuery query = ParseQuery.getQuery("Gem");
            query.whereEqualTo("objectId", id);
            query.setLimit(1);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() != 0 && objects.get(0).getBoolean("isPlaced"))
                        grabGem(objects.get(0));
                    else {
                        Toast.makeText(getApplicationContext(), "Someone grabbed it already!", Toast.LENGTH_SHORT).show();
                        totalGemsInBag--;
                    }

                }
            });
        }
        catch (Exception e)
        {
            createToast("Here is the bug! 752");
        }


       // loadAllGems();
    }

    void saveBagData()
    {
        editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String bagGemsJson = gson.toJson(bagGems);
            String bagOId = gson.toJson(bagGemObjectIds);
            editor.putString("bagGems", bagGemsJson);
            editor.putString("bagGemObjectIds",bagOId);
            editor.apply();

    }

    void bagDataInitialize()
    {
        ArrayList<Gem> bagGemsT = new ArrayList<>();
        try {
           String s = sharedPreferences.getString("bagGems", "noData");
            if(s.equals("noData"))
                return;
            Gson gson = new Gson();
            Type listOfGems = new TypeToken<List<Gem>>(){}.getType();
            bagGemsT = (ArrayList) gson.fromJson(s,listOfGems);
            for(Gem gem : bagGemsT)
            {
                updateBagUI(gem,true);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    void grabGem(ParseObject object)
    {
        try {
            final String objectId = object.getObjectId();
            object.put("isPlaced", false);
            object.put("location", new ParseGeoPoint(0, 0));
            object.put("username", username);
            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getApplicationContext(), "Successfully grabbed!", Toast.LENGTH_SHORT).show();
                        Gem gem = findGem(objectId);
                        updateGrabGemUI(gem, false, false);
                        gem.isPlaced = false;
                        gem.username = username;
                        updateBagUI(gem, true);
                    } else {
                        Toast.makeText(getApplicationContext(), "Network Error!", Toast.LENGTH_SHORT).show();
                        Gem gem = findGrabGem(objectId);
                        updateGrabGemUI(gem, true, false);
                        totalGemsInBag--;
                    }
                }
            });
        }
        catch (Exception e)
        {
            createToast("Exception:grabGem");
        }
    }

    void onClickBag()
    {

    }


}

