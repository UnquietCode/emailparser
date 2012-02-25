package com.jarchivemail.parser;

/**
 * 
 * @Description 邮件体解析接口
 * @Author zhangzuoqiang
 * @Date 2012-2-25
 */
public interface IBody {

	public String[] getContentTypes();
	
	public String[] getContentEncodings();
	
	public String[] getContentFileNames();
}
