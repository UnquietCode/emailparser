package com.jarchivemail.handler;


/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-25
 */
public interface IHandler {

	/**
	 * 根据Detector识别到的文件类型自动转换到相应的处理器
	 * 
	 * @param type
	 */
	public void autoDetectHandler(MailType type);
}
