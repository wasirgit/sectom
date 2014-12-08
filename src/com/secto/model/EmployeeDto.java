package com.secto.model;

public class EmployeeDto {
	private int employee_id;
	private String billable_hours_id;
	private int employee_type;
	private String employee_date_of_birth;
	private int employee_status;
	private String employee_first_name;
	private String employee_last_name;
	private String employee_pps_number;
	private String password;
	
	public int getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(int employee_id) {
		this.employee_id = employee_id;
	}

	public String getBillable_hours_id() {
		return billable_hours_id;
	}
	public void setBillable_hours_id(String billable_hours_id) {
		this.billable_hours_id = billable_hours_id;
	}
	public int getEmployee_type() {
		return employee_type;
	}
	public void setEmployee_type(int employee_type) {
		this.employee_type = employee_type;
	}
	public String getEmployee_date_of_birth() {
		return employee_date_of_birth;
	}
	public void setEmployee_date_of_birth(String employee_date_of_birth) {
		this.employee_date_of_birth = employee_date_of_birth;
	}
	public int getEmployee_status() {
		return employee_status;
	}
	public void setEmployee_status(int employee_status) {
		this.employee_status = employee_status;
	}
	public String getEmployee_first_name() {
		return employee_first_name;
	}
	public void setEmployee_first_name(String employee_first_name) {
		this.employee_first_name = employee_first_name;
	}
	public String getEmployee_last_name() {
		return employee_last_name;
	}
	public void setEmployee_last_name(String employee_last_name) {
		this.employee_last_name = employee_last_name;
	}
	public String getEmployee_pps_number() {
		return employee_pps_number;
	}
	public void setEmployee_pps_number(String employee_pps_number) {
		this.employee_pps_number = employee_pps_number;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
