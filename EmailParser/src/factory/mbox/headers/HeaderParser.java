/**
 * @Package: factory.mbox.headers
 * @Title: HeaderParser.java
 * @Author: zhangzuoqiang
 * @Time: 5:19:57 PM Aug 29, 2011
 * @Version: 
 */
package factory.mbox.headers;

import com.auxilii.msgparser.Message;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public abstract class HeaderParser {

	final String header;

	public HeaderParser(String header) {
		this.header = header;
	}

	public String getHeader() {
		return header;
	}

	public abstract void parse(Message message, String line) throws Exception;
}