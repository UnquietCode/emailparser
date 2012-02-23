package com.eams.msgparser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @Description This class represents a recipient's entry of the parsed .msg
 *              file. It provides informations like the email address and the
 *              display name.
 * @Author zhangzuoqiang
 * @Date 2012-2-23
 */
public class RecipientEntry {

	/**
	 * The address part of To: mail address.
	 */
	protected String toEmail = null;
	/**
	 * The address part of To: name.
	 */
	protected String toName = null;
	/**
	 * Contains all properties that are not covered by the special properties.
	 */
	protected Map<String, Object> properties = new HashMap<String, Object>();

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
		if (value instanceof String) {
			if (name == "39fe") {
				this.setToEmail((String) value);
			} else if (name == "3003" && this.getToEmail() == null) {
				this.setToEmail((String) value);
			} else if (name == "3001") {
				this.setToName((String) value);
			}
		}

		// save all properties (incl. those identified above)
		this.properties.put(name, value);
	}

	/**
	 * @return the to: email
	 */
	public String getToEmail() {
		return toEmail;
	}

	/**
	 * @param toEmail
	 *            the to email to be set
	 */
	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	/**
	 * @return the to name
	 */
	public String getToName() {
		return toName;
	}

	/**
	 * @param toName
	 *            the to name to be set
	 */
	public void setToName(String toName) {
		this.toName = toName;
	}

	/**
	 * Provides a short representation of this recipient object <br>
	 * (e.g. 'Firstname Lastname &lt;firstname.lastname@domain.tld&gt;').
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.toName);
		if (sb.length() > 0) {
			sb.append(" ");
		}
		if ((this.toEmail != null) && (this.toEmail.length() > 0)) {
			sb.append("<" + this.toEmail + ">");
		}
		return sb.toString();
	}

	/**
	 * 
	 * @return the set of keys that are stored in the properties map.
	 */
	public Set<String> getProperties() {
		return this.properties.keySet();
	}

	/**
	 * 
	 * @param name
	 *            the name of the property to be returned.
	 * @return the property that matches the given name.
	 */
	public Object getProperty(String name) {
		return this.properties.get(name);
	}
}
