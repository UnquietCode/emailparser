package com.book.model;

/**
 * @do
 * @Modify
 * @author zhangzuoqiang
 */
public class Log {

	private String id;
	private String userName;
	private String ip;
	private String resourcePattern;
	private String resourceId;
	private boolean success;
	private String remarks;
	private String createTime;

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(final String userName) {
		this.userName = userName;
	}

	public String getResourcePattern() {
		return this.resourcePattern;
	}

	public void setResourcePattern(final String resourcePattern) {
		this.resourcePattern = resourcePattern;
	}

	public String getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(final String resourceId) {
		this.resourceId = resourceId;
	}

	public boolean isSuccess() {
		return this.success;
	}

	public void setSuccess(final boolean success) {
		this.success = success;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(final String remarks) {
		this.remarks = remarks;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(final String createTime) {
		this.createTime = createTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
