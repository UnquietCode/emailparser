/**
 * @Package: factory.mbox
 * @Title: MBoxWriterViaJavaMail.java
 * @Author: zhangzuoqiang
 * @Time: 6:04:18 PM Aug 29, 2011
 * @Version: 
 */
package factory.mbox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import utils.DeleteDir;
import utils.TempDir;

import com.auxilii.msgparser.Message;
import com.auxilii.msgparser.attachment.Attachment;
import com.auxilii.msgparser.attachment.FileAttachment;

import exception.ParseException;
import factory.mbox.headers.DateHeader;
import factory.parser.HtmlFromRtf;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class MBoxWriterViaJavaMail {

	private Session session;
	private File tmp_dir;

	public MBoxWriterViaJavaMail() {
		session = Session.getInstance(System.getProperties());
	}

	public void write(Message msg, OutputStream out) throws AddressException,
			MessagingException, IOException, ParseException {
		javax.mail.Message jmsg = new MimeMessage(session);
		/*
		 * MailAddress from = new MailAddress(msg.getFromName(),
		 * msg.getFromEmail());
		 * jmsg.addFrom(InternetAddress.parse(from.toString()));
		 */
		writeMBoxHeader(msg, out);
		/*
		 * MailAddress to = new MailAddress(msg.getToName(), msg.getToEmail());
		 * jmsg.addRecipients(RecipientType.TO,
		 * InternetAddress.parse(to.toString()));
		 * 
		 * jmsg.setSubject(msg.getSubject()); jmsg.setSentDate(msg.getDate());
		 */
		MimeMultipart mp = new MimeMultipart();

		MimeBodyPart mp_text_alternate = new MimeBodyPart();
		MimeMultipart mp_alternate = new MimeMultipart("alternative");

		String rtf = msg.getBodyRTF();

		if (rtf != null && !rtf.isEmpty()) {
			MimeBodyPart html_text = new MimeBodyPart();

			String html = rtf;

			if (rtf.contains("\\fromhtml")) {
				HtmlFromRtf rtf2html = new HtmlFromRtf(rtf);
				html = rtf2html.getHTML();
			}
			html_text.setDataHandler(new DataHandler(new ByteArrayDataSource(
					html, "text/html")));
			mp_alternate.addBodyPart(html_text);
		}
		{
			MimeBodyPart plain_text = new MimeBodyPart();
			String plain_text_string = msg.getBodyText();
			plain_text.setText(plain_text_string);
			mp_alternate.addBodyPart(plain_text);

			mp_text_alternate.setContent(mp_alternate);
			MimeBodyPart part = new MimeBodyPart();
			part.setContent(mp_alternate);

			mp.addBodyPart(part);
		}

		for (Attachment att : msg.getAttachments()) {
			FileAttachment fatt = (FileAttachment) att;
			MimeBodyPart part = new MimeBodyPart();
			part.setDisposition(BodyPart.ATTACHMENT);

			part.attachFile(dumpAttachment(fatt));

			mp.addBodyPart(part);
		}

		addHeaders(msg, jmsg);
		jmsg.setContent(mp);
		jmsg.writeTo(out);
		close();
	}

	File dumpAttachment(FileAttachment fatt) throws FileNotFoundException,
			IOException {
		if (tmp_dir == null) {
			try {
				tmp_dir = TempDir.getTempDir(null, null);
			} catch (IOException ex) {
				tmp_dir = new File(System.getProperty("java.io.tmpdir") + "/"
						+ this.getClass().getSimpleName());
			}
		}

		File content = new File(tmp_dir + "/" + fatt.toString());
		FileOutputStream fout = new FileOutputStream(content);

		fout.write(fatt.getData());
		fout.close();
		return content;
	}

	private void writeMBoxHeader(Message msg, OutputStream out)
			throws UnsupportedEncodingException, IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("From ");
		sb.append(msg.getFromEmail());
		sb.append(" ");
		Date date = msg.getDate();
		if (date == null) {
			date = new Date(0);
		}
		sb.append(DateHeader.date_format.format(date));
		sb.append("\r\n");
		out.write(sb.toString().getBytes("ASCII"));
	}

	void addHeaders(Message msg, javax.mail.Message jmsg)
			throws MessagingException {
		if (msg.getHeaders() == null) {
			return;
		}

		String headers[] = msg.getHeaders().split("\n");
		StringBuilder sb = new StringBuilder();
		for (String hl : headers) {
			String header_line = hl.trim();
			if (header_line.startsWith(" ")) {
				sb.append("\n");
				sb.append(header_line);
			} else {
				sb.append(header_line);
				String h = sb.toString();
				int idx = h.indexOf(':');
				if (idx > 0) {
					String name = h.substring(0, idx);
					String value = h.substring(idx + 1);
					if (name.startsWith("From ")) {
						sb.setLength(0);
						continue;
					}
					jmsg.addHeader(name, value);
				}
				sb.setLength(0);
			}
		}
	}

	public void close() {
		if (tmp_dir != null) {
			DeleteDir.deleteDirectory(tmp_dir);
		}
		tmp_dir = null;
	}
}