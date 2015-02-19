package com.android.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("SharedList")
public class SharedListParser extends ParseObject {

	public SharedListParser() {

	}

	public SharedListParser(SharedList list) {
		if (!list.getObjectId().equals(""))
			setObjectId(list.getObjectId());
		setListObjectId(list.getListObjectId());
		setSharedByObjectId(list.getSharedByObjectId());
		setListTitle(list.getListTitle());
		setSharedByName(list.getSharedByName());
		setNewList(list.isNewList());
		setListType(list.getListType());
		setSharedToObjectId(list.getSharedToObjectId());
	}

	public String getListObjectId() {
		return getString("listObjectId");
	}

	public void setListObjectId(String listObjectId) {
		put("listObjectId", listObjectId);
	}

	public String getSharedByObjectId() {
		return getString("sharedByObjectId");
	}

	public void setSharedByObjectId(String sharedByObjectId) {
		put("sharedByObjectId", sharedByObjectId);
	}

	public String getListTitle() {
		return getString("listTitle");
	}

	public void setListTitle(String listTitle) {
		put("listTitle", listTitle);
	}

	public String getSharedByName() {
		return getString("sharedByName");
	}

	public void setSharedByName(String sharedByName) {
		put("sharedByName", sharedByName);
	}

	public boolean isNewList() {
		return getBoolean("newList");
	}

	public void setNewList(boolean newList) {
		put("newList", newList);
	}

	public String getListType() {
		return getString("listType");
	}

	public void setListType(String listType) {
		put("listType", listType);
	}

	public String getSharedToObjectId() {
		return getString("sharedToObjectId");
	}

	public void setSharedToObjectId(String sharedToObjectId) {
		put("sharedToObjectId", sharedToObjectId);
	}

}
