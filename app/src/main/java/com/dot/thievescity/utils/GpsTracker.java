package com.dot.thievescity.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.dot.thievescity.SecondGamePlayActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Ahmet Ertugrul OZCAN
 * Cihazin konum bilgisini goruntuler
 */
public class GpsTracker extends Service implements LocationListener {
    private final Context mContext;
    GoogleMap mMap;
    // Cihazda gps acik mi?
    boolean isGPSEnabled = false;

    // Cihazda veri baglantisi aktif mi?
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;
    Marker currentLocationMarker;

    // Konum
    public Location location = null;
    // Enlem
    double latitude;
    // Boylam
    double longitude;

    // Konum guncellemesi gerektirecek minimum degisim miktari
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // metre

    // Konum guncellemesi gerektirecek minimum sure miktari
    private static final long MIN_TIME_BW_UPDATES = 1; // dakika

    // LocationManager nesnesi
    protected LocationManager locationManager;

    public SecondGamePlayActivity myActivity;
    boolean onOnce = false;

    //
    // Kurucu Metod - Constructor
    //
    public GpsTracker(Context context, GoogleMap map, SecondGamePlayActivity myActivity) {
        this.mContext = context;
        this.mMap=map;
        this.myActivity = myActivity;
        //location = new Location("dummyProvider");
        location = getLocation();
    }

    public GpsTracker(Context context, GoogleMap map) {
        this.mContext = context;
        this.mMap=map;
        location = getLocation();
    }
     public GpsTracker(Context context)
     {
         this.mContext = context;
         location = getLocation();
     }

    //
    // Konum bilgisini dondurur
    //
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            // GPS acik mi?
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // Internet acik mi?
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.canGetLocation = true;

                // Once internetten alinan konum bilgisi kayitlanir
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Log.d("GPS", "not permitted");
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return null;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null)
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                // GPS'ten alinan konum bilgisi
                if (isGPSEnabled)
                {
                    if (!onOnce)
                    {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.i("GPS Enabled", "GPS Enabled");
                        Toast.makeText(myActivity,"Here", Toast.LENGTH_LONG).show();
                        if (locationManager != null)
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null)
                            {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                        onOnce = true;
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(),"Please enable GPS and/or Internet", Toast.LENGTH_LONG);
        }

        return location;
    }

    // Enlem bilgisini dondurur
    public double getLatitude()
    {
        if(location != null)
        {
            latitude = location.getLatitude();
        }

        return latitude;
    }

    // Boylam bilgisini dondurur
    public double getLongitude()
    {
        if(location != null)
        {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    @Override
    public void onLocationChanged(Location locationLocal)
    {

        Log.i("location","changed");
        if(myActivity != null)
        myActivity.loadGemsNearBy();
        LatLng yourLoc = new LatLng(location.getLatitude(), location.getLongitude());
        location.setLatitude(locationLocal.getLatitude());
        location.setLongitude(locationLocal.getLongitude());
        //mMap.clear();
        if(currentLocationMarker!=null)
            currentLocationMarker.remove();
         currentLocationMarker = mMap.addMarker(new MarkerOptions().position(yourLoc).title("Marker in User Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yourLoc,15));
    }

    @Override
    public void onProviderDisabled(String provider)
    {
    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    public boolean canGetLocation()
    {
        return this.canGetLocation;
    }

    // Konum bilgisi kapali ise kullaniciya ayarlar sayfasina baglanti iceren bir mesaj goruntulenir
    public void showSettingsAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Mesaj basligi
        alertDialog.setTitle("GPS Kapalı");

        // Mesaj
        alertDialog.setMessage("Konum bilgisi alınamıyor. Ayarlara giderek gps'i aktif hale getiriniz.");

        // Mesaj ikonu
        //alertDialog.setIcon(R.drawable.delete);

        // Ayarlar butonuna tiklandiginda
        alertDialog.setPositiveButton("Ayarlar", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // Iptal butonuna tiklandiginda
        alertDialog.setNegativeButton("İptal", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        // Mesaj kutusunu goster
        alertDialog.show();
    }

    // LocationManager'in gps isteklerini durdurur
    public void stopUsingGPS()
    {
        if(locationManager != null)
        {
            locationManager.removeUpdates(GpsTracker.this);
        }
    }
}