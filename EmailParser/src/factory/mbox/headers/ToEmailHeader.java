/**
 * @Package: factory.mbox.headers
 * @Title: ToEmailHeader.java
 * @Author: zhangzuoqiang
 * @Time: 5:45:55 PM Aug 29, 2011
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
public class ToEmailHeader extends EmailHeader {

	public ToEmailHeader() {
		super("From");
	}

	@Override
	public void assign(Message msg, List<MailAddress> emails) {
		msg.setToEmail(emails.get(0).getEmail());
		msg.setToName(emails.get(0).getDisplayName());
	}
}