/**
 * @Package: utils
 * @Title: ParserFactory.java
 * @Author: zhangzuoqiang
 * @Time: 5:17:39 PM Aug 29, 2011
 * @Version: 
 */
package factory;

import java.io.File;
import java.io.IOException;

import model.EmailFileVO;
import model.EmailVO;

import com.auxilii.msgparser.Message;

import factory.parser.EmlParser;
import factory.parser.JavaMailParser;
import factory.parser.MBoxParser;
import factory.parser.MSGParser;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class ParserFactory {

	private static ParserFactory instance;

	private MSGParser msg_parser;
	private EmlParser eml_parser;
	private MBoxParser mbox_parser;
	private JavaMailParser jmail_parser;

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
		} catch (Exception e) {
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
			// message = paserJavaMailFile(vo.getFile());
			message = paserMBoxFile(vo.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		email.init(message);
		return email;
	}

	protected Message parseMsgFile(File file) throws Exception {
		if (msg_parser == null) {
			msg_parser = new MSGParser();
		}
		return msg_parser.parse(file);
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

	public MSGParser getMsg_parser() {
		return msg_parser;
	}

	public EmlParser getEml_parser() {
		return eml_parser;
	}

	public MBoxParser getMbox_parser() {
		return mbox_parser;
	}

	public JavaMailParser getJmail_parser() {
		return jmail_parser;
	}

}