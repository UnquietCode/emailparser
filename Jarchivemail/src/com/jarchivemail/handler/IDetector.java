package com.jarchivemail.handler;

import java.io.InputStream;
import java.io.Serializable;

import com.jarchivemail.metadata.MailType;
import com.jarchivemail.metadata.Metadata;

/**
 * 
 * @Description 邮件类型识别器
 * @Author zhangzuoqiang
 * @Date 2012-2-25
 */
public interface IDetector extends Serializable {

	public MailType detect(InputStream input, Metadata metadata);

}
