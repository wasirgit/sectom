package com.secto.model;

public class JobRegisterDto {
	private int job_register_id;
	private String job_name;
	private int job_number;
	private String customer_id;

	public int getJob_register_id() {
		return job_register_id;
	}

	public void setJob_register_id(int job_register_id) {
		this.job_register_id = job_register_id;
	}

	public String getJob_name() {
		return job_name;
	}

	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}

	public int getJob_number() {
		return job_number;
	}

	public void setJob_number(int job_number) {
		this.job_number = job_number;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

}
