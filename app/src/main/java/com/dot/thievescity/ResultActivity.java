package com.dot.thievescity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    String username;
    int score = 0;
    public List<User> allUsers;
    ListView listView;
    TextView textView;
    User me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        allUsers = new ArrayList<>();
        textView = (TextView) findViewById(R.id.textView_loading);
        //allGems = new ArrayList<>();
        username = ParseUser.getCurrentUser().getUsername();
        //initializeListView();
        //getUsers();
       // getGems();
        //sortList();
        displayScore();
    }

    List<String> texts = new ArrayList<>();
    void createTextList()
    {
        texts.add("Rank\tTeam Name\tScore");
        int rank = 1;
        for(User user: allUsers)
        {
            texts.add(rank+"\t"+user.username+"\t"+user.score);
            rank++;
        }
        //initializeListView();
    }

    ArrayAdapter adapter;
    void initializeListView()
    {
        //listView = (ListView)findViewById(R.id.result_list);
        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,texts);
        listView.setClickable(false);
        listView.setAdapter(adapter);
    }

    void getUsers()
    {
        ParseQuery query = new ParseQuery("UserList");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> users, ParseException e) {
                for(ParseObject userObject : users){
                    User user = parseObjectToUser(userObject);
                    allUsers.add(user);
                    if(user.username.equals(username))
                    me = user;
                }
                getGems();
            }
        });
    }

    void getGems()
    {
        ParseQuery query = new ParseQuery("Gem");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> allGems, ParseException e) {
                for(ParseObject gem : allGems){
                    User user  = findUser(gem);
                    if(user==null)continue;
                    user.score += valueOfGem(gem.getInt("type"));
                }
                sortList();
                displayResult();
            }
        });
    }

    void displayResult()
    {
         int rank = allUsers.indexOf(me) + 1;
         int score = me.score;
         textView.setText("Team Name: " + username+"\n" + "Score: " + score + "\n" + "Rank: " + rank);
    }

    User findUser(ParseObject gem)
    {
        for(User user : allUsers)
        {
            if(user.username.equals(gem.getString("username")))
                return user;
        }
        return null;
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

    void sortList()
    {
        Collections.sort(allUsers, new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                if (u1.score < u2.score)
                    return 1;
                if (u1.score >= u2.score)
                    return -1;
                return 0;
            }
        });
    }

    void displayScore()
    {
        score = 0;
        ParseQuery query = new ParseQuery("Gem");
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> allGems, ParseException e) {
                for(ParseObject gem : allGems){
                    int type = gem.getInt("type");
                    int value = valueOfGem(type);
                    score = score + value;
                }
                textView.setText("TeamName: "+username+ "\n" + "Score: " + score);

                updateScore();
            }
        });


    }

    void updateScore()
    {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("UserList");
            query.whereEqualTo("username", username);
            query.setLimit(1);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> scoreList, ParseException e) {
                    if (e == null) {
                        if(scoreList.size()== 0)
                            return;
                        ParseObject object = scoreList.get(0);
                        object.put("score", score);
                        object.saveInBackground();

                    } else {
                        Log.i("Error", "Something happened");

                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
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


