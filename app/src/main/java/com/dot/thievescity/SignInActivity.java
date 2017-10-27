package com.dot.thievescity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class SignInActivity extends AppCompatActivity {

   String usernameGlobal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isGameStarted())
        {
            Intent toGame = new Intent(getApplicationContext(),SecondGamePlayActivity.class);
            startActivity(toGame);
            finish();
        }
        setContentView(R.layout.activity_sign_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    boolean isGameStarted()
    {
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.dot.thievescity", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("secStarted", false);
    }

    public void onSignIn(View view)
    {
       // Log.i("I am here", "here");
        EditText usernameField = (EditText)findViewById(R.id.user_name_field);
        String username = usernameField.getText().toString();
        Log.i("Name",username);
        EditText passwordField = (EditText)findViewById(R.id.password_field);
        String password = passwordField.getText().toString();
        Log.i("Password",password);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user != null)
                {
                    usernameGlobal = user.getUsername();
                    checkUserAlreadyLoggedIn(usernameGlobal);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Login failed! Try again", Toast.LENGTH_LONG).show();
                }
            }
        });


     /* ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Gem");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e== null)
                {
                    Toast.makeText(getApplicationContext(),"This much time!", Toast.LENGTH_LONG).show();
                }
            }
        });
        */
    }
    //boolean loggedIn;
    void checkUserAlreadyLoggedIn(String username) {
        //loggedIn = false;
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("UserList");
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null)
                {
                    if(objects.size() == 0 || !objects.get(0).getBoolean("loggedIn"))
                        handleResult(false);
                    else
                         handleResult(true);

                }
            }
        });
    }

    void handleResult(boolean loggedIn)
    {
       if(!loggedIn)
       {
           Toast.makeText(getApplicationContext(),"Login Successful", Toast.LENGTH_LONG).show();
           ParseObject parseObject = new ParseObject("UserList");
           parseObject.put("username", usernameGlobal);
           parseObject.put("loggedIn", true);
           parseObject.saveInBackground();
           Intent toInstructionsActivity = new Intent(getApplicationContext(), InstructionsActivity.class);
           startActivity(toInstructionsActivity);
           finish();
       }
       else
       {
           Toast.makeText(getApplicationContext(),"User already logged in with another device", Toast.LENGTH_LONG).show();
       }
    }


}
