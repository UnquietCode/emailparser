package com.jarchivemail.handler;

import java.io.File;

import com.jarchivemail.handler.mbox.MBOXHandler;
import com.jarchivemail.utils.FileUtils;

/**
 * 
 * @Description 将要解析的邮件的类型
 * @Author zhangzuoqiang
 * @Date 2012-2-25
 */
public enum MailType {

	MSG(1), EML(2), MBOX(3), PST(4), UNKNOWN(5);

	private int type;

	private MailType(int type) {
		this.type = type;
	}

	public static MailType string2Enum(String str) {
		if (str != null) {
			try {
				return Enum.valueOf(MailType.class, str.trim());
			} catch (IllegalArgumentException ex) {
			}
		}
		return null;
	}

	public int getType() {
		return type;
	}

	/**
	 * 识别文件（邮件、附件）类型，并返回相应的处理器
	 * 
	 * @param file
	 * @return
	 */
	public static IHandler autoDetect(File file) {
		MailType ext = MailType.string2Enum(FileUtils.getExtension(file));
		IHandler handler;
		switch (ext) {
		case MSG:
			handler = null;
			break;
		case EML:
			handler = null;
			break;
		case MBOX:
			handler = new MBOXHandler(file);
			break;
		case PST:
			handler = null;
			break;
		default:
			handler = null;
			break;
		}
		return handler;
	}
}