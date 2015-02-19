package com.android.model;

import java.io.Serializable;

public class FriendListItem implements Serializable {
	private String objectId;
	private String firstName;
	private String lastName;
	private String email;

	public FriendListItem() {

	}

	public FriendListItem(String objectId, String firstName, String lastName,
			String email) {
		this.objectId = objectId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstLastName() {
		return this.firstName + " " + this.lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "FriendListItem [objectId=" + objectId + ", firstName="
				+ firstName + ", lastName=" + lastName + ", email=" + email
				+ "]";
	}
}
