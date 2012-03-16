package com.book.model;

public class Customer {

	private String id;
	// textbox
	private String userName;
	// textarea
	private String address;
	// checkbox
	private String newsletter;
	// radio button
	private boolean sex;
	// dropdown box
	private String country;
	// textarea
	private String commons;
	// hidden value
	private String createTime;

	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public final String getUserName() {
		return userName;
	}

	public final void setUserName(String userName) {
		this.userName = userName;
	}

	public final String getAddress() {
		return address;
	}

	public final void setAddress(String address) {
		this.address = address;
	}

	public final String getNewsletter() {
		return newsletter;
	}

	public final void setNewsletter(String newsletter) {
		this.newsletter = newsletter;
	}

	public final boolean isSex() {
		return sex;
	}

	public final void setSex(boolean sex) {
		this.sex = sex;
	}

	public final String getCountry() {
		return country;
	}

	public final void setCountry(String country) {
		this.country = country;
	}

	public final String getCommons() {
		return commons;
	}

	public final void setCommons(String commons) {
		this.commons = commons;
	}

	public final String getCreateTime() {
		return createTime;
	}

	public final void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}