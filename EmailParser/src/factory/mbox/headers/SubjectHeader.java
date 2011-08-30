/**
 * @Package: factory.mbox.headers
 * @Title: SubjectHeader.java
 * @Author: zhangzuoqiang
 * @Time: 5:45:01 PM Aug 29, 2011
 * @Version: 
 */
package factory.mbox.headers;

import com.auxilii.msgparser.Message;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class SubjectHeader extends HeaderParser {

	public SubjectHeader() {
		super("Subject");
	}

	@Override
	public void parse(Message message, String line) {
		message.setSubject(line);
	}
}