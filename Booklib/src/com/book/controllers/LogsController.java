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

	@Get("")
	public String list(final Invocation inv,
			@Param("byLogId") final long byLogId) {
		final User user = (User) inv.getRequest().getSession()
				.getAttribute("loginUser");
		final List<Log> logs = (byLogId <= 0) ? this.logDAO.find(
				user.getName(), PER_PAGE_LIMIT) : this.logDAO.find(
				user.getName(), byLogId, PER_PAGE_LIMIT);
		inv.addModel("logs", logs);
		return "logs";
	}

	@Post("{id:[0-9]+}/delete")
	public String deleteBook(@Param("id") final long id) {
		this.logDAO.delete(id);
		return "r:/Booklib/logs";
	}
}
