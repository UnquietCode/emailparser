package com.jarchivemail.metadata;

import java.io.Serializable;

/**
 * 
 * @Description 保存邮件文件属性，如果存在附件，同时记录附件的文件属性
 * @Author zhangzuoqiang
 * @Date 2012-2-25
 */
public final class Email implements Comparable<Email>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3363806325932801052L;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		// builder.append(type);
		// builder.append('/');
		// builder.append(subtype);
		return builder.toString();
	}

	@Override
	public boolean equals(Object object) {
		// if (object instanceof Email) {
		// Email that = (Email) object;
		// return type.equals(that.type) && subtype.equals(that.subtype);
		// } else {
		// return false;
		// }
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 17;
		// hash = hash * 31 + type.hashCode();
		// hash = hash * 31 + subtype.hashCode();
		return hash;
	}

	@Override
	public int compareTo(Email that) {
		return toString().compareTo(that.toString());
	}

}
