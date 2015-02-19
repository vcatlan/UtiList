package com.android.model;

import java.io.Serializable;

public class ShoppingListItem implements Serializable {
	private String name;
	private String type; // ex. gallon, kg, cup..
	private int quantity;
	private double unitPrice;
	private String note;
	private boolean checked;

	public ShoppingListItem(String name, String type, int quantity,
			double unitPrice, String note) {
		super();
		this.name = name;
		this.type = type;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.note = note;
		this.checked = false;
	}

	public ShoppingListItem(String name) {
		this.name = name;
		this.type = "none";
		this.quantity = 1;
		this.unitPrice = 0.00;
		this.note = "";
		this.checked = false;
	}

	public ShoppingListItem() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
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
		if (getUnitPrice() == 0.00 && getType().equals("none")
				&& getQuantity() == 1) {
			detail = "";
		} else if (getUnitPrice() > 0.00 && getType().equals("none")
				&& getQuantity() == 1) {
			detail = "$ " + Double.toString(getUnitPrice());
		} else if (getUnitPrice() == 0.00 && getType().equals("none")
				&& getQuantity() > 1) {
			detail = "Qty " + Integer.toString(getQuantity());
		} else if (getUnitPrice() > 0.00 && getType().equals("none")
				&& getQuantity() > 1) {
			detail = "Qty " + Integer.toString(getQuantity()) + " @ $"
					+ Double.toString(getUnitPrice());
		} else if (getUnitPrice() == 0.00 && !getType().equals("one")
				&& getQuantity() == 1) {
			detail = Integer.toString(getQuantity()) + " " + getType();
		} else if (getUnitPrice() > 0.00 && !getType().equals("none")) {
			detail = Integer.toString(getQuantity()) + " " + getType() + " @ $"
					+ Double.toString(getUnitPrice());
		}
		return detail;
	}

	@Override
	public String toString() {
		return "Item [name=" + name + ", type=" + type + ", quantity="
				+ quantity + ", unitPrice=" + unitPrice + ", note=" + note
				+ "]";
	}
}
