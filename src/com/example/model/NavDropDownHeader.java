package com.example.model;


public class NavDropDownHeader implements NavDropDownItem {

    public static final int SECTION_TYPE = 0;
    private int id;
    private String label;

    private NavDropDownHeader() {
    	
    }
    
    public NavDropDownHeader(int id, String label) {
		this.id = id;
		this.label = label;
	}

	@Override
    public int getType() {
        return SECTION_TYPE;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
