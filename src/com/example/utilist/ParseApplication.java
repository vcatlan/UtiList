package com.example.utilist;

import android.app.Application;

import com.example.model.FriendListParser;
import com.example.model.HistoryParser;
import com.example.model.SharedListParser;
import com.example.model.ShoppingListParser;
import com.example.model.TodoListParser;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
static final String APPLICATION_ID = "z2DSUy0lAebUaUJYcSO8qVIXMXptzK6HwB2LoeIk";
static final String CLIENT_KEY = "N595P7JAZERWHQckoScgFLtXmm0PNWmSzKqM24q9";

  @Override
  public void onCreate() {
    super.onCreate();

    // Add your initialization code here
    ParseObject.registerSubclass(ShoppingListParser.class);
    ParseObject.registerSubclass(HistoryParser.class);
    ParseObject.registerSubclass(TodoListParser.class);
    ParseObject.registerSubclass(FriendListParser.class);
    ParseObject.registerSubclass(SharedListParser.class);
    Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);


//    ParseUser.enableAutomaticUser();
//    ParseACL defaultACL = new ParseACL();
//      
//    // If you would like all objects to be private by default, remove this line.
//    defaultACL.setPublicReadAccess(true);
//    
//    ParseACL.setDefaultACL(defaultACL, true);
  }
}
