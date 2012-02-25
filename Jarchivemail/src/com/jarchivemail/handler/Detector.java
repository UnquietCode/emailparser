package com.jarchivemail.handler;

import java.io.File;

import com.jarchivemail.utils.FileUtils;

/**
 * 
 * @Description 邮件类型识别器
 * @Author zhangzuoqiang
 * @Date 2012-2-25
 */
public class Detector {

	public static final String TYPE_MSG = MailType.MSG.toString().toLowerCase();

	/**
	 * 识别文件（邮件、附件）类型
	 * 
	 * @param file
	 * @return
	 */
	public static MailType detect(File file) {
		MailType ext = MailType.string2Enum(FileUtils.getExtension(file));
		MailType result;
		switch (ext) {
		case MSG:
			result = MailType.MSG;
			break;
		case EML:
			result = MailType.EML;
			break;
		case MBOX:
			result = MailType.MBOX;
			break;
		case PST:
			result = MailType.PST;
			break;
		default:
			result = MailType.UNKNOWN;
			break;
		}
		return result;
	}

}
