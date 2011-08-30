/**
 * @Package: utils
 * @Title: ReadFile.java
 * @Author: zhangzuoqiang
 * @Time: 5:50:38 PM Aug 29, 2011
 * @Version: 
 */
package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class ReadFile {

	public static String read_file_string(String file_name) {
		File file = new File(file_name);
		FileReader fis = null;
		BufferedReader bis = null;
		String res = "";

		try {
			fis = new FileReader(file);
			bis = new BufferedReader(fis);
			int len = 10;

			char[] buff = new char[(int) Math.max(len, file.length())];

			while (bis.ready()) {
				len = bis.read(buff);
				res = new StringBuilder().append(res).append(new String(buff))
						.toString();
			}
			fis.close();
			bis.close();
		} catch (FileNotFoundException e) {
			System.out.println(new StringBuilder().append("File Not Found: ")
					.append(file_name).toString());
		} catch (IOException e) {
			System.out.println(e);
		}
		return res;
	}

	public static StringBuilder read_file_builder(String file_name) {
		File file = new File(file_name);
		FileReader fis = null;
		BufferedReader bis = null;
		StringBuilder res = new StringBuilder();
		try {
			fis = new FileReader(file);
			bis = new BufferedReader(fis);
			int len = 10;
			char[] buff = new char[(int) Math.max(len, file.length())];
			while (bis.ready()) {
				len = bis.read(buff);
				res.append(buff, 0, len);
			}
			fis.close();
			bis.close();
		} catch (FileNotFoundException e) {
			System.out.println(new StringBuilder().append("File Not Found: ")
					.append(file_name).toString());
		} catch (IOException e) {
			System.out.println(e);
		}
		return res;
	}

	public static String read_file(String file_name) {
		return read_file_string(file_name);
	}

	public static byte[] getBytpesFromFile(String file_name) throws IOException {
		File file = new File(file_name);
		byte[] bytes = null;
		bytes = getBytesFromFile(file);
		return bytes;
	}

	public static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();
		if (length > 2147483647L) {
			;
		}
		byte[] bytes = new byte[(int) length];
		int offset = 0;
		int numRead = 0;

		while ((offset < bytes.length)
				&& ((numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException(new StringBuilder()
					.append("Could not completely read file ")
					.append(file.getName()).toString());
		}
		is.close();
		return bytes;
	}
}