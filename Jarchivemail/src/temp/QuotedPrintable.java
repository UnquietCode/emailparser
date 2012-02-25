package temp;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-23
 */
public class QuotedPrintable {

	public static String decode(String text) {
		StringBuffer out = new StringBuffer();

		int startIndex = 0;
		int endIndex = 0;

		while ((endIndex = text.indexOf('=', startIndex)) >= 0) {
			if (text.length() > endIndex + 2) {
				String isoChar = text.substring(endIndex + 1, endIndex + 3);
				try {
					char c = (char) (Integer.valueOf(isoChar, 16).intValue());
					out.append(text.substring(startIndex, endIndex));
					out.append(c);
					startIndex = endIndex + 3;
				} catch (NumberFormatException e) {
					out.append(text.substring(startIndex, endIndex));
					startIndex = endIndex + 3;
				}
			} else {
				out.append(text.substring(startIndex, endIndex));
				startIndex = endIndex + 1;
			}
		}
		out.append(text.substring(startIndex));
		return out.toString();
	}
}
