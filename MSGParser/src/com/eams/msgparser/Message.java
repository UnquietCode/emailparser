package com.eams.msgparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.eams.msgparser.attachment.Attachment;
import com.eams.msgparser.attachment.FileAttachment;
import com.eams.msgparser.attachment.MsgAttachment;
import com.eams.msgparser.util.TNEFUtils;

/**
 * 
 * @Description Class that represents a .msg file. Some fields from the .msg
 *              file are stored in special parameters (e.g., {@link #fromEmail}
 *              ). Attachments are stored in the property {@link #attachments}).
 *              An attachment may be of the type {@link MsgAttachment} which
 *              represents another attached (encapsulated) .msg object.
 * @Author zhangzuoqiang
 * @Date 2012-2-23
 */
public class Message {

	protected static final Logger logger = Logger.getLogger(Message.class
			.getName());

	/**
	 * The message class as defined in the .msg file.
	 */
	protected String messageClass = "IPM.Note";
	/**
	 * The message Id.
	 */
	protected String messageId = null;
	/**
	 * The address part of From: mail address.
	 */
	protected String fromEmail = null;
	/**
	 * The name part of the From: mail address
	 */
	protected String fromName = null;
	/**
	 * The mail's subject.
	 */
	protected String subject = null;
	/**
	 * The normalized body text.
	 */
	protected String bodyText = null;
	/**
	 * The displayed To: field
	 */
	protected String displayTo = null;
	/**
	 * The displayed Cc: field
	 */
	protected String displayCc = null;
	/**
	 * The displayed Bcc: field
	 */
	protected String displayBcc = null;

	/**
	 * The body in RTF format (if available)
	 */
	protected String bodyRTF = null;

	/**
	 * Email headers (if available)
	 */
	protected String headers = null;

	/**
	 * Email Date
	 */
	protected String date = null;

	/**
	 * A list of all attachments (both {@link FileAttachment} and
	 * {@link MsgAttachment}).
	 */
	protected List<Attachment> attachments = new ArrayList<Attachment>();
	/**
	 * Contains all properties that are not covered by the special properties.
	 */
	protected Map<String, Object> properties = new HashMap<String, Object>();
	/**
	 * A list containing all recipients for this message (which can be set in
	 * the 'to:', 'cc:' and 'bcc:' field, respectively).
	 */
	protected List<RecipientEntry> recipients = new ArrayList<RecipientEntry>();

	public void addAttachment(Attachment attachment) {
		this.attachments.add(attachment);
	}

	public void addRecipient(RecipientEntry recipient) {
		this.recipients.add(recipient);
	}

	/**
	 * Sets the name/value pair in the {@link #properties} map. Some properties
	 * are put into special attributes (e.g., {@link #toEmail} when the property
	 * name is '0076').
	 * 
	 * @param name
	 *            The property name (i.e., the class of the document entry).
	 * @param value
	 *            The value of the field.
	 * @throws ClassCastException
	 *             Thrown if the detected data type does not match the expected
	 *             data type.
	 */
	public void setProperty(String name, Object value)
			throws ClassCastException {

		if ((name == null) || (value == null)) {
			return;
		}
		name = name.intern();

		// we know that the name is lower case
		// because this is done in MsgParser.analyzeDocumentEntry
		if (name == "001a") {
			this.setMessageClass((String) value);
		} else if (name == "1035") {
			this.setMessageId((String) value);
		} else if (name == "0037") {
			this.setSubject((String) value);
		} else if (name == "0c1f") {
			this.setFromEmail((String) value);
		} else if (name == "0042") {
			this.setFromName((String) value);
		} else if (name == "0e04") {
			this.setDisplayTo((String) value);
		} else if (name == "0e03") {
			this.setDisplayCc((String) value);
		} else if (name == "0e02") {
			this.setDisplayBcc((String) value);
		} else if (name == "1000") {
			if (value instanceof String) {
				this.setBodyText((String) value);
			} else if (value instanceof byte[]) {
				this.setBodyText(new String((byte[]) value));
			} else {
				logger.log(Level.FINE, "Unexpected body class: "
						+ value.getClass().getName());
				this.setBodyText(value.toString());
			}
		} else if (name == "1009") {
			// we simply try to decompress the RTF data
			// if it's not compressed, the utils class
			// is able to detect this anyway
			if (value instanceof byte[]) {
				byte[] compressedRTF = (byte[]) value;
				try {
					byte[] decompressedRTF = TNEFUtils
							.decompressRTF(compressedRTF);
					// is RTF always in ANSI encoding?
					this.setBodyRTF(new String(decompressedRTF));
				} catch (Exception e) {
					logger.log(Level.FINEST, "Could not decompress RTF data", e);
				}
			} else {
				logger.log(Level.FINEST,
						"Unexpected data type " + value.getClass());
			}
		} else if (name == "007d") {
			// email headers
			String headers = (String) value;
			this.setHeaders(headers);
			// try to parse the date from the headers
			String date = Message.getDateFromHeaders(headers);
			if (date != null) {
				this.setDate(date);
			}
		}

		// save all properties (incl. those identified above)
		this.properties.put(name, value);

		// other possible values (some are duplicates)
		// 0044: recv name
		// 004d: author
		// 0050: reply
		// 005a: sender
		// 0065: sent email
		// 0076: received email
		// 0078: repr. email
		// 0c1a: sender name
		// 0e04: to
		// 0e1d: subject normalized
		// 1046: sender email
		// 3003: email address
		// 1008 rtf sync
	}

