package com.book.controllers;

import javax.servlet.http.Cookie;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.book.dao.UserDAO;
import com.book.model.User;

public class LoginController {

	@Autowired
	private UserDAO userDAO;

	@Get("")
	public String show() {
		return "login";
	}

	@Post("")
	public String doLogin(final Invocation inv,
			@Param("loginName") final String loginName,
			@Param("password") final String password) {
		if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(password)) {
			inv.addModel("error", "不能为空！");
			return "login";
		}
		final User user = this.userDAO.getByLoginName(loginName);
		if (user == null) {
			inv.addModel("loginName", loginName);
			inv.addModel("error", "用户名不存在");
			return "login";
		}
		if (!user.getPassword().equals(password)) {
			inv.addModel("loginName", loginName);
			inv.addModel("error", "密码错误");
			return "login";
		}
		loginAction(inv, user);
		return "r:/Booklib/book";
	}

	public final static void loginAction(final Invocation inv, final User user) {
		inv.getRequest().getSession().setAttribute("loginUser", user);
		inv.getRequest().getSession().setMaxInactiveInterval(10 * 60);
		// final Cookie cookie = new Cookie("JSESSIONID",
		// inv.getRequest().getSession().getId());
		final Cookie cookie = new Cookie("user", user.getId());
		cookie.setMaxAge(5 * 60);
		cookie.setPath("/");
		inv.getResponse().addCookie(cookie);
	}
}
