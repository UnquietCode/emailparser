/**
 * @Package: utils
 * @Title: StringUtils.java
 * @Author: zhangzuoqiang
 * @Time: 5:33:55 PM Aug 29, 2011
 * @Version: 
 */
package utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class StringUtils {

	private static int defaultAutoLineLength = 40;

	/**
	 * 检查指定字符串中是否包含指定字符
	 * 
	 * @param c
	 * @param what
	 * @return
	 */
	static boolean contains(char c, String what) {
		return what.indexOf(c) >= 0;
	}

	/**
	 * 
	 * @param s
	 * @param what
	 * @param pos
	 * @return
	 */
	static int skip_char(StringBuilder s, String what, int pos) {
		while (pos <= s.length() - 1) {
			char c = s.charAt(pos);
			if (!contains(c, what))
				break;
			pos++;
		}
		return pos;
	}

	/**
	 * 
	 * @param s
	 * @param what
	 * @param pos
	 * @return
	 */
	static int skip_char_reverse(StringBuilder s, String what, int pos) {
		while (pos > 0) {
			char c = s.charAt(pos);
			if (!contains(c, what))
				break;
			pos--;
		}
		return pos;
	}

	/**
	 * 
	 * @param s
	 * @param pos
	 * @return
	 */
	public static int skip_spaces_reverse(StringBuilder s, int pos) {
		return skip_char_reverse(s, " \t\n\r", pos);
	}

	/**
	 * 
	 * @param s
	 * @param pos
	 * @return
	 */
	public static int skip_spaces(StringBuilder s, int pos) {
		return skip_char(s, " \t\n\r", pos);
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public static boolean is_space(char c) {
		return (c == ' ') || (c == '\t') || (c == '\n') || (c == '\r');
	}

	/**
	 * 
	 * @param s
	 * @param c
	 * @return
	 */
	public static List<String> split_str(StringBuilder s, String c) {
		List<String> res = new ArrayList<String>();
		int start = 0;
		do {
			int pos = s.indexOf(c, start);
			if (pos < 0) {
				res.add(s.substring(start));
				break;
			}
			res.add(s.substring(start, pos));
			start = pos + 1;
		} while (start > 0);
		return res;
	}

	/**
	 * 
	 * @param s
	 * @param what
	 * @return
	 */
	public static String strip(StringBuilder s, String what) {
		int start = skip_char(s, what, 0);
		int end = skip_char_reverse(s, what, s.length() - 1);
		if (start > end) {
			return s.substring(start);
		}
		return s.substring(start, end + 1);
	}

	/**
	 * 
	 * @param s
	 * @param what
	 * @return
	 */
	public static String strip_post(StringBuilder s, String what) {
		int end = skip_char_reverse(s, what, s.length() - 1);
		return s.substring(0, end + 1);
	}

	/**
	 * 
	 * @param str
	 * @param what
	 * @return
	 */
	public static String strip_post(String str, String what) {
		if (str == null) {
			throw new NullPointerException();
		}
		StringBuilder s = new StringBuilder();
		s.append(str);
		return strip_post(s, what);
	}

	/**
	 * 
	 * @param s
	 * @param what
	 * @return
	 */
	public static String strip(String s, String what) {
		return strip(new StringBuilder(s), what);
	}

	/**
	 * 
	 * @param length
	 */
	public static void set_defaultAutoLineLenght(int length) {
		defaultAutoLineLength = length;
	}

	/**
	 * 
	 * @return
	 */
	public static int get_defaultAutoLineLenght() {
		return defaultAutoLineLength;
	}

	/**
	 * 
	 * @param what
	 * @return
	 */
	public static String autoLineBreak(String what) {
		return autoLineBreak(what, defaultAutoLineLength);
	}

	/**
	 * 
	 * @param what
	 * @param length
	 * @return
	 */
	public static String autoLineBreak(StringBuilder what, int length) {
		return autoLineBreak(what.toString(), length);
	}

	/**
	 * 
	 * @param what
	 * @return
	 */
	public static String autoLineBreak(StringBuilder what) {
		return autoLineBreak(what.toString(), defaultAutoLineLength);
	}

	/**
	 * 
	 * @param what
	 * @param length
	 * @return
	 */
	public static String autoLineBreak(String what, int length) {
		String[] res = autoLineBreak_int(what, length).split("\n");
		StringBuilder stripped_string = new StringBuilder();
		for (String line : res) {
			if (stripped_string.length() > 0) {
				stripped_string.append('\n');
			}
			stripped_string.append(line.trim());
		}
		return stripped_string.toString();
	}

	private static String autoLineBreak_int(String what, int length) {
		char[] myPreferedSigns = { ';', '.', ',', '!', '?', '>', '-' };
		char[] mySpaceSigns = { ' ', '\t' };
		if ((length < 10) || (length >= what.length()) || (10 >= what.length())) {
			return what;
		}
		char[] in = what.toCharArray();
		StringBuilder str = new StringBuilder();
		for (int walker = 1; walker < in.length; walker++) {
			if (in.length <= 10) {
				break;
			}
			if (walker % length != 0) {
				continue;
			}
			boolean found = false;
			for (int signidx = 0; signidx < myPreferedSigns.length; signidx++) {
				for (int index = 1; index <= 10; index++) {
					if ((walker + index + 1 >= in.length)
							|| (walker - index <= 0)) {
						break;
					}
					if (in[(walker + index)] == myPreferedSigns[signidx]) {
						str.append(new String(in, 0, walker + index + 1));
						str.append("\n");
						walker++;
						for (int i = 0; i < mySpaceSigns.length; i++) {
							if (in[(walker + 1)] == mySpaceSigns[i]) {
								walker++;
							}
						}
						String rest = new String(in, walker + index, in.length
								- (walker + index));
						in = rest.toCharArray();
						walker = 0;
						found = true;
						break;
					}
					if (in[(walker - index)] == myPreferedSigns[signidx]) {
						str.append(new String(in, 0, walker - index + 1));
						str.append("\n");
						walker++;
						for (int i = 0; i < mySpaceSigns.length; i++) {
							if (in[(walker + 1)] == mySpaceSigns[i]) {
								walker++;
							}
						}
						String rest = new String(in, walker - index, in.length
								- (walker - index));
						in = rest.toCharArray();
						walker = 0;
						found = true;
						break;
					}
				}

				if (found) {
					break;
				}
			}
			for (int signidx = 0; signidx < mySpaceSigns.length; signidx++) {
				for (int index = 1; index <= 25; index++) {
					if ((walker + index + 1 >= in.length)
							|| (walker - index <= 0)) {
						break;
					}
					if (in[(walker + index)] == mySpaceSigns[signidx]) {
						str.append(new String(in, 0, walker + index));
						str.append("\n");
						walker++;
						String rest = new String(in, walker + index, in.length
								- (walker + index));
						in = rest.toCharArray();
						walker = 0;
						found = true;
						break;
					}
					if (in[(walker - index)] != mySpaceSigns[signidx])
						continue;
					str.append(new String(in, 0, walker - index + 1));
					str.append("\n");
					walker++;
					String rest = new String(in, walker - index, in.length
							- (walker - index));
					in = rest.toCharArray();
					walker = 0;
					found = true;
					break;
				}
				if (found) {
					break;
				}
			}
		}
		str.append(in);
		return str.toString();
	}

	public static String formatDouble(double d) {
		String s = String.format("%f", new Object[] { Double.valueOf(d) });
		s = strip_post(s, "0");
		s = strip_post(s, ".");
		s = strip_post(s, ",");
		return s;
	}

	public static String exceptionToString(Exception ex) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintStream s = new PrintStream(bos);
		ex.printStackTrace(s);
		s.flush();
		return bos.toString();
	}

	public static String skipLeadingLines(String sourceString, int lines) {
		if (lines <= 0) {
			return sourceString;
		}
		int lbCounter = 0;
		int idx = 0;
		char[] arr = sourceString.toCharArray();
		for (idx = 0; (idx < arr.length) && (lbCounter != lines); idx++) {
			if (arr[idx] == '\n') {
				lbCounter++;
			}
		}
		String truncatedString = String.valueOf(arr, idx, arr.length - idx);
		return truncatedString;
	}

	public static String byteArrayToString(byte[] data) {
		if (data == null) {
			return "";
		}
		StringBuilder str = new StringBuilder();
		for (int index = 0; index < data.length; index++) {
			str.append((char) data[index]);
		}
		return str.toString();
	}

	public static String addLineNumbers(String text) {
		String[] lines = text.split("\n");
		String max_num = String.valueOf(lines.length);
		String format = new StringBuilder().append("%0")
				.append(max_num.length()).append("d: ").toString();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lines.length; i++) {
			sb.append(String.format(format,
					new Object[] { Integer.valueOf(i + 1) }));
			sb.append(lines[i]);
			sb.append('\n');
		}
		return sb.toString();
	}

	public static InputStream String2InputStream(String str) {
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}

	public static String inputStream2String(InputStream is) {
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
}