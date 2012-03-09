package com.book.model;

/**
 * @do 简单的POJO，与数据库一一对应，但是字段很多的话，建立这么一个POJO本身就是一件很麻烦的事情。
 * @Modify
 * @author zhangzuoqiang
 */
public class Book {

	private String id;
	private String name;
	private String price;
	private String author;
	private String createTime;

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getPrice() {
		return this.price;
	}

	public void setPrice(final String price) {
		this.price = price;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(final String author) {
		this.author = author;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(final String createTime) {
		this.createTime = createTime;
	}

}
