package com.secto.model;

public class WorkListDto {
	private int work_list_id;
	private int week_no;
	private String work_date;
	private int year;

	public int getWork_list_id() {
		return work_list_id;
	}

	public void setWork_list_id(int work_list_id) {
		this.work_list_id = work_list_id;
	}

	public int getWeek_no() {
		return week_no;
	}

	public void setWeek_no(int week_no) {
		this.week_no = week_no;
	}

	public String getWork_date() {
		return work_date;
	}

	public void setWork_date(String work_date) {
		this.work_date = work_date;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

}
