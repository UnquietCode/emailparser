package com.book.controllers;

import java.util.List;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.springframework.beans.factory.annotation.Autowired;

import com.book.dao.LogDAO;
import com.book.model.Log;
import com.book.model.User;

@LoginRequired
public class LogsController {

	public static final int PER_PAGE_LIMIT = 20;

	@Autowired
	private LogDAO logDAO;

	@Get()
	public String list(final Invocation inv,
			@Param("pageId") final long pageId) {
		final User user = (User) inv.getRequest().getSession()
				.getAttribute("loginUser");
		final List<Log> logs = (pageId <= 0) ? this.logDAO.find(
				user.getName(), PER_PAGE_LIMIT) : this.logDAO.find(
				user.getName(), pageId, PER_PAGE_LIMIT);
		inv.addModel("logs", logs);
		return "logs";
	}

	@Get("{pageId}")
	public String pageList(final Invocation inv,
			@Param("pageId") final long pageId,
			@Param("pageAction") final String action) {
		
		inv.getRequest();
		
		System.out.println(pageId);
		System.out.println(action);
		return list(inv, pageId);
	}

	@Post("{id:[0-9]+}/delete")
	public String deleteBook(@Param("id") final long id) {
		this.logDAO.delete(id);
		return "r:/Booklib/logs";
	}
}
