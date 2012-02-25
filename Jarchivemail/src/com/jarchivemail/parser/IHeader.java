package com.jarchivemail.parser;

import java.util.Date;

/**
 * 
 * @Description 邮件头解析接口
 * @Author zhangzuoqiang
 * @Date 2012-2-25
 */
public interface IHeader {

	public String getSender();

	public String getReceiver();

	public String getSubject();

	public Date getReceiveDate();

	public Date getSendDate();
}
