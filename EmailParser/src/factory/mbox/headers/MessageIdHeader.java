/**
 * @Package: factory.mbox.headers
 * @Title: MessageIdHeader.java
 * @Author: zhangzuoqiang
 * @Time: 5:44:13 PM Aug 29, 2011
 * @Version: 
 */
package factory.mbox.headers;

import com.auxilii.msgparser.Message;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class MessageIdHeader extends HeaderParser {

	public MessageIdHeader() {
		super("Message-Id");
	}

	@Override
	public void parse(Message message, String line) {
		message.setMessageId(line);
	}

}