package com.jarchivemail.parser;

/**
 * 
 * @Description 邮件附件解析接口
 * @Author zhangzuoqiang
 * @Date 2012-2-25
 */
public interface IAttachment {

	public int saveAttachments(int index, int[] attachments, String directory);

	public String[] getAttachmentFileNames(int index);
}
