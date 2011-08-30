/**
 * @Package: factory.mbox.headers
 * @Title: DateHeader.java
 * @Author: zhangzuoqiang
 * @Time: 5:23:29 PM Aug 29, 2011
 * @Version: 
 */
package factory.mbox.headers;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.auxilii.msgparser.Message;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class DateHeader extends HeaderParser {

	public static final SimpleDateFormat date_format = new SimpleDateFormat(
			"EEE, dd MMM yyyy HH:mm:ss ZZZZ", Locale.US);

	public DateHeader() {
		super("Date");
	}

	@Override
	public void parse(Message message, String line) throws Exception {
		message.setDate(date_format.parse(line));
	}
}
