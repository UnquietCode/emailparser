package com.jarchivemail.metadata;

import java.io.Serializable;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-25
 */
public final class MailType implements Comparable<MailType>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3363806325932801052L;

	private final String type;
	private final String subtype;

	public static final MailType EMAIL_MSG = email("msg");
	public static final MailType EMAIL_EML = email("eml");
	public static final MailType EMAIL_MBOX = email("mbox");
	public static final MailType EMAILPST_ = email("pst");

	public MailType(String type, String subtype) {
		this.type = type.trim().toLowerCase();
		this.subtype = subtype.trim().toLowerCase();
	}

	public static MailType email(String type) {
		return new MailType("email", type);
	}

	public static MailType attachment(String type) {
		return new MailType("attachment", type);
	}

	public MailType getBaseType() {
		return new MailType(type, subtype);
	}

	public String getType() {
		return type;
	}

	public String getSubtype() {
		return subtype;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(type);
		builder.append('/');
		builder.append(subtype);
		return builder.toString();
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof MailType) {
			MailType that = (MailType) object;
			return type.equals(that.type) && subtype.equals(that.subtype);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 31 + type.hashCode();
		hash = hash * 31 + subtype.hashCode();
		return hash;
	}

	@Override
	public int compareTo(MailType that) {
		return toString().compareTo(that.toString());
	}

}
