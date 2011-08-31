/**
 * @Package: factory.parser
 * @Title: EmlParser.java
 * @Author: zhangzuoqiang
 * @Time: 10:04:49 PM Aug 29, 2011
 * @Version: 
 */
package factory.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import model.AttachVO;
import factory.mbox.MailAddress;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class EmlParser {

	public EmlParser() {
	}

	public void parse(File file) {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			mimeMessage = new MimeMessage(null, inputStream);
			toHeaderEnum();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获得发件人的姓名
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getFromName() throws Exception {
		InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
		String personal = address[0].getPersonal();
		if (personal == null) {
			personal = "";
		}
		return personal;
	}

	/**
	 * 获得发件人的地址
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getFromEmail() throws Exception {
		InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
		String from = address[0].getAddress();
		if (from == null) {
			from = "";
		}
		return from;
	}

	/**
	 * 获得邮件的收件人，抄送，和密送的地址和姓名，根据所传递的参数的不同 <br/>
	 * "to"----收件人　"cc"---抄送人地址　"bcc"---密送人地址
	 * 
	 * @param type
	 * @return
	 */
	public List<MailAddress> getRecipients(String type) {
		List<MailAddress> list = new ArrayList<MailAddress>();
		try {
			InternetAddress[] address = null;
			String addtype = type.toUpperCase();
			if (addtype.equals("TO") || addtype.equals("CC")
					|| addtype.equals("BBC")) {
				if (addtype.equals("TO")) {
					address = (InternetAddress[]) mimeMessage
							.getRecipients(Message.RecipientType.TO);
				} else if (addtype.equals("CC")) {
					address = (InternetAddress[]) mimeMessage
							.getRecipients(Message.RecipientType.CC);
				} else {
					address = (InternetAddress[]) mimeMessage
							.getRecipients(Message.RecipientType.BCC);
				}
				if (address != null) {
					for (int i = 0; i < address.length; i++) {
						String email = address[i].getAddress();
						if (email == null) {
							email = "";
						} else {
							email = MimeUtility.decodeText(email);
						}
						String personal = address[i].getPersonal();
						if (personal == null) {
							personal = "";
						} else {
							personal = MimeUtility.decodeText(personal);
						}
						list.add(new MailAddress(personal, email));
					}
				}
			}
		} catch (Exception e) {
		}
		return list;
	}

	/**
	 * 获得邮件主题
	 * 
	 * @return
	 */
	public String getSubject() {
		String subject = "";
		try {
			subject = MimeUtility.decodeText(mimeMessage.getSubject());
			if (subject == null)
				subject = "";
		} catch (Exception e) {

		}
		return subject;
	}

	/**
	 * 获得邮件发送日期
	 * 
	 * @return
	 * @throws Exception
	 */
	public Date getSendDate() throws Exception {
		Date senddate = mimeMessage.getSentDate();
		return senddate;
	}

	/**
	 * 解析邮件，把得到的邮件内容保存到一个StringBuffer对象中<br/>
	 * 解析邮件 主要是根据MimeType类型的不同执行不同的操作，一步一步的解析
	 * 
	 * @param part
	 * @throws Exception
	 */
	public void initMailContent(Part part) throws Exception {
		String contenttype = part.getContentType();
		int nameindex = contenttype.indexOf("name");
		boolean conname = false;
		if (nameindex != -1) {
			conname = true;
		}
		if (part.isMimeType("text/plain") && !conname) {
			bodytext.append((String) part.getContent());
		} else if (part.isMimeType("text/html") && !conname) {
			bodytext.append((String) part.getContent());
		} else if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			int counts = multipart.getCount();
			for (int i = 0; i < counts; i++) {
				initMailContent(multipart.getBodyPart(i));
			}
		} else if (part.isMimeType("message/rfc822")) {
			initMailContent((Part) part.getContent());
		}
	}

	/**
	 * 获得邮件正文内容
	 * 
	 * @return
	 */
	public String getBodyText() {
		return bodytext.toString();
	}

	/**
	 * 判断此邮件是否需要回执，如果需要回执返回"true",否则返回"false"
	 * 
	 * @return
	 * @throws MessagingException
	 */
	public boolean getReplySign() throws MessagingException {
		boolean replysign = false;
		String needreply[] = mimeMessage
				.getHeader("Disposition-Notification-To");
		if (needreply != null) {
			replysign = true;
		}
		return replysign;
	}

	/**
	 * 获得此邮件的Message-ID
	 * 
	 * @return
	 * @throws MessagingException
	 */
	public String getMessageId() throws MessagingException {
		return mimeMessage.getMessageID();
	}

	/**
	 * 判断此邮件是否已读，如果未读返回返回false,反之返回true
	 * 
	 * @return
	 * @throws MessagingException
	 */
	public boolean isNew() throws MessagingException {
		boolean isnew = false;
		Flags flags = ((Message) mimeMessage).getFlags();
		Flags.Flag[] flag = flags.getSystemFlags();
		for (int i = 0; i < flag.length; i++) {
			if (flag[i] == Flags.Flag.SEEN) {
				isnew = true;
				break;
			}
		}
		return isnew;
	}

	/**
	 * 判断此邮件是否包含附件
	 * 
	 * @param part
	 * @return
	 * @throws Exception
	 */
	public boolean isContainAttach(Part part) throws Exception {
		boolean attachflag = false;
		// String contentType = part.getContentType();
		if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			// 获取附件名称可能包含多个附件
			for (int j = 0; j < mp.getCount(); j++) {
				BodyPart mpart = mp.getBodyPart(j);
				String disposition = mpart.getDescription();
				if ((disposition != null)
						&& ((disposition.equals(Part.ATTACHMENT)) || (disposition
								.equals(Part.INLINE)))) {
					attachflag = true;
				} else if (mpart.isMimeType("multipart/*")) {
					attachflag = isContainAttach((Part) mpart);
				} else {
					String contype = mpart.getContentType();
					if (contype.toLowerCase().indexOf("application") != -1)
						attachflag = true;
					if (contype.toLowerCase().indexOf("name") != -1)
						attachflag = true;
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			attachflag = isContainAttach((Part) part.getContent());
		}
		return attachflag;
	}

	/**
	 * 保存附件，并返回附件名称
	 * 
	 * @param part
	 * @throws Exception
	 */
	public List<AttachVO> getAttachments(Part part) throws Exception {
		// 不存在附件
		if (!isContainAttach(part)) {
			return null;
		}
		List<AttachVO> list = new ArrayList<AttachVO>();
		String fileName = "";
		if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			for (int j = 0; j < mp.getCount(); j++) {
				BodyPart mpart = mp.getBodyPart(j);
				String disposition = mpart.getDescription();
				if ((disposition != null)
						&& ((disposition.equals(Part.ATTACHMENT)) || (disposition
								.equals(Part.INLINE)))) {
					fileName = mpart.getFileName();
					if (fileName.toLowerCase().indexOf("UTF-8") != -1) {
						fileName = MimeUtility.decodeText(fileName);
					}
					list.add(new AttachVO(fileName, mpart.getInputStream()));
				} else if (mpart.isMimeType("multipart/*")) {
					fileName = mpart.getFileName();
				} else {
					fileName = mpart.getFileName();
					if ((fileName != null)) {
						fileName = MimeUtility.decodeText(fileName);
						list.add(new AttachVO(fileName, mpart.getInputStream()));
					}
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			getAttachments((Part) part.getContent());
		}
		return list;
	}

	public void toHeaderEnum() {
		try {
			header = this.getMimeMessage().getAllHeaders();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public String getHeaders() {
		String headers = "";
		while (this.header.hasMoreElements()) {
			Header h = (Header) this.header.nextElement();
			headers += h.getName() + " : " + h.getValue() + "\n";
		}
		return headers;
	}

	public MimeMessage getMimeMessage() {
		return mimeMessage;
	}

	public void setMimeMessage(MimeMessage mimeMessage) {
		this.mimeMessage = mimeMessage;
	}

	public StringBuffer getBodyTxt() {
		return bodytext;
	}

	public void setBodyTxt(StringBuffer bodytext) {
		this.bodytext = bodytext;
	}

	private Enumeration<?> header = null;
	private MimeMessage mimeMessage = null;
	// 存放邮件内容的StringBuffer对象
	private StringBuffer bodytext = new StringBuffer();

}
