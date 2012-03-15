package com.book.controllers;

import java.util.List;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.springframework.beans.factory.annotation.Autowired;

import com.book.dao.LogDAO;
import com.book.model.Log;
import com.book.model.Page;
import com.book.model.User;
import com.book.util.WebUtil;

@LoginRequired
public class LogsController {

	private static final int PER_PAGE_LIMIT = 20;

	@Autowired
	private LogDAO logDAO;

	@Get()
	public String list(final Invocation inv) {

		final User user = (User) inv.getRequest().getSession()
				.getAttribute("loginUser");

		int pageIndex = WebUtil.getIntByRequestParament(inv, "pageIndex", 1);
		// (当前页码-1)*页面容量 pageIndex, limit
		int preLimit = (pageIndex - 1) * PER_PAGE_LIMIT;

		final List<Log> logs = this.logDAO.find(user.getName(), preLimit,
				PER_PAGE_LIMIT);

		// 构造一个page对象，第1个参数是当前页，第2个参数是该页最大记录数，第3个是页码上的连接地址
		Page page = new Page(pageIndex, PER_PAGE_LIMIT, "logs");
		page.setTotalCount(this.logDAO.rows(user.getName()));
		// 出来后的page对象已经有了总记录数了，自然就有了页码信息
		inv.addModel("page", page);
		inv.addModel("logs", logs);
		return "logs";
	}

	@Post("{id:[0-9]+}/delete")
	public String deleteBook(final Invocation inv, @Param("id") final long id,
			@Param("pageIndex") final int pageIndex) {
		this.logDAO.delete(id);
		return "r:/Booklib/logs?pageIndex=" + pageIndex;
	}
}
