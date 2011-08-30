/**
 * @Package: factory.parser
 * @Title: IParser.java
 * @Author: zhangzuoqiang
 * @Time: 10:30:24 PM Aug 29, 2011
 * @Version: 
 */
package factory.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.MimeMessage;

import com.auxilii.msgparser.Message;

import factory.mbox.headers.HeaderParser;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public abstract class AbstractParser {

	protected List<HeaderParser> header_parsers = new ArrayList<HeaderParser>();
	protected MimeMessage mimeMessage;

	// protected Message message;

	public Message parse(File file) throws IOException, Exception {
		// mimeMessage = new MimeMessage(null, new FileInputStream(file));
		// message = new Message();
		return null;
	}

	public Message parse(String content) throws Exception {
		return null;
	}

	public void parseHeader(Message message, String header) throws Exception {
		String lines[] = header.split("\n");
		for (String line : lines) {
			for (HeaderParser parser : header_parsers) {
				String header_prefix = parser.getHeader() + ": ";
				if (line.startsWith(header_prefix)) {
					String header_content = line.substring(header_prefix
							.length());
					parser.parse(message, header_content);
				}
			}
		}
	}

	public void parseBody(Message message, String body) {
		message.setBodyText(body);
		message.setBodyRTF("");
	}

	public MimeMessage getMime() {
		return mimeMessage;
	}

	// public Message getMessage() {
	// return message;
	// }

}
