package org.owlmail.utils;

import java.io.File;

import org.owlmail.global.MailType;

public class FileUtil {

	public static final String[] extensions = { "msg", "eml", "mbox", "pst" };

	/**
	 * 转换enum为String
	 * 
	 * @param type
	 * @return
	 */
	public static String enum2Str(MailType type) {
		if (type != null) {
			return type.toString().toUpperCase();
		}
		return null;
	}

	/**
	 * 检查指定文件后缀名
	 * 
	 * @param file
	 * @param type
	 * @return
	 */
	public static boolean isExtendsequal(File file, MailType type) {
		if (null == type) {
			return false;
		}
		String aType = enum2Str(type);
		if (aType.equals(file
				.getAbsolutePath()
				.substring(file.getAbsolutePath().lastIndexOf(".") + 1,
						file.getAbsolutePath().length()).toUpperCase())) {
			return true;
		}
		return false;
	}

	/**
	 * 在指定的文件路径下生成缓存文件
	 * 
	 * @param file
	 * @param searchFolder
	 * @param cacheFolder
	 * @return
	 */
	public static File generateCacheFile(File file, String searchFolder,
			String cacheFolder) {
		String fileName = file.getAbsolutePath().substring(
				file.getAbsolutePath().lastIndexOf("\\") + 1,
				file.getAbsolutePath().lastIndexOf("."))
				+ ".cache";
		return new File(searchFolder + "\\" + cacheFolder + "\\" + fileName);
	}
}
