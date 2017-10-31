package com.dot.thievescity;

import android.app.Application;
import android.util.Log;

import com.parse.LiveQueryException;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseLiveQueryClient;
import com.parse.ParseObject;
import com.parse.ParseUser;


/**
 * Created by Sanath on 10/11/2017.
 */

public class ThievesCity extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
       // ParseObject.registerSubclass(Gem.class);

        // Add your initialization code here
       /* Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("7504ba215dd57dcd2585e9406d50832ed4dda57e")
                .clientKey("1aea9245f094279bdd6bd46419fb565da1500a61")
                .server("http://18.220.173.24:80/parse")
                .build()
        ); */


        //LiveQueryClient.init("","7504ba215dd57dcd2585e9406d50832ed4dda57e",true);
        //LiveQueryClient.init("ws://18.220.173.24:80/parse","7504ba215dd57dcd2585e9406d50832ed4dda57e",true);
      //  LiveQueryClient.connect();
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("anotherId")
                .clientKey("myClient")
                .server("http://192.168.1.7:1337/parse")
                .build()
        );


        ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}