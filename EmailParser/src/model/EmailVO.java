/**
 * @Package: model
 * @Title: EmailVO.java
 * @Author: zhangzuoqiang
 * @Time: 10:42:07 AM Aug 30, 2011
 * @Version: 
 */
package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Header;
import javax.mail.Part;

import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.RecipientEntry;

import factory.ParserFactory;
import factory.mbox.MailAddress;
import factory.parser.EmlParser;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 30, 2011
 */
public class EmailVO {

	public EmailVO() {
		this.map = new HashMap<String, Object>();
		this.toList = new ArrayList<Object>();
		this.attachList = new ArrayList<Object>();
	}

	public EmailVO(EmailFileVO vo) {
		this.setFileVO(vo);
		this.map = new HashMap<String, Object>();
		this.toList = new ArrayList<Object>();
		this.attachList = new ArrayList<Object>();
	}

	public void init(Message message) {
		if (null != message.getSubject() || !message.getSubject().equals("")) {
			this.setSubject(message.getSubject());
		}
		String fromName = "";
		String fromEmail = "";
		if (null != message.getFromName()) {
			fromName = message.getFromName();
		}
		if (null != message.getFromEmail()) {
			fromEmail = message.getFromEmail();
		}
		this.setFrom(new MailAddress(fromName, fromEmail));

		if (null != message.getRecipients()
				&& message.getRecipients().size() > 0) {
			this.setToList(message.getRecipients());
		} else if (null != message.getToEmail()) {
			List<RecipientEntry> list = new ArrayList<RecipientEntry>();
			RecipientEntry recip = new RecipientEntry();
			recip.setToEmail(message.getToEmail());
			if (null != message.getToName()) {
				recip.setToName(message.getToName());
			} else {
				recip.setToName("\"\"");
			}
			list.add(recip);
			message.setRecipients(list);
			this.setToList(list);
		}
		if (null != message.getDisplayBcc()) {
			this.getMap().put("BCC", message.getDisplayBcc());
		}
		if (null != message.getDisplayCc()) {
			this.getMap().put("CC", message.getDisplayCc());
		}
		if (null != message.getDate()) {
			this.setDate(message.getDate());
		}
		if (null != message.getMessageId()) {
			this.setMessageID(message.getMessageId());
		}
		if (null != message.getAttachments()
				&& message.getAttachments().size() > 0) {
			this.setAttachList(message.getAttachments());
		}
		if (null != message.getBodyText() || !message.getBodyText().equals("")) {
			this.setBodyTxt(message.getBodyText());
		}

		// 根据文件格式选择处理器
		if (this.getFileVO().getSuffix().equals("mbox")) {
			if (null != ParserFactory.getInstance().getMbox_parser()
					.getHeaderEnum(message)) {
				this.setHeader(ParserFactory.getInstance().getMbox_parser()
						.getHeaderEnum(message));
			}
		} else if (this.getFileVO().getSuffix().equals("msg")) {
			if (null != ParserFactory.getInstance().getMsg_parser()
					.getHeaderEnum(message)) {
//				this.setHeader(ParserFactory.getInstance().getMsg_parser()
//						.getHeaderEnum(message));
			}
		} else {
			this.setHeader(null);
		}

		// if (null != ParserFactory.getInstance().getJmail_parser()
		// .getHeaderEnum(message)) {
		// this.setHeader(ParserFactory.getInstance().getJmail_parser()
		// .getHeaderEnum(message));
		// }
	}

	public void init(EmlParser eml) throws Exception {
		if (null != eml.getSubject() || !eml.getSubject().equals("")) {
			this.setSubject(eml.getSubject());
		}
		if (null == eml.getFromName() || null == eml.getFromEmail()) {
			this.setFrom(new MailAddress("", ""));
		} else {
			this.setFrom(new MailAddress(eml.getFromName(), eml.getFromEmail()));
		}
		if (null != eml.getRecipients("TO")
				&& eml.getRecipients("TO").size() > 0) {
			this.setToList(eml.getRecipients("TO"));
		}
		if (null != eml.getRecipients("BCC")
				&& eml.getRecipients("BCC").size() > 0) {
			this.getMap().put("BCC", eml.getRecipients("BCC"));
		}
		if (null != eml.getRecipients("CC")
				&& eml.getRecipients("CC").size() > 0) {
			this.getMap().put("CC", eml.getRecipients("CC"));
		}
		if (null != eml.getSendDate()) {
			this.setDate(eml.getSendDate());
		}
		if (null != eml.getBodyText() || !eml.getBodyText().equals("")) {
			this.setMessageID(eml.getMessageId());
		}
		// 根据内容的不同解析邮件
		eml.initMailContent((Part) eml.getMimeMessage());
		boolean isContainAttach = eml.isContainAttach((Part) eml
				.getMimeMessage());
		if (isContainAttach) {
			this.setAttachList(eml.getAttachments((Part) eml.getMimeMessage()));
		}
		if (null != eml.getBodyText() || !eml.getBodyText().equals("")) {
			this.setBodyTxt(eml.getBodyText());
		}
		if (null != eml.getHeaderEnum()) {
			this.setHeader(eml.getHeaderEnum());
		}
	}

	@Override
	public String toString() {
		String toStr = "";
		toStr += fileVO.toString() + "\n";
		toStr += "Subject : " + this.getSubject() + "\n";
		toStr += "From : " + this.getFrom().toString() + "\n";
		List<?> list = this.getToList();
		for (Object object : list) {
			toStr += "ToList : " + object.toString() + "\n";
		}
		if (this.getMap().size() > 0) {
			if (this.getMap().containsKey("BCC")) {
				toStr += "BCC : " + this.getMap().get("BCC").toString() + "\n";
			}
			if (this.getMap().containsKey("CC")) {
				toStr += "CC: " + this.getMap().get("CC").toString() + "\n";
			}
		}
		toStr += "Date : " + this.getDate().toString() + "\n";
		toStr += "MessageID : " + this.getMessageID() + "\n";
		list = this.getAttachList();
		for (Object object : list) {
			toStr += object.toString() + "\n";
		}
		toStr += "BodyTxt : " + this.getBodyTxt() + "\n";
		if (null != this.getHeader()) {
			toStr += "Header : " + "\n";
			while (this.getHeader().hasMoreElements()) {
				Header h = (Header) this.getHeader().nextElement();
				toStr += h.getName() + " : " + h.getValue() + "\n";
			}
		}
		return toStr;
	}

	private EmailFileVO fileVO;

	private String subject = "";
	private MailAddress from;
	private List<?> toList;
	private Map<String, Object> map;
	private Date date;
	private String messageID = "";
	private List<?> attachList;
	private String bodyTxt = "";
	private Enumeration<?> header;

	public Enumeration<?> getHeader() {
		return header;
	}

	public void setHeader(Enumeration<?> header) {
		this.header = header;
	}

	public String getBodyTxt() {
		return bodyTxt;
	}

	public void setBodyTxt(String bodyTxt) {
		this.bodyTxt = bodyTxt;
	}

	public EmailFileVO getFileVO() {
		return fileVO;
	}

	public void setFileVO(EmailFileVO fileVO) {
		this.fileVO = fileVO;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public MailAddress getFrom() {
		return from;
	}

	public void setFrom(MailAddress from) {
		this.from = from;
	}

	public List<?> getToList() {
		return toList;
	}

	public void setToList(List<?> toList) {
		this.toList = toList;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public List<?> getAttachList() {
		return attachList;
	}

	public void setAttachList(List<?> attachList) {
		this.attachList = attachList;
	}

}
