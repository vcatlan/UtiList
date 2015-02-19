package com.android.model;

import java.util.HashSet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("History")
public class HistoryParser extends ParseObject {

	public HistoryParser() {

	}

	public HistoryParser(History history) {
		if (!history.getObjectId().equals(""))
			setObjectId(history.getObjectId());
		setOwner(ParseUser.getCurrentUser());
		setItem(new Gson().toJson(history.getItemNameHistoryList(),
				new TypeToken<HashSet<String>>() {
				}.getType()));
		setPrice(new Gson().toJson(history.getPriceHistoryList(),
				new TypeToken<HashSet<Double>>() {
				}.getType()));
		setItemTodo(new Gson().toJson(history.getItemTodoHistoryList(),
				new TypeToken<HashSet<String>>() {
				}.getType()));
	}

	public ParseUser getOwner() {
		return getParseUser("owner");
	}

	public void setOwner(ParseUser owner) {
		put("owner", owner);
	}

	public String getPrice() {
		return getString("price");
	}

	public void setPrice(String price) {
		put("price", price);
	}

	public String getItem() {
		return getString("item");
	}

	public void setItem(String item) {
		put("item", item);
	}

	public String getItemTodo() {
		return getString("itemTodo");
	}

	public void setItemTodo(String itemTodo) {
		put("itemTodo", itemTodo);
	}

}
