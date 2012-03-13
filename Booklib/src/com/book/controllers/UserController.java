package com.book.controllers;

import java.util.List;

import javax.servlet.http.Cookie;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.book.dao.UserDAO;
import com.book.model.Page;
import com.book.model.User;
import com.book.util.Utils;
import com.book.util.WebUtil;

@LoginRequired
public class UserController {

	private static final int PER_PAGE_LIMIT = 10;

	@Autowired
	private UserDAO userDAO;

	@Get("")
	public String list(final Invocation inv) {
		int pageIndex = WebUtil.getIntByRequestParament(inv, "pageIndex", 1);
		// (当前页码-1)*页面容量 pageIndex, limit
		int preLimit = (pageIndex - 1) * PER_PAGE_LIMIT;

		final List<User> users = this.userDAO.find(preLimit, PER_PAGE_LIMIT);
		// 构造一个page对象，第1个参数是当前页，第2个参数是该页最大记录数，第3个是页码上的连接地址
		Page page = new Page(pageIndex, PER_PAGE_LIMIT, "user");
		page.setTotalCount(this.userDAO.rows());
		// 出来后的page对象已经有了总记录数了，自然就有了页码信息
		inv.addModel("page", page);
		inv.addModel("users", users);
		return "all_users";
	}

	@Get("add")
	public String showAdd() {
		return "add_user";
	}

	@Post("add")
	public String add(final Invocation inv,
			@Param("password2") final String password2, final User user) {
		if (StringUtils.isEmpty(user.getLoginName())
				|| StringUtils.isEmpty(user.getPassword())
				|| StringUtils.isEmpty(user.getName())) {
			inv.addModel("error", "不能为空");
			return "add_user";
		}
		final User userFromDB = this.userDAO
				.getByLoginName(user.getLoginName());
		if (userFromDB != null) {
			inv.addModel("error", "用户名已存在");
			return "add_user";
		}
		if (!password2.equals(user.getPassword())) {
			inv.addModel("error", "两次输入的密码不一致");
			return "add_user";
		}
		this.userDAO.save(user);
		return "r:/Booklib/user";
	}

	/**
	 * 个人中心
	 * 
	 * @param inv
	 * @return
	 */
	@Get("/my")
	public String showMy(final Invocation inv) {
		final User user = (User) inv.getRequest().getSession()
				.getAttribute("loginUser");
		inv.addModel("user", user);
		return "one_user";
	}

	@Get("{id}")
	public String show(final Invocation inv, @Param("id") final String id,
			@Param("edit") final boolean edit) {
		final User user = this.userDAO.get(Long.parseLong(id));
		inv.addModel("user", user);
		if (edit) {
			return "one_user_edit";
		} else {
			return "one_user";
		}
	}

	@Post("{id}/info/update")
	public String updateUser(final Invocation inv,
			@Param("id") final String id, final User user) {
		final User userFromDB = this.userDAO.get(Long.parseLong(id));
		// 服务器端验证字符的合法性
		if (StringUtils.isEmpty(user.getName())) {
			inv.addModel("user", userFromDB);
			inv.addModel("error", "任何一项都不能为空！");
			return "one_user_edit";
		}
		Utils.updateModel(userFromDB, user);
		this.userDAO.update(userFromDB);
		return "r:/Booklib/user/" + id;
	}

	@Post("{id}/password/update")
	public String updatePassword(final Invocation inv,
			@Param("id") final String id,
			@Param("old_password") final String oldPassword,
			@Param("password") final String password,
			@Param("password2") final String password2) {
		final User userFromDB = this.userDAO.get(Long.parseLong(id));
		inv.addModel("user", userFromDB);
		// 服务器端验证字符的合法性
		if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(password)
				|| StringUtils.isEmpty(password2)) {
			inv.addModel("error", "任何一项都不能为空！");
			return "one_user_edit";
		}
		if (!password.equals(password2)) {
			inv.addModel("error", "两次输入的密码不相等！");
			return "one_user_edit";
		}
		final User user = this.userDAO.get(Long.parseLong(id));
		if (!user.getPassword().equals(oldPassword)) {
			inv.addModel("error", "原密码错误！");
			return "one_user_edit";
		}
		user.setPassword(password);
		this.userDAO.update(user);
		return "r:/Booklib/user/" + id;
	}

	@Post("{id}/delete")
	@AdminRequired
	public String delete(final Invocation inv, @Param("id") final String id) {
		this.userDAO.delete(Long.parseLong(id));
		final User user = (User) inv.getRequest().getSession()
				.getAttribute("loginUser");
		if (id.equals(user.getId())) {
			return logout(inv);
		}
		return "r:/Booklib/user";
	}

	@Get("logout")
	public String logout(final Invocation inv) {
		final User user = (User) inv.getRequest().getSession()
				.getAttribute("loginUser");
		final Cookie[] cookies = inv.getRequest().getCookies();
		for (final Cookie cookie : cookies) {
			if (cookie.getName().equals("user")) {
				final String id = cookie.getValue();
				if (user.getId().equals(id)) {
					final Cookie c = new Cookie(cookie.getName(), null);
					c.setPath("/");
					c.setMaxAge(0);
					inv.getResponse().addCookie(c);
					break;
				}
			}
		}
		inv.getRequest().getSession().removeAttribute("loginUser");
		inv.getRequest().getSession().invalidate();
		return "r:/Booklib";
	}
}
