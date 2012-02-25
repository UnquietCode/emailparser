package com.jarchivemail.handler;


/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-25
 */
public interface IHandler {

	/**
	 * 处理器, 返回可用于下一步解析的对象
	 * 
	 * @param emailFile
	 */
	public LineReader handle();
}
