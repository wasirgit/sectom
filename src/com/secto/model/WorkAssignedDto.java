package com.secto.model;

public class WorkAssignedDto {
	private String date_name;
	private String customer_name;
	private String site_name;
	private String site_username;

	public String getDate_name() {
		return date_name;
	}

	public void setDate_name(String date_name) {
		this.date_name = date_name;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getSite_name() {
		return site_name;
	}

	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}

	public String getSite_username() {
		return site_username;
	}

	public void setSite_username(String site_username) {
		this.site_username = site_username;
	}

}
