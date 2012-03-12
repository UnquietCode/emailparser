package com.book.util;

import org.apache.commons.lang.StringUtils;

import com.book.model.Page;

public class PageUtil {

	public static final String ASC = "asc";
	public static final String DESC = "desc";
	public static final String PAGE_DESC = "↓";
	public static final String PAGE_ASC = "↑";
	public static final String PAGE_NULL = "&nbsp;&nbsp;";
	public static final String SESSION_PAGE_KEY = "page";

	public static String getPageQuerySql(String querySql, Long beginIndex,
			Long endinIndex) {
		if ((querySql.indexOf("where rn>") != -1)
				&& (querySql.indexOf("and rn<=") != -1)) {
			return querySql.toUpperCase();
		}
		return (querySql + " where rn>" + beginIndex + " and rn<=" + endinIndex)
				.toUpperCase();
	}

	public static String createQuerySql(String querySql, String orderByName) {
		StringBuilder sql = new StringBuilder();
		sql.append("select ttt.* from(select tt.*,rownum rn from(");
		sql.append(querySql);
		if (StringUtils.isNotEmpty(orderByName)) {
			sql.append(" order by ").append(orderByName);
		}
		sql.append(" )tt)ttt ");
		return sql.toString();
	}

	public static String getSortDescOrAsc(String querySql) {
		querySql = querySql.toUpperCase();
		String temp = "ORDER BY";
		int orderIndex = querySql.lastIndexOf(temp);
		String newsql = querySql.substring(orderIndex);
		String temp2 = ")";
		int lastIndex = newsql.indexOf(temp2);
		String orderByName = newsql.substring(temp.length(), lastIndex).trim();
		return orderByName;
	}

	public static Page inintPage(String initPageSql, Long totalCount,
			Integer index, String value, Page sessionPage) {
		Page page = null;
		if (index.intValue() < 0) {
			page = new Page(totalCount);
		} else {
			Long everPage = Long.valueOf(value == null ? 10L : Long
					.parseLong(value));
			page = sessionPage;
			page.setEveryPage(everPage);
			page.setTotalCount(totalCount);
		}
		page.setInitQuerySql(initPageSql);
		String querySql = getPageQuerySql(initPageSql, page.getBeginIndex(),
				page.getEndinIndex());
		page.setQuerySql(querySql);
		return page;
	}

	public static Page execPage(int index, String value, Page sessionPage) {
		Page page = sessionPage;
		page.pageState(index, value);

		String initPageSql = page.getInitQuerySql();
		String querySql = getPageQuerySql(initPageSql, page.getBeginIndex(),
				page.getEndinIndex());

		if (page.isSort().booleanValue()) {
			String sortName = page.getSortName();
			if (sortName != null) {
				String descAsc = page.getSortState();
				String sortNameDescAsc = (" " + sortName + " " + descAsc)
						.toUpperCase();
				String getOldSortName = getSortDescOrAsc(querySql);
				querySql = querySql.replace(getOldSortName, sortNameDescAsc);
			}
		}
		page.setQuerySql(querySql);
		return page;
	}
}