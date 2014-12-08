package com.secto.customadapter;


/**
 * 
 * @author manish.s
 * 
 */

public class Item {
	String imageUrl;
	String title;

	public Item(String imageUrl, String title) {
		super();
		this.imageUrl = imageUrl;
		this.title = title;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
