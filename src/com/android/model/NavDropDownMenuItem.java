package com.android.model;

public class NavDropDownMenuItem implements NavDropDownItem {

	public static final int ITEM_TYPE = 1;
	private int id;
	private String label;

	private NavDropDownMenuItem() {

	}

	public NavDropDownMenuItem(int id, String label) {
		this.id = id;
		this.label = label;
	}

	@Override
	public int getType() {
		return ITEM_TYPE;
	}

	@Override
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
