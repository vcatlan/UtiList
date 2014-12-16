package com.example.model;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
@ParseClassName("ShoppingList")
public class ShoppingListParser extends ParseObject{ 
	
	public ShoppingListParser() {

	}
	public ShoppingListParser(ShoppingList list){
		if(!list.getObjectId().equals(""))
			setObjectId(list.getObjectId());
		setTitle(list.getTitle());
		setOnwer(ParseUser.getCurrentUser());
		setItems(new Gson().toJson(list.getItems(), new TypeToken<ArrayList<ShoppingListItem>>() {}.getType()));
		setTotal(list.getTotal());
		setRemaining(list.getRemaining());
		setOutstandingItem(list.getOutstandingItem());
	}
	

	public String getTitle() {
		return getString("title");
	}
	public void setTitle(String title) {
		put("title", title);
	}
	public String getItems() {
		return getString("items");
	}
	public void setItems(String items) {
		
		put("items", items);
	}
	public ParseUser getOnwer() {
		return getParseUser("onwer");
	}
	public void setOnwer(ParseUser onwer) {
		put("onwer", onwer);
	}
	public double getTotal() {
		return getDouble("total");
	}
	public void setTotal(double total) {
		put("total", total);
	}
	public double getRemaining() {
		return getDouble("remaining");
	}
	public void setRemaining(double remaining) {
		put("remaining", remaining);
	}
	public String getOutstandingItem() {
		return getString("outstandingItem");
	}
	public void setOutstandingItem(String outstandingItem) {
		put("outstandingItem", outstandingItem);
	}
	
}