	/**
	 * Provides a short representation of this .msg object.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("From: "
				+ this.createMailString(this.fromEmail, this.fromName) + "\n");

		sb.append("To: ");
		for (RecipientEntry recipient : this.recipients) {
			sb.append(this
					.createMailString(recipient.toEmail, recipient.toName));
		}
		sb.append("\n");

		if (this.date != null) {
			sb.append("Date: " + this.date + "\n");
		}
		if (this.subject != null)
			sb.append("Subject: " + this.subject + "\n");
		sb.append("" + this.attachments.size() + " attachments.");
		return sb.toString();
	}

	/**
	 * Provides all information of this message object.
	 * 
	 * @return The full message information.
	 */
	public String toLongString() {
		StringBuffer sb = new StringBuffer();
		sb.append("From: "
				+ this.createMailString(this.fromEmail, this.fromName) + "\n");

		sb.append("To: ");
		for (RecipientEntry recipient : this.recipients) {
			sb.append(this
					.createMailString(recipient.toEmail, recipient.toName));
		}
		sb.append("\n");

		if (this.date != null) {
			sb.append("Date: " + this.date + "\n");
		}
		if (this.subject != null)
			sb.append("Subject: " + this.subject + "\n");
		sb.append("\n");
		if (this.bodyText != null)
			sb.append(this.bodyText);
		if (this.attachments.size() > 0) {
			sb.append("\n");
			sb.append("" + this.attachments.size() + " attachments.\n");
			for (Attachment att : this.attachments) {
				sb.append(att.toString() + "\n");
			}
		}
		return sb.toString();
	}

	/**
	 * Convenience method for creating an email address expression (including
	 * the name, the address, or both).
	 * 
	 * @param mail
	 *            The mail address.
	 * @param name
	 *            The name part of the address.
	 * @return A combination of the name and address.
	 */
	public String createMailString(String mail, String name) {
		if ((mail == null) && (name == null)) {
			return null;
		}
		if (name == null) {
			return mail;
		}
		if (mail == null) {
			return name;
		}
		if (mail.equalsIgnoreCase(name)) {
			return mail;
		}
		return "\"" + name + "\" <" + mail + ">";
	}

	/**
	 * @return the attachments
	 */
	public List<Attachment> getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments
	 *            the attachments to set
	 */
	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	/**
	 * @return the recipients
	 */
	public List<RecipientEntry> getRecipients() {
		return recipients;
	}

	/**
	 * @param recipients
	 *            the recipients to set
	 */
	public void setRecipients(List<RecipientEntry> recipients) {
		this.recipients = recipients;
	}

	/**
	 * @return the bodyText
	 */
	public String getBodyText() {
		return bodyText;
	}

	/**
	 * @param bodyText
	 *            the bodyText to set
	 */
	public void setBodyText(String bodyText) {
		if (bodyText != null) {
			bodyText = bodyText.trim();
		}
		this.bodyText = bodyText;
	}

	/**
	 * @return the fromEmail
	 */
	public String getFromEmail() {
		return fromEmail;
	}

	/**
	 * @param fromEmail
	 *            the fromEmail to set
	 */
	public void setFromEmail(String fromEmail) {
		if (fromEmail != null) {
			fromEmail = fromEmail.trim();
		}
		this.fromEmail = fromEmail;
	}

	/**
	 * @return the fromName
	 */
	public String getFromName() {
		return fromName;
	}

	/**
	 * @param fromName
	 *            the fromName to set
	 */
	public void setFromName(String fromName) {
		if (fromName != null) {
			fromName = fromName.trim();
		}
		this.fromName = fromName;
	}

	public String getDisplayTo() {
		return displayTo;
	}

	public void setDisplayTo(String displayTo) {
		this.displayTo = displayTo;
	}

	public String getDisplayCc() {
		return displayCc;
	}

	public void setDisplayCc(String displayCc) {
		this.displayCc = displayCc;
	}

	public String getDisplayBcc() {
		return displayBcc;
	}

	public void setDisplayBcc(String displayBcc) {
		this.displayBcc = displayBcc;
	}

	/**
	 * @return the messageClass
	 */
	public String getMessageClass() {
		return messageClass;
	}

	/**
	 * @param messageClass
	 *            the messageClass to set
	 */
	public void setMessageClass(String messageClass) {
		this.messageClass = messageClass;
	}

	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * @param messageId
	 *            the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		if (subject != null) {
			subject = subject.trim();
		}
		this.subject = subject;
	}

	/**
	 * @return the bodyRTF
	 */
	public String getBodyRTF() {
		return bodyRTF;
	}

	/**
	 * @param bodyRTF
	 *            the bodyRTF to set
	 */
	public void setBodyRTF(String bodyRTF) {
		if (bodyRTF != null) {
			bodyRTF = bodyRTF.trim();
		}
		this.bodyRTF = bodyRTF;
	}

	/**
	 * @return the headers
	 */
	public String getHeaders() {
		return headers;
	}

	/**
	 * @param headers
	 *            the headers to set
	 */
	public void setHeaders(String headers) {
		this.headers = headers;
	}

	/**
	 * Parses the message date from the mail headers.
	 * 
	 * @param headers
	 *            The headers in a single String object
	 * @return The Date object or null, if no valid Date: has been found
	 */
	public static String getDateFromHeaders(String headers) {
		if (headers == null) {
			return null;
		}
		String[] headerLines = headers.split("\n");
		for (String headerLine : headerLines) {
			if (headerLine.toLowerCase().startsWith("date:")) {
				String dateValue = headerLine.substring("Date:".length())
						.trim();
				// There may be multiple Date: headers. Let's take the first one
				// that can be parsed.
				return dateValue;
			}
		}
		return null;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	public Set<String> getProperties() {
		return this.properties.keySet();
	}

	public Object getProperty(String name) {
		return this.properties.get(name);
	}
}
