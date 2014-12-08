package com.secto.model;

public class SiteDto {
	private int site_id;
	private int site_operator_id;
	private int service_provider_id;
	private int site_type_id;
	private String site_name;
	private String site_username;
	private String lng;
	private String lat;

	public int getSite_id() {
		return site_id;
	}

	public void setSite_id(int site_id) {
		this.site_id = site_id;
	}

	public int getSite_operator_id() {
		return site_operator_id;
	}

	public void setSite_operator_id(int site_operator_id) {
		this.site_operator_id = site_operator_id;
	}

	public int getService_provider_id() {
		return service_provider_id;
	}

	public void setService_provider_id(int service_provider_id) {
		this.service_provider_id = service_provider_id;
	}

	public int getSite_type_id() {
		return site_type_id;
	}

	public void setSite_type_id(int site_type_id) {
		this.site_type_id = site_type_id;
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

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

}
