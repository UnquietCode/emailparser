/**
 * @Package: factory.parser
 * @Title: ConvertCharset.java
 * @Author: zhangzuoqiang
 * @Time: 6:13:39 PM Aug 29, 2011
 * @Version: 
 */
package factory.parser;

import java.io.UnsupportedEncodingException;

import utils.StringUtils;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class ConvertCharset {

	static String convertCharacter(String characterSet, String text) {
		if (characterSet.isEmpty()) {
			return text;
		}

		byte[] bytes = new byte[1];

		try {
			bytes[0] = (byte) Integer.parseInt(text, 16);
		} catch (NumberFormatException ex) {
			System.out.println("character set: " + characterSet
					+ " hey string '" + text + "', "
					+ StringUtils.exceptionToString(ex));
		}
		try {
			String res = new String(bytes, "CP" + characterSet);
			return res;
		} catch (UnsupportedEncodingException ex) {
			System.out.println("character set: " + characterSet
					+ " hey string '" + text + "', "
					+ StringUtils.exceptionToString(ex));
			return text;
		}
	}
}