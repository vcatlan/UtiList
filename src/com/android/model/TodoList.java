package com.android.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseUser;

public class TodoList implements Serializable {
	private String objectId = "";
	private String title;
	private ArrayList<TodoListItem> items;
	private String ownerId;
	private Date updatedDate;
	private Date createdDate;
	private String outstandingItem;

	public TodoList() {

	}

	public TodoList(String title) {
		this.title = title;
		this.items = new ArrayList<TodoListItem>();
		setOutstandingItem();
	}

	public TodoList(String objectId, String title,
			ArrayList<TodoListItem> items, String ownerId, Date updatedDate,
			Date createdDate) {
		this.objectId = objectId;
		this.title = title;
		this.items = items;
		this.ownerId = ParseUser.getCurrentUser().getObjectId();
		this.updatedDate = updatedDate;
		this.createdDate = createdDate;
		setOutstandingItem();
	}

	public TodoList(TodoListParser listParser) {
		this.objectId = listParser.getObjectId();
		this.title = listParser.getTitle();
		if (listParser.getItems() != null) {
			this.items = new Gson().fromJson(listParser.getItems(),
					new TypeToken<ArrayList<TodoListItem>>() {
					}.getType());
		}
		this.ownerId = ParseUser.getCurrentUser().getObjectId();
		this.updatedDate = listParser.getUpdatedAt();
		this.outstandingItem = listParser.getOutstandingItem();
		this.createdDate = listParser.getCreatedAt();
	}

	public TodoList(String objectId, String title,
			ArrayList<TodoListItem> items, Date updatedDate, Date createdDate) {
		this.objectId = objectId;
		this.title = title;
		this.items = items;
		this.ownerId = ParseUser.getCurrentUser().getObjectId();
		this.updatedDate = updatedDate;
		this.createdDate = createdDate;
		setOutstandingItem();
	}

	public TodoList(String objectId, String title, String items,
			String ownerId, Date updatedDate, Date createdDate) {
		this.objectId = objectId;
		this.title = title;
		if (items != null) {
			this.items = new Gson().fromJson(items,
					new TypeToken<ArrayList<TodoListItem>>() {
					}.getType());
		}
		this.ownerId = ParseUser.getCurrentUser().getObjectId();
		this.updatedDate = updatedDate;
		this.createdDate = createdDate;
		setOutstandingItem();
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		this.items = new ArrayList<TodoListItem>();
		this.ownerId = ParseUser.getCurrentUser().getObjectId();
		setOutstandingItem();
	}

	public ArrayList<TodoListItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<TodoListItem> items) {
		this.items = items;
	}

	public void setItems(String items) {
		if (items != null) {
			this.items = new Gson().fromJson(items,
					new TypeToken<ArrayList<TodoListItem>>() {
					}.getType());
		}
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getOutstandingItem() {
		return outstandingItem;
	}

	public void setOutstandingItem() {
		String outstandingItem = "";
		if (items != null) {
			int checkedItem = items.size();
			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).isChecked())
					checkedItem--;
			}
			if (checkedItem == 0)
				outstandingItem = "No outstanding items";
			else
				outstandingItem = checkedItem + " outstanding items";
		}
		this.outstandingItem = outstandingItem;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof TodoList))
			return false;
		if (this.objectId.equals(((TodoList) other).getObjectId()))
			return true;
		else
			return false;
	}

	@Override
	public String toString() {
		return "TodoList [objectId=" + objectId + ", title=" + title
				+ ", items=" + items + ", ownerId=" + ownerId
				+ ", updatedDate=" + updatedDate + ", createdDate="
				+ createdDate + ", outstandingItem=" + outstandingItem + "]";
	}

}
