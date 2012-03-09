package com.book.model;

/**
 * @do 
 * @Modify
 * @author zhangzuoqiang
 */
public class User {

	private String id;
	private String loginName;
	private String password;
	private String name;
	private String groups;
	private String createTime;

	public String getGroups() {
		return this.groups;
	}

	public void setGroups(final String groups) {
		this.groups = groups;
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(final String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(final String createTime) {
		this.createTime = createTime;
	}

}
