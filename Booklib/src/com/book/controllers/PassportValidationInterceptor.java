package com.book.controllers;

import java.lang.annotation.Annotation;

import javax.servlet.http.Cookie;

import net.paoding.rose.web.ControllerInterceptorAdapter;
import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Interceptor;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.book.dao.UserDAO;
import com.book.model.User;

/**
 * 
 * @author zhangzuoqiang
 * @Email z.zuoqiang@gmail.com
 * @time 2012-3-9  上午11:40:22
 * @do 自动登录的实现
 * @Modify
 */
@Interceptor(oncePerRequest = true)
public class PassportValidationInterceptor extends ControllerInterceptorAdapter {

	@Autowired
	private UserDAO userDAO;

	public PassportValidationInterceptor() {
		// 设置优先级，优先级越高的拦截器，before方法越先执行
		// PassportInterceptor要比很多拦截器都要先执行，其中包括LoginRequredInterceptor
		this.setPriority(1000);
	}

	@Override
	protected Class<? extends Annotation> getDenyAnnotationClass() {
		return IgnorePassportValidation.class;
	}

	// before方法在调用控制器方法前执行，相反的after则是控制器执行后才执行
	@Override
	protected Object before(final Invocation inv) throws Exception {
		final User loginUser = (User) inv.getRequest().getSession()
				.getAttribute("loginUser");
		if (loginUser != null) {
			// 既然都已经登录过了，那就算了。
			return true;
		}
		final Cookie[] cookies = inv.getRequest().getCookies();
		if (cookies == null) {
			return true;
		}
		for (final Cookie cookie : cookies) {
			if (cookie.getName().equals("user")) {
				final String id = cookie.getValue();
				if (StringUtils.isEmpty(id)) {
					return true;
				}
				final User user = this.userDAO.get(Long.parseLong(id));
				LoginController.loginAction(inv, user);
				return true;
			}
		}
		return true;
	}
}
