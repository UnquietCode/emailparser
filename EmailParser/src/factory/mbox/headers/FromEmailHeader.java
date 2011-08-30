/**
 * @Package: factory.mbox.headers
 * @Title: FromEmailHeader.java
 * @Author: zhangzuoqiang
 * @Time: 5:43:27 PM Aug 29, 2011
 * @Version: 
 */
package factory.mbox.headers;

import java.util.List;

import com.auxilii.msgparser.Message;

import factory.mbox.MailAddress;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class FromEmailHeader extends EmailHeader {

	public FromEmailHeader() {
		super("From");
	}

	@Override
	public void assign(Message msg, List<MailAddress> emails) {
		msg.setFromEmail(emails.get(0).getEmail());
		msg.setFromName(emails.get(0).getDisplayName());
	}
}