package com.android.model;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("TodoList")
public class TodoListParser extends ParseObject {

	public TodoListParser() {

	}

	public TodoListParser(TodoList todoList) {
		if (!todoList.getObjectId().equals(""))
			setObjectId(todoList.getObjectId());
		setTitle(todoList.getTitle());
		setOnwer(ParseUser.getCurrentUser());
		setItems(new Gson().toJson(todoList.getItems(),
				new TypeToken<ArrayList<TodoListItem>>() {
				}.getType()));
		setOutstandingItem(todoList.getOutstandingItem());
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

	public ParseUser getOwner() {
		return getParseUser("owner");
	}

	public void setOnwer(ParseUser owner) {
		put("owner", owner);
	}

	public String getOutstandingItem() {
		return getString("outstandingItem");
	}

	public void setOutstandingItem(String outstandingItem) {
		put("outstandingItem", outstandingItem);
	}
}
