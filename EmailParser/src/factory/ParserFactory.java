/**
 * @Package: utils
 * @Title: ParserFactory.java
 * @Author: zhangzuoqiang
 * @Time: 5:17:39 PM Aug 29, 2011
 * @Version: 
 */
package factory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import model.EmailFileVO;
import model.EmailVO;

import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.MsgParser;

import factory.mbox.MBoxWriterViaJavaMail;
import factory.parser.EmlParser;
import factory.parser.JavaMailParser;
import factory.parser.MBoxParser;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class ParserFactory {

	private static ParserFactory instance;

	private MsgParser msg_parser;
	private EmlParser eml_parser;
	private MBoxParser mbox_parser;
	private JavaMailParser jmail_parser;
	private MBoxWriterViaJavaMail mbox_writer;

	/**
	 * 单例模式
	 * 
	 * @return
	 */
	public static ParserFactory getInstance() {
		if (instance == null) {
			instance = new ParserFactory();
		}
		return instance;
	}

	public EmailVO msgParser(EmailFileVO vo) {
		EmailVO email = new EmailVO(vo);
		Message message = null;
		try {
			message = parseMsgFile(vo.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		email.init(message);
		return email;
	}

	public EmailVO emlParser(EmailFileVO vo) {
		EmailVO email = new EmailVO(vo);
		try {
			email.init(parseEmlFile(vo.getFile()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return email;
	}

	public EmailVO mboxParser(EmailFileVO vo) {
		EmailVO email = new EmailVO(vo);
		Message message = null;
		try {
			message = paserJavaMailFile(vo.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		email.init(message);
		return email;
	}

	protected Message parseMsgFile(File file) throws IOException {
		if (msg_parser == null) {
			msg_parser = new MsgParser();
		}
		return msg_parser.parseMsg(file);
	}

	protected EmlParser parseEmlFile(File file) throws Exception {
		if (eml_parser == null) {
			eml_parser = new EmlParser();
		}
		eml_parser.parse(file);
		return eml_parser;
	}

	protected Message paserMBoxFile(File file) throws IOException, Exception {
		if (mbox_parser == null) {
			mbox_parser = new MBoxParser();
		}
		return mbox_parser.parse(file);
	}

	protected Message paserJavaMailFile(File file) throws IOException,
			Exception {
		if (jmail_parser == null) {
			jmail_parser = new JavaMailParser();
		}
		return jmail_parser.parse(file);
	}

	// ----------------------------------------------------------------------------------------------

	protected void saveMessage(Message msg, File file)
			throws FileNotFoundException, Exception {
		int idx = file.getName().lastIndexOf(".");
		if (idx < 0) {
			throw new FileNotFoundException("Cannot identify file type");
		}
		String suffix = file.getName().substring(idx + 1).toLowerCase();
		if (suffix.equals("msg")) {
			saveMsgFile(msg, file);
		} else if (suffix.equals("mbox")) {
			saveMBoxFile(msg, file);
		}
	}

	public void saveMsgFile(Message msg, File file) {

	}

	public void saveMBoxFile(Message msg, File file) throws Exception {
		if (mbox_writer == null) {
			mbox_writer = new MBoxWriterViaJavaMail();
		}
		try {
			mbox_writer.write(msg, new FileOutputStream(file));
		} catch (Exception ex) {
			mbox_writer.close();
			throw ex;
		}
	}
}