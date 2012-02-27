package org.owlmail.global;

import java.io.File;

public enum MailType {

	MSG(1), EML(2), MBOX(3), PST(4), UNKNOWN(5);

	private int type;

	private MailType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	/**
	 * 获取邮件类型，并将类型转换为enum
	 * 
	 * @param file
	 * @return
	 */
	public static MailType getFileType(File file) {
		if (null == file) {
			return null;
		}
		String exts = file
				.getAbsolutePath()
				.substring(file.getAbsolutePath().lastIndexOf(".") + 1,
						file.getAbsolutePath().length()).toUpperCase();
		return str2Enum(exts);
	}

	/**
	 * 转换String为enum
	 * 
	 * @param str
	 * @return
	 */
	public static MailType str2Enum(String str) {
		if (str != null) {
			try {
				return Enum.valueOf(MailType.class, str.trim().toUpperCase());
			} catch (IllegalArgumentException ex) {
			}
		}
		return null;
	}

}
