/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package factory.mbox.headers;

import java.util.ArrayList;
import java.util.List;

import utils.StringUtils;

import com.auxilii.msgparser.Message;

import factory.mbox.MailAddress;

/**
 * 
 * @author martin
 */
public abstract class EmailHeader extends HeaderParser {

	public EmailHeader(String header) {
		super(header);
	}

	@Override
	public void parse(Message msg, String line) {
		List<MailAddress> emails = splitAttendees(line);
		if (!emails.isEmpty())
			assign(msg, emails);
	}

	public abstract void assign(Message msg, List<MailAddress> emails);

	public static List<MailAddress> splitAttendees(String text) {
		String parts[] = text.split(",");
		List<MailAddress> addresses = new ArrayList<MailAddress>();
		for (String part : parts) {
			MailAddress addr = new MailAddress();
			int start = part.indexOf("<");
			int end = part.indexOf(">");

			if (start >= 0 && end >= 0) {
				addr.setEmail(part.substring(start + 1, end));
				addr.setDisplayName(StringUtils.strip(part.substring(0, start)
						.trim(), "\""));
			} else {
				addr.setEmail(part);
			}
			if (addr.getEmail().contains("@")) {
				addresses.add(addr);
			}
		}
		if (addresses.isEmpty()) {
			return null;
		}
		return addresses;
	}
}