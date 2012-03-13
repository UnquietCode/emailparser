package com.book.util;

import net.paoding.rose.web.Invocation;

public class WebUtil {

	// 根据指定参数在Request对象得到整数值，如果为空或出错则返回给定的默认值
	public static int getIntByRequestParament(final Invocation inv,
			String param, int defaultvalue) {
		try {
			return Integer.parseInt(inv.getRequest().getParameter(param));
		} catch (Exception e) {
			return defaultvalue;
		}
	}
}