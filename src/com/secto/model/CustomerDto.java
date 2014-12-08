package com.secto.model;

public class CustomerDto {
	private int customer_id;
	private String customer_name;
	private String customer_full_name;
	private String color_code;
	private int customer_status;

	public int getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getCustomer_full_name() {
		return customer_full_name;
	}

	public void setCustomer_full_name(String customer_full_name) {
		this.customer_full_name = customer_full_name;
	}

	public String getColor_code() {
		return color_code;
	}

	public void setColor_code(String color_code) {
		this.color_code = color_code;
	}

	public int getCustomer_status() {
		return customer_status;
	}

	public void setCustomer_status(int customer_status) {
		this.customer_status = customer_status;
	}

}
