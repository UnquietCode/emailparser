package com.jarchivemail.handler;

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
}