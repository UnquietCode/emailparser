package com.book.controllers;

import java.util.List;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.rest.Get;

import org.springframework.beans.factory.annotation.Autowired;

import com.book.dao.LogDAO;
import com.book.model.Log;
import com.book.model.User;

@LoginRequired
public class LogsController {

	@Autowired
	private LogDAO logDAO;

	@Get()
	public String list(final Invocation inv) {
		final User user = (User) inv.getRequest().getSession()
				.getAttribute("loginUser");
		final List<Log> logs = this.logDAO.find(user.getName());
		inv.addModel("logs", logs);
		return "logs";
	}
}
