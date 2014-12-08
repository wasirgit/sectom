package com.secto.model;

public class JobStatusDto {
	private int job_status_id;
	private int job_type_id;
	private int tag_id;
	private String job_complete_date;

	public int getJob_status_id() {
		return job_status_id;
	}

	public void setJob_status_id(int job_status_id) {
		this.job_status_id = job_status_id;
	}

	public int getJob_type_id() {
		return job_type_id;
	}

	public void setJob_type_id(int job_type_id) {
		this.job_type_id = job_type_id;
	}

	public int getTag_id() {
		return tag_id;
	}

	public void setTag_id(int tag_id) {
		this.tag_id = tag_id;
	}

	public String getJob_complete_date() {
		return job_complete_date;
	}

	public void setJob_complete_date(String job_complete_date) {
		this.job_complete_date = job_complete_date;
	}

}
