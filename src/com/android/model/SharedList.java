package com.android.model;

import java.io.Serializable;

public class SharedList implements Serializable {
	private String objectId;
	private String listObjectId;
	private String sharedByObjectId;
	private String listTitle;
	private String sharedByName;
	private boolean newList;
	private String listType;
	private String sharedToObjectId;

	public SharedList() {

	}

	public SharedList(String listObjectId, String sharedByObjectId,
			String listTitle, String sharedByName, boolean newList,
			String listType, String sharedToObjectId) {
		this.objectId = "";
		this.listObjectId = listObjectId;
		this.sharedByObjectId = sharedByObjectId;
		this.listTitle = listTitle;
		this.sharedByName = sharedByName;
		this.newList = newList;
		this.listType = listType;
		this.sharedToObjectId = sharedToObjectId;
	}

	public SharedList(SharedListParser listParser) {
		this.objectId = listParser.getObjectId();
		this.listObjectId = listParser.getListObjectId();
		this.sharedByObjectId = listParser.getSharedByObjectId();
		this.listTitle = listParser.getListTitle();
		this.sharedByName = listParser.getSharedByName();
		this.newList = listParser.isNewList();
		this.listType = listParser.getListType();
		this.sharedToObjectId = listParser.getSharedToObjectId();
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getListObjectId() {
		return listObjectId;
	}

	public void setListObjectId(String listObjectId) {
		this.listObjectId = listObjectId;
	}

	public String getSharedByObjectId() {
		return sharedByObjectId;
	}

	public void setSharedByObjectId(String sharedByObjectId) {
		this.sharedByObjectId = sharedByObjectId;
	}

	public String getListTitle() {
		return listTitle;
	}

	public void setListTitle(String listTitle) {
		this.listTitle = listTitle;
	}

	public String getSharedByName() {
		return sharedByName;
	}

	public void setSharedByName(String sharedByName) {
		this.sharedByName = sharedByName;
	}

	public boolean isNewList() {
		return newList;
	}

	public void setNewList(boolean newList) {
		this.newList = newList;
	}

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public String getSharedToObjectId() {
		return sharedToObjectId;
	}

	public void setSharedToObjectId(String sharedToObjectId) {
		this.sharedToObjectId = sharedToObjectId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof SharedList))
			return false;
		if ((this.listObjectId.equals(((SharedList) o).getListObjectId()))
				&& (this.sharedToObjectId.equals(((SharedList) o)
						.getSharedToObjectId())))
			return true;
		else
			return false;
	}

	@Override
	public String toString() {
		return "SharedList [objectId=" + objectId + ", listObjectId="
				+ listObjectId + ", sharedByObjectId=" + sharedByObjectId
				+ ", listTitle=" + listTitle + ", sharedByName=" + sharedByName
				+ ", newList=" + newList + ", listType=" + listType
				+ ", sharedToObjectId=" + sharedToObjectId + "]";
	}

}
