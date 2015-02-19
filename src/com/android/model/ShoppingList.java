package com.android.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseUser;

public class ShoppingList implements Serializable {
	private String objectId = "";
	private String title;
	private ArrayList<ShoppingListItem> items;
	private String onwerId;
	private double total;
	private double remaining;
	private String outstandingItem;
	private Date updatedDate;
	private Date createdDate;

	public ShoppingList() {

	}

	public ShoppingList(String title, ArrayList<ShoppingListItem> items) {
		this.title = title;
		this.items = items;
	}

	public ShoppingList(String title) {
		this.title = title;
		this.items = new ArrayList<ShoppingListItem>();
		setTRO();
	}

	public ShoppingList(ShoppingListParser listParser) {
		this.objectId = listParser.getObjectId();
		this.title = listParser.getTitle();
		if (listParser.getItems() != null) {
			this.items = new Gson().fromJson(listParser.getItems(),
					new TypeToken<ArrayList<ShoppingListItem>>() {
					}.getType());
		}
		this.onwerId = ParseUser.getCurrentUser().getObjectId();
		this.total = listParser.getTotal();
		this.remaining = listParser.getRemaining();
		this.outstandingItem = listParser.getOutstandingItem();
		this.updatedDate = listParser.getUpdatedAt();
		this.createdDate = listParser.getCreatedAt();
	}

	public ShoppingList(String objectId, String title, String items,
			double total, double remaining, String outstandingItem,
			Date updatedDate, Date createdDate) {
		this.objectId = objectId;
		this.title = title;
		if (items != null) {
			this.items = new Gson().fromJson(items,
					new TypeToken<ArrayList<ShoppingListItem>>() {
					}.getType());
		}
		this.onwerId = ParseUser.getCurrentUser().getObjectId();
		this.total = total;
		this.remaining = remaining;
		this.outstandingItem = outstandingItem;
		this.updatedDate = updatedDate;
		this.createdDate = createdDate;
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
		setOnwerId(ParseUser.getCurrentUser().getObjectId());
		items = new ArrayList<ShoppingListItem>();
		setTotal();
		setRemaining();
		setOutstandingItem();
	}

	public ArrayList<ShoppingListItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<ShoppingListItem> items) {
		this.items = items;
	}

	public void setItems(String items) {
		if (items != null) {
			this.items = new Gson().fromJson(items,
					new TypeToken<ArrayList<ShoppingListItem>>() {
					}.getType());
		}
	}

	public String getOnwerId() {
		return onwerId;
	}

	public void setOnwerId(String onwerId) {
		this.onwerId = onwerId;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal() {
		double total = 0;
		for (int i = 0; i < items.size(); i++) {
			total += (items.get(i).getUnitPrice() * items.get(i).getQuantity());
		}
		this.total = total;
	}

	public double getRemaining() {
		return remaining;
	}

	public void setRemaining() {
		double remaining = getTotal();
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).isChecked())
				remaining -= (items.get(i).getUnitPrice() * items.get(i)
						.getQuantity());
		}
		this.remaining = remaining;
	}

	public String getOutstandingItem() {
		return outstandingItem;
	}

	public void setOutstandingItem() {
		String outstandingItem = "";
		int checkedItem = items.size();
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).isChecked())
				checkedItem--;
		}
		if (checkedItem == 0)
			outstandingItem = "No outstanding items";
		else
			outstandingItem = checkedItem + " outstanding items";
		this.outstandingItem = outstandingItem;
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

	public void setTRO() {
		setTotal();
		setRemaining();
		setOutstandingItem();
	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other == this)
			return true;
		if (!(other instanceof ShoppingList))
			return false;
		if (this.objectId.equals(((ShoppingList) other).getObjectId()))
			return true;
		else
			return false;
	}

	@Override
	public String toString() {
		return "ShoppingList [objectId=" + objectId + ", title=" + title
				+ ", items=" + items + ", onwerId=" + onwerId + ", total="
				+ total + ", remaining=" + remaining + ", outstandingItem="
				+ outstandingItem + ", updatedDate=" + updatedDate
				+ ", createdDate=" + createdDate + "]";
	}
}
