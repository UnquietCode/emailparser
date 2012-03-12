package com.book.model;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Page implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Boolean hasPrePage;
	private Boolean hasNextPage;
	private Long everyPage = Long.valueOf(10L);
	private Long totalPage;
	private Long currentPage = Long.valueOf(1L);
	private Long beginIndex;
	private Long endinIndex;
	private Long totalCount;
	private String querySql;
	private String initQuerySql;
	private String sortName;
	private String sortState;
	private String sortInfo;
	private Boolean sort;
	private String defaultInfo = "&nbsp;&nbsp;";

	public String getDefaultInfo() {
		return this.defaultInfo;
	}

	public void setDefaultInfo(String defaultInfo) {
		this.defaultInfo = defaultInfo;
	}

	public String getSortInfo() {
		return this.sortInfo;
	}

	public void setSortInfo(String sortInfo) {
		this.sortInfo = sortInfo;
	}

	public String getSortName() {
		return this.sortName;
	}

	public void setSortName(String sortName) {
		setPageSortState(sortName);
	}

	public String getSortState() {
		return this.sortState;
	}

	public void setSortState(String sortState) {
		this.sortState = sortState;
	}

	public String getQuerySql() {
		return this.querySql;
	}

	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}

	public Page() {
	}

	public Page(Long totalRecords) {
		this.totalCount = totalRecords;
		setTotalPage(getTotalPage(totalRecords));
	}

	public Page(Long everyPage, Long totalRecords) {
		this.everyPage = everyPage;
		this.totalCount = totalRecords;
		setTotalPage(getTotalPage(totalRecords));
	}

	public void pageState(int index, String value) {
		this.sort = Boolean.valueOf(false);
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
		case 5:
			this.sort = Boolean.valueOf(true);
			sort(value);
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

	private void sort(String sortName) {
		setPageSortState(sortName);
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

	private void setPageSortState(String newPageSortName) {
		if (StringUtils.isEmpty(this.sortName)) {
			this.sortState = "asc";
			this.sortInfo = "↑";
		} else if (StringUtils.equalsIgnoreCase(newPageSortName, this.sortName)) {
			if (StringUtils.equalsIgnoreCase(this.sortState, "asc")) {
				this.sortState = "desc";
				this.sortInfo = "↓";
			} else {
				this.sortState = "asc";
				this.sortInfo = "↑";
			}
		} else {
			this.sortState = "asc";
			this.sortInfo = "↑";
		}

		this.sortName = newPageSortName.toLowerCase();
	}

	public Boolean isSort() {
		return this.sort;
	}

	public void setSort(Boolean sort) {
		this.sort = sort;
	}

	public String getInitQuerySql() {
		return this.initQuerySql;
	}

	public void setInitQuerySql(String initQuerySql) {
		this.initQuerySql = initQuerySql;
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