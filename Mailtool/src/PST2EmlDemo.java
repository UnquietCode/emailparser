import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.independentsoft.msg.Attachment;
import com.independentsoft.msg.Message;
import com.independentsoft.msg.Recipient;
import com.independentsoft.msg.RecipientType;
import com.independentsoft.pst.Folder;
import com.independentsoft.pst.Item;
import com.independentsoft.pst.PstFile;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-23
 */
public class PST2EmlDemo {

	private Session session;
	private int emlCounter;

	public static void main(String[] argv) {
		String inputFilePath = "C:\\testPST.pst";
		String outputFolder = "C:\\PST";

		if (!isPST(inputFilePath)) {
			return;
		}

		PST2EmlDemo instance = new PST2EmlDemo();
		instance.extractEml(inputFilePath, outputFolder);
	}

	public static boolean isPST(String fileName) {
		if ("pst".equalsIgnoreCase(Util.getExtension(fileName))) {
			return true;
		}
		return false;
	}

	public void extractEml(String pstPath, String outputFolder) {
		String parentFolderPath = outputFolder;
		Properties properties = System.getProperties();
		this.session = Session.getInstance(properties);
		this.emlCounter = 1;
		try {
			PstFile file = new PstFile(pstPath);
			try {
				List<?> allFolders = file.getRoot().getFolders(true);

				HashMap<Long, String> parents = new HashMap<Long, String>();

				File newFolder = new File(outputFolder);
				newFolder.mkdirs();

				parents.put(Long.valueOf(file.getRoot().getId()),
						parentFolderPath);

				for (int p = 0; p < allFolders.size(); p++) {
					Folder currentFolder = (Folder) allFolders.get(p);

					parentFolderPath = (String) parents.get(Long
							.valueOf(currentFolder.getParentId()));

					newFolder = new File(parentFolderPath + File.separator
							+ getFileName(currentFolder.getDisplayName()));

					newFolder.mkdirs();

					parents.put(
							Long.valueOf(currentFolder.getId()),
							parentFolderPath
									+ File.separator
									+ getFileName(currentFolder
											.getDisplayName()));
				}

				for (int j = 0; j < allFolders.size(); j++)
					for (int s = 0; s < ((Folder) allFolders.get(j))
							.getChildrenCount(); s += 100) {
						List<?> items = ((Folder) allFolders.get(j)).getItems(
								s, s + 100);

						for (int k = 0; k < items.size(); k++) {
							Item item = (Item) items.get(k);
							Message message = item.getMessageFile();

							MimeMessage mimeMessage = convertToMimeMessage(message);

							String parentFolder = (String) parents.get(Long
									.valueOf(item.getParentId()));

							String fileName = Integer.toString(this.emlCounter);
							this.emlCounter += 1;

							String filePath = parentFolder + File.separator
									+ fileName + ".eml";

							if (filePath.length() > 244) {
								filePath = filePath.substring(0, 244);
							}

							File emlFile = new File(filePath);
							mimeMessage.writeTo(new FileOutputStream(emlFile));
						}
					}
			} catch (IOException e) {
				e.printStackTrace(System.out);
			} catch (MessagingException e) {
				e.printStackTrace(System.out);
			} finally {
				if (file != null)
					file.close();
			}
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
	}

	private MimeMessage convertToMimeMessage(Message message)
			throws MessagingException, IOException {
		MimeMessage mimeMessage = new MimeMessage(this.session);

		if (message.getTransportMessageHeaders() != null) {
			InternetHeaders headers = new InternetHeaders(
					new ByteArrayInputStream(message
							.getTransportMessageHeaders().getBytes()));
			headers.removeHeader("Content-Type");

			Enumeration<?> allHeaders = headers.getAllHeaders();

			while (allHeaders.hasMoreElements()) {
				Header header = (Header) allHeaders.nextElement();
				mimeMessage.addHeader(header.getName(), header.getValue());
			}
		} else {
			mimeMessage.setSubject(message.getSubject());
			mimeMessage.setSentDate(message.getClientSubmitTime());

			InternetAddress fromMailbox = new InternetAddress();

			fromMailbox.setAddress(message.getSenderEmailAddress());

			if ((message.getSenderName() != null)
					&& (message.getSenderName().length() > 0))
				fromMailbox.setPersonal(message.getSenderName());
			else {
				fromMailbox.setPersonal(message.getSenderEmailAddress());
			}

			mimeMessage.setFrom(fromMailbox);

			for (int i = 0; i < message.getRecipients().size(); i++) {
				Recipient recipient = (Recipient) message.getRecipients()
						.get(i);

				if (recipient.getRecipientType() == RecipientType.TO)
					mimeMessage.setRecipient(MimeMessage.RecipientType.TO,
							new InternetAddress(recipient.getEmailAddress(),
									recipient.getDisplayName()));
				else if (recipient.getRecipientType() == RecipientType.CC)
					mimeMessage.setRecipient(MimeMessage.RecipientType.CC,
							new InternetAddress(recipient.getEmailAddress(),
									recipient.getDisplayName()));
				else if (recipient.getRecipientType() == RecipientType.BCC) {
					mimeMessage.setRecipient(MimeMessage.RecipientType.BCC,
							new InternetAddress(recipient.getEmailAddress(),
									recipient.getDisplayName()));
				}
			}
		}

		MimeMultipart rootMultipart = new MimeMultipart();
		MimeMultipart contentMultipart = new MimeMultipart();

		MimeBodyPart contentBodyPart = new MimeBodyPart();
		contentBodyPart.setContent(contentMultipart);

		if ((message.getBody() != null) && (message.getBody().length() > 0)) {
			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText(message.getBody());
			contentMultipart.addBodyPart(textBodyPart);
		}

		if (message.getBodyHtmlText() != null) {
			MimeBodyPart htmlBodyPart = new MimeBodyPart();
			String htmlPart = message.getBodyHtmlText();
			htmlBodyPart.setDataHandler(new DataHandler(
					new ByteArrayDataSource(htmlPart, "text/html")));
			contentMultipart.addBodyPart(htmlBodyPart);
		}

		if ((message.getBody() == null) && (message.getBodyHtmlText() == null)) {
			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setText("<<Empty Body>>");
			textBodyPart
					.addHeaderLine("Content-Type: text/plain; charset=\"utf-8\"");
			textBodyPart
					.addHeaderLine("Content-Transfer-Encoding: quoted-printable");
			contentMultipart.addBodyPart(textBodyPart);
		}

		rootMultipart.addBodyPart(contentBodyPart);

		for (int i = 0; i < message.getAttachments().size(); i++) {
			Attachment attachment = (Attachment) message.getAttachments()
					.get(i);

			if ((attachment != null) && (attachment.toByteArray() != null)) {
				MimeBodyPart attachmentBodyPart = new MimeBodyPart();

				if (attachment.getMimeTag() != null) {
					DataSource source = new ByteArrayDataSource(
							attachment.toByteArray(), attachment.getMimeTag());
					attachmentBodyPart.setDataHandler(new DataHandler(source));
				} else {
					DataSource source = new ByteArrayDataSource(
							attachment.toByteArray(),
							"application/octet-stream");
					attachmentBodyPart.setDataHandler(new DataHandler(source));
				}

				attachmentBodyPart.setContentID(attachment.getContentId());

				String fileName = "";

				if (attachment.getLongFileName() != null)
					fileName = attachment.getLongFileName();
				else if (attachment.getDisplayName() != null)
					fileName = attachment.getDisplayName();
				else if (attachment.getFileName() != null) {
					fileName = attachment.getFileName();
				}

				attachmentBodyPart.setFileName(fileName);

				rootMultipart.addBodyPart(attachmentBodyPart);
			}
		}

		mimeMessage.setContent(rootMultipart);

		return mimeMessage;
	}

	private String getFileName(String subject) {
		if ((subject == null) || (subject.length() == 0)) {
			String fileName = "NoSubject";
			return fileName;
		}
		String fileName = "";

		for (int i = 0; i < subject.length(); i++) {
			if ((subject.charAt(i) > '\037') && (subject.charAt(i) < '')) {
				fileName = fileName + subject.charAt(i);
			}
		}

		fileName = fileName.replace("\\", "_");
		fileName = fileName.replace("/", "_");
		fileName = fileName.replace(":", "_");
		fileName = fileName.replace("*", "_");
		fileName = fileName.replace("?", "_");
		fileName = fileName.replace("\"", "_");
		fileName = fileName.replace("<", "_");
		fileName = fileName.replace(">", "_");
		fileName = fileName.replace("|", "_");
		fileName = fileName.replace(" ", "_");

		return fileName;
	}
}