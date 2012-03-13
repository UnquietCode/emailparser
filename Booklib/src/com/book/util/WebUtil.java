package com.book.util;

import javax.servlet.http.HttpServletRequest;

public class WebUtil {

	// 根据指定参数在Request对象得到整数值，如果为空或出错则返回给定的默认值
	public static int getIntByRequestParament(HttpServletRequest request,
			String param, int defaultvalue) {
		try {
			return Integer.parseInt(request.getParameter(param));
		} catch (Exception e) {
			return defaultvalue;
		}
	}
}