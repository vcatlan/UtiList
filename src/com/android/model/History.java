package com.android.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.ParseUser;

public class History implements Serializable {
	private String objectId = "";
	private Set<Double> priceHistoryList = new HashSet<Double>();
	private Set<String> itemNameHistoryList = new HashSet<String>();
	private Set<String> itemTodoHistoryList = new HashSet<String>();
	private String ownerId;

	public History() {

	}

	public History(String objectId, Set<Double> priceHistoryList,
			Set<String> itemNameHistoryList, Set<String> itemTodoHistoryList) {
		this.objectId = objectId;
		this.priceHistoryList = priceHistoryList;
		this.itemNameHistoryList = itemNameHistoryList;
		this.itemTodoHistoryList = itemTodoHistoryList;
		this.ownerId = ParseUser.getCurrentUser().getObjectId();
	}

	public History(Set<Double> priceHistoryList,
			Set<String> itemNameHistoryList, Set<String> itemTodoHistoryList) {
		this.priceHistoryList = priceHistoryList;
		this.itemNameHistoryList = itemNameHistoryList;
		this.itemTodoHistoryList = itemTodoHistoryList;
		this.ownerId = ParseUser.getCurrentUser().getObjectId();
	}

	public History(String priceHistoryList, String itemNameHistoryList,
			String itemTodoHistoryList) {
		this.priceHistoryList = new Gson().fromJson(priceHistoryList,
				new TypeToken<HashSet<Double>>() {
				}.getType());
		this.itemNameHistoryList = new Gson().fromJson(itemNameHistoryList,
				new TypeToken<HashSet<String>>() {
				}.getType());
		this.itemTodoHistoryList = new Gson().fromJson(itemTodoHistoryList,
				new TypeToken<HashSet<String>>() {
				}.getType());
		this.ownerId = ParseUser.getCurrentUser().getObjectId();
	}

	public History(HistoryParser historyParser) {
		this.objectId = historyParser.getObjectId();
		this.priceHistoryList = new Gson().fromJson(historyParser.getPrice(),
				new TypeToken<HashSet<Double>>() {
				}.getType());
		this.itemNameHistoryList = new Gson().fromJson(historyParser.getItem(),
				new TypeToken<HashSet<String>>() {
				}.getType());
		this.itemTodoHistoryList = new Gson().fromJson(
				historyParser.getItemTodo(), new TypeToken<HashSet<String>>() {
				}.getType());
		this.ownerId = ParseUser.getCurrentUser().getObjectId();
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public Set<Double> getPriceHistoryList() {
		return priceHistoryList;
	}

	public void setPriceHistoryList(Set<Double> priceHistoryList) {
		this.priceHistoryList = priceHistoryList;
	}

	public void setPriceHistoryList(String priceHistoryList) {
		this.priceHistoryList = new Gson().fromJson(priceHistoryList,
				new TypeToken<HashSet<Double>>() {
				}.getType());
	}

	public Set<String> getItemNameHistoryList() {
		return itemNameHistoryList;
	}

	public void setItemNameHistoryList(Set<String> itemNameHistoryList) {
		this.itemNameHistoryList = itemNameHistoryList;
	}

	public void setItemNameHistoryList(String itemNameHistoryList) {
		this.itemNameHistoryList = new Gson().fromJson(itemNameHistoryList,
				new TypeToken<HashSet<String>>() {
				}.getType());
	}

	public Set<String> getItemTodoHistoryList() {
		return itemTodoHistoryList;
	}

	public void setItemTodoHistoryList(Set<String> itemTodoHistoryList) {
		this.itemTodoHistoryList = itemNameHistoryList;
	}

	public void setItemTodoHistoryList(String itemTodoHistoryList) {
		this.itemTodoHistoryList = new Gson().fromJson(itemTodoHistoryList,
				new TypeToken<HashSet<String>>() {
				}.getType());
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public CharSequence[] getItemCarSequence() {
		CharSequence[] charSequences = new CharSequence[itemNameHistoryList
				.size()];
		int i = 0;
		for (String item : itemNameHistoryList) {
			charSequences[i] = item;
			i++;
		}
		return charSequences;
	}

	public CharSequence[] getItemTodoCarSequence() {
		CharSequence[] charSequences = new CharSequence[itemTodoHistoryList
				.size()];
		int i = 0;
		for (String item : itemTodoHistoryList) {
			charSequences[i] = item;
			i++;
		}
		return charSequences;
	}

	public CharSequence[] getPriceCarSequence() {
		CharSequence[] charSequences = new CharSequence[priceHistoryList.size()];
		int i = 0;
		for (Double price : priceHistoryList) {
			charSequences[i] = Double.toString(price);
			i++;
		}
		return charSequences;
	}

	@Override
	public String toString() {
		return "History [objectId=" + objectId + ", priceHistoryList="
				+ priceHistoryList + ", itemNameHistoryList="
				+ itemNameHistoryList + ", ownerId=" + ownerId + "]";
	}

}
