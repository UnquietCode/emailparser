package com.book.model;

/**
 * @do 
 * @Modify
 * @author zhangzuoqiang
 */
public class Remark {

	private String id;
	private String userName;
	private String bookId;
	private String essay;
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

	public String getBookId() {
		return this.bookId;
	}

	public void setBookId(final String bookId) {
		this.bookId = bookId;
	}

	public String getEssay() {
		return this.essay;
	}

	public void setEssay(final String essay) {
		this.essay = essay;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(final String createTime) {
		this.createTime = createTime;
	}

}
