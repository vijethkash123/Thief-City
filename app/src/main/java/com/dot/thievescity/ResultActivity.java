package com.dot.thievescity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ResultActivity extends AppCompatActivity {

    String username;
    int score = 0;
    public List<User> allUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
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

        username = ParseUser.getCurrentUser().getUsername();
        getUsers();
    }

    void getUsers()
    {
        ParseQuery query = new ParseQuery("User");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> users, ParseException e) {
                for(ParseObject userObject : users){
                    User user = parseObjectToUser(userObject);
                    allUsers.add(user);
                }
            }
        });
    }

    void getGems()
    {
        ParseQuery query = new ParseQuery("Gem");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> allGems, ParseException e) {
                for(ParseObject gem : allGems){


                }
            }
        });
    }

    int calculateScore(List<ParseObject> myObjects)
    {
        int score = 0 ;
        for (ParseObject object: myObjects) {
            score += valueOfGem(object.getInt("type"));
        }
        return  score;
    }

    User parseObjectToUser(ParseObject object)
    {
        return new User(object.getString("username"),0);
    }

    int valueOfGem(int type)
    {
        switch (type){
            case 0: return 1000;
            case 1: return 500;
            default: return 300;
        }
    }


}

class User{
    String username;
    int score;

    public User(String username, int score)
    {
        this.username = username;
        this.score = score;
    }
}
