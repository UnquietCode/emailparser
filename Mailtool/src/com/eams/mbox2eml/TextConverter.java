package com.eams.mbox2eml;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-23
 */
public class TextConverter {

	public static String convert(String s) {
		if (s == null)
			return null;

		String westernEncoding[] = { "=?ISO-8859-15?", "=?ISO-8859-1?",
				"=?WINDOWS-1252?", "=?WINDOWS-1250?" };
		
		s = s.trim();
		int startIndex, endIndex;
		String isoString = "", encoding = "";
		StringBuffer buf = new StringBuffer();
		String out;

		for (int i = 0; i < westernEncoding.length; i++) {
			String currentEncoding = westernEncoding[i];
			while ((startIndex = s.toUpperCase().indexOf(currentEncoding)) >= 0) {
				buf.append(s.substring(0, startIndex));
				s = s.substring(startIndex + currentEncoding.length());
				if (s.length() < 2)
					break;
				encoding = s.toUpperCase().substring(0, 2);
				s = s.substring(2);
				endIndex = s.toUpperCase().indexOf("?=");
				if (endIndex < 0)
					break;
				isoString = s.substring(0, endIndex);
				s = s.substring(endIndex + 2);
				if (encoding.equals("Q?")) {
					isoString = QuotedPrintable.decode(isoString);
				} else if (encoding.equals("B?")) {
					isoString = new String(Base64.decode(isoString.getBytes()));
				}
				buf.append(isoString);
			}
			buf.append(s);
			s = buf.toString();
			buf = new StringBuffer();
		}
		out = s;
		out = out.replace('_', ' ');
		return out;
	}
}