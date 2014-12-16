package com.example.model;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
@ParseClassName("FriendList")
public class FriendListParser extends ParseObject{
	
	public FriendListParser(){
		
	}
	
	public FriendListParser(FriendList list){
		if(!list.getObjectId().equals(""))
			setObjectId(list.getObjectId());
		setItems(new Gson().toJson(list.getItems(), new TypeToken<ArrayList<FriendListItem>>() {}.getType()));
		setOwner(ParseUser.getCurrentUser());
	}
	
	public String getItems() {
		return getString("items");
	}
	public void setItems(String items) {
		
		put("items", items);
	}
	public ParseUser getOwner() {
		return getParseUser("onwer");
	}
	public void setOwner(ParseUser owner) {
		put("owner", owner);
	}
	
}
