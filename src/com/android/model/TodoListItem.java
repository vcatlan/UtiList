package com.android.model;

import java.io.Serializable;

public class TodoListItem implements Serializable {
	private String name;
	private String date;
	private String time;
	private String note;
	private boolean checked;;

	public TodoListItem() {
		this.name = "";
		this.date = "";
		this.time = "";
		this.note = "";
		this.checked = false;
	}

	public TodoListItem(String name, String date, String time, String note,
			boolean checked) {
		this.name = name;
		this.date = date;
		this.time = time;
		this.note = note;
		this.checked = checked;
	}

	public TodoListItem(String name) {
		this.name = name;
		this.date = "...";
		this.time = "...";
		this.note = "";
		this.checked = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public void setChecked() {
		if (!checked)
			this.checked = true;
		else
			this.checked = false;
	}

	public String getItemDetail() {
		String detail = "";
		if (getDate().equals("...") && getNote().equals(""))
			detail = "";
		else if (getDate().equals("...") && !getNote().equals(""))
			detail = getNote();
		else if (!getDate().equals("...") && getNote().equals(""))
			detail = getDate() + " " + getTime();
		else if (!getDate().equals("...") && !getNote().equals(""))
			detail = getDate() + " " + getTime() + getNote();
		return detail;
	}

	@Override
	public String toString() {
		return "TodoListItem [name=" + name + ", date=" + date + ", time="
				+ time + ", note=" + note + ", checked=" + checked + "]";
	}

}
