package com.book.controllers;

import java.util.List;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;

import org.springframework.beans.factory.annotation.Autowired;

import com.book.dao.LogDAO;
import com.book.model.Log;

@LoginRequired
public class LogsController {

	public static final int PER_PAGE_LIMIT = 20;

	@Autowired
	private LogDAO logDAO;

	@Get("")
	public String list(final Invocation inv,
			@Param("byLogId") final long byLogId) {
		final List<Log> logs = (byLogId <= 0) ? this.logDAO
				.find(PER_PAGE_LIMIT) : this.logDAO.find(byLogId,
				PER_PAGE_LIMIT);
		inv.addModel("logs", logs);
		return "logs";
	}
}
