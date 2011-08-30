/**
 * @Package: factory.parser
 * @Title: MBoxParser.java
 * @Author: zhangzuoqiang
 * @Time: 5:47:53 PM Aug 29, 2011
 * @Version: 
 */
package factory.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.mail.internet.MimeMessage;

import utils.ReadFile;

import com.auxilii.msgparser.Message;

import factory.mbox.headers.DateHeader;
import factory.mbox.headers.FromEmailHeader;
import factory.mbox.headers.MessageIdHeader;
import factory.mbox.headers.SubjectHeader;
import factory.mbox.headers.ToEmailHeader;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class MBoxParser extends AbstractParser {

	public MBoxParser() {
		header_parsers.add(new FromEmailHeader());
		header_parsers.add(new ToEmailHeader());
		header_parsers.add(new DateHeader());
		header_parsers.add(new SubjectHeader());
		header_parsers.add(new MessageIdHeader());
	}

	public Message parse(File file) throws IOException, Exception {
		byte bytes[] = ReadFile.getBytesFromFile(file);
		String content = new String(bytes);
		mimeMessage = new MimeMessage(null, new FileInputStream(file));
		return parse(content);
	}

	public Message parse(String content) throws Exception {
		Message msg = new Message();
		int idx_unix = content.indexOf("\n\n");
		int idx_win = content.indexOf("\r\n\r\n");
		String header = null;
		int start = 0;

		if (content.startsWith("From ")) {
			start = content.indexOf("\n");
		}

		int header_end = 0;

		if (idx_unix > 0) {
			header_end = idx_unix;
		} else if (idx_win > 0) {
			header_end = idx_win;
		}

		header = content.substring(start, header_end);
		msg.setHeaders(header);

		parseHeader(msg, header);
		parseBody(msg, content.substring(header_end + 1));

		return msg;
	}

}