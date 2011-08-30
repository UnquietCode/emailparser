/**
 * @Package: factory.parser
 * @Title: MSGParser.java
 * @Author: zhangzuoqiang
 * @Time: 5:43:20 PM Aug 30, 2011
 * @Version: 
 */
package factory.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.mail.internet.MimeMessage;

import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.MsgParser;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 30, 2011
 */
public class MSGParser extends AbstractParser {

	private MsgParser msgParser;

	public MSGParser() {
		msgParser = new MsgParser();
	}

	@Override
	public Message parse(File file) throws IOException, Exception {
		mimeMessage = new MimeMessage(null, new FileInputStream(file));
		return msgParser.parseMsg(file);
	}

	public MsgParser getMsgParser() {
		return msgParser;
	}

}
