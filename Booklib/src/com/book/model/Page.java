package com.book.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Page implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Log> logs;

	private Boolean hasPrePage;
	private Boolean hasNextPage;
	private Long everyPage = Long.valueOf(10L);
	private Long totalPage;
	private Long currentPage = Long.valueOf(1L);
	private Long beginIndex;
	private Long endinIndex;
	private Long totalCount;

	public Page() {
	}

	public Page(List<Log> logs, Long totalRecords) {
		this.logs = logs;
		this.totalCount = totalRecords;
		setTotalPage(getTotalPage(totalRecords));
	}

	public Page(List<Log> logs, Long everyPage, Long totalRecords) {
		this.logs = logs;
		this.everyPage = everyPage;
		this.totalCount = totalRecords;
		setTotalPage(getTotalPage(totalRecords));
	}

	public List<Log> getLogs() {
		return logs;
	}

	public void setLogs(List<Log> logs) {
		this.logs = logs;
	}

	public void pageState(int index, String value) {
		switch (index) {
		case 0:
			setEveryPage(Long.valueOf(Long.parseLong(value)));
			break;
		case 1:
			first();
			break;
		case 2:
			previous();
			break;
		case 3:
			next();
			break;
		case 4:
			last();
			break;
		case 6:
			setCurrentPage(Long.valueOf(Long.parseLong(value)));
		}
	}

	private void first() {
		this.currentPage = Long.valueOf(1L);
	}

	private void previous() {
		this.currentPage = Long.valueOf(this.currentPage.longValue() - 1L);
	}

	private void next() {
		this.currentPage = Long.valueOf(this.currentPage.longValue() + 1L);
	}

	private void last() {
		this.currentPage = this.totalPage;
	}

	private Long getTotalPage(Long totalRecords) {
		Long totalPage = Long.valueOf(0L);
		if (totalRecords.longValue() % this.everyPage.longValue() == 0L)
			totalPage = Long.valueOf(totalRecords.longValue()
					/ this.everyPage.longValue());
		else {
			totalPage = Long.valueOf(totalRecords.longValue()
					/ this.everyPage.longValue() + 1L);
		}
		return totalPage;
	}

	public Long getBeginIndex() {
		this.beginIndex = Long.valueOf((this.currentPage.longValue() - 1L)
				* this.everyPage.longValue());
		return this.beginIndex;
	}

	public void setBeginIndex(Long beginIndex) {
		this.beginIndex = beginIndex;
	}

	public Long getCurrentPage() {
		this.currentPage = Long.valueOf(this.currentPage.longValue() == 0L ? 1L
				: this.currentPage.longValue());
		return this.currentPage;
	}

	public void setCurrentPage(Long currentPage) {
		if (0L == currentPage.longValue()) {
			currentPage = Long.valueOf(1L);
		}
		this.currentPage = currentPage;
	}

	public Long getEveryPage() {
		this.everyPage = Long.valueOf(this.everyPage.longValue() == 0L ? 10L
				: this.everyPage.longValue());
		return this.everyPage;
	}

	public void setEveryPage(Long everyPage) {
		this.everyPage = everyPage;
	}

	public Boolean getHasNextPage() {
		this.hasNextPage = Boolean.valueOf((this.currentPage != this.totalPage)
				&& (this.totalPage.longValue() != 0L));
		return this.hasNextPage;
	}

	public void setHasNextPage(Boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}

	public Boolean getHasPrePage() {
		this.hasPrePage = Boolean.valueOf(this.currentPage.longValue() != 1L);
		return this.hasPrePage;
	}

	public void setHasPrePage(Boolean hasPrePage) {
		this.hasPrePage = hasPrePage;
	}

	public Long getTotalPage() {
		return this.totalPage;
	}

	public void setTotalPage(Long totalPage) {
		if (this.currentPage.longValue() > totalPage.longValue()) {
			this.currentPage = totalPage;
		}
		this.totalPage = totalPage;
	}

	public Long getTotalCount() {
		return this.totalCount;
	}

	public void setTotalCount(Long totalCount) {
		setTotalPage(getTotalPage(totalCount));
		this.totalCount = totalCount;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Long getEndinIndex() {
		this.endinIndex = Long.valueOf(this.currentPage.longValue()
				* this.everyPage.longValue());
		return this.endinIndex;
	}

	public void setEndinIndex(Long endinIndex) {
		this.endinIndex = endinIndex;
	}
}
