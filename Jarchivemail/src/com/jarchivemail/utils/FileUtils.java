package com.jarchivemail.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-25
 */
public class FileUtils {

	/**
	 * String --> InputStream
	 * 
	 * @param str
	 * @return
	 */
	public static InputStream String2InputStream(String str) {
		if (null == str) {
			return null;
		}
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}

	/**
	 * InputStream --> String
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream is) {
		if (null == is) {
			return null;
		}

		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		try {
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	/**
	 * File --> InputStream
	 * 
	 * @param file
	 * @return
	 */
	public static InputStream file2InputStream(File file) {
		InputStream result = null;
		try {
			result = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * InputStream --> File
	 * 
	 * @param ins
	 * @param file
	 */
	public void inputstreamtofile(InputStream ins, File file) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getExtension(File file) {
		if (null == file) {
			return "unknown";
		}
		return getExtension(file.getName());
	}

	public static String getExtension(String fileName) {
		if (null == fileName || "".equals(fileName)) {
			return "unknown";
		}
		int dot = fileName.lastIndexOf(".");
		if (dot < 0) {
			return "unknown";
		}
		String extension = fileName.substring(dot + 1);
		if (extension.length() > 10) {
			return "unknown";
		}
		return extension;
	}
}