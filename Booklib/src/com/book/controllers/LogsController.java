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

	public static final int PER_PAGE_LIMIT = 20;

	@Autowired
	private LogDAO logDAO;

	@Get()
	public String list(final Invocation inv, @Param("pageId") final long pageId) {
		final User user = (User) inv.getRequest().getSession()
				.getAttribute("loginUser");
		final List<Log> logs = (pageId <= 0) ? this.logDAO.find(user.getName(),
				PER_PAGE_LIMIT) : this.logDAO.find(user.getName(), pageId,
				PER_PAGE_LIMIT);

		// 从request对象里得到页码信息，如果为空或不是数值都返回1
		int pageindex = WebUtil.getIntByRequestParament(inv.getRequest(),
				"pageindex", 1);
		// 构造一个page对象，第1个参数是当前页，第2个参数是该页最大记录数，第3个是页码上的连接地址
		Page page = new Page(pageindex, 20, "logs");
		page.setTotalCount(this.logDAO.rows(user.getName()));
		// 出来后的page对象已经有了总记录数了，自然就有了页码信息
		inv.addModel("page", page);
		
		inv.addModel("logs", logs);
		return "logs";
	}

	@Post("{id:[0-9]+}/delete")
	public String deleteBook(@Param("id") final long id) {
		this.logDAO.delete(id);
		return "r:/Booklib/logs";
	}
}
