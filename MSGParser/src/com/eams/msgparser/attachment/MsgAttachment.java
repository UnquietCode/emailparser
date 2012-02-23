package com.eams.msgparser.attachment;

import com.eams.msgparser.Message;

/**
 * 
 * @Description This {@link Attachment} implementation represents a .msg object
 *              attachment. Instead of storing a byte[] of the attachment, this
 *              implementation provides an embedded {@link Message} object.
 * @Author zhangzuoqiang
 * @Date 2012-2-23
 */
public class MsgAttachment implements Attachment {

	/**
	 * The encapsulated (attached) message.
	 */
	Message message = null;

	/**
	 * @return the message
	 */
	public Message getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(Message message) {
		this.message = message;
	}

	/**
	 * Returns the String returned by {@link Message#toString()}.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (this.message == null) {
			return null;
		}
		return "Mail Attachment: " + this.message.toString();
	}
}
