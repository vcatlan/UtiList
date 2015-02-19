package com.android.model;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseUser;

public class FriendList {
	private String objectId;
	private ArrayList<FriendListItem> items;
	private String ownerId;

	public FriendList() {
		this.ownerId = "";
		this.ownerId = ParseUser.getCurrentUser().getObjectId();
	}

	public FriendList(FriendListParser listParser) {
		this.objectId = listParser.getObjectId();
		if (listParser.getItems() != null) {
			this.items = new Gson().fromJson(listParser.getItems(),
					new TypeToken<ArrayList<FriendListItem>>() {
					}.getType());
		}
		this.ownerId = ParseUser.getCurrentUser().getObjectId();
	}

	public FriendList(String objectId, ArrayList<FriendListItem> items,
			String ownerId) {
		this.objectId = objectId;
		this.items = items;
		this.ownerId = ownerId;
	}

	public FriendList(String objectId, String items, String ownerId) {
		this.objectId = objectId;
		if (items != null) {
			this.items = new Gson().fromJson(items,
					new TypeToken<ArrayList<FriendListItem>>() {
					}.getType());
		}
		this.ownerId = ownerId;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public ArrayList<FriendListItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<FriendListItem> items) {
		this.items = items;
	}

	public void setItems(String items) {
		if (items != null)
			this.items = new Gson().fromJson(items,
					new TypeToken<ArrayList<FriendListItem>>() {
					}.getType());
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public boolean isExist(String objectId) {
		for (FriendListItem item : items) {
			if (item.getObjectId().equals(objectId))
				return true;
		}
		return false;
	}

}
