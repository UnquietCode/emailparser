package com.eams.msgparser;

import org.apache.poi.poifs.filesystem.DocumentEntry;

/**
 * 
 * @Description Convenience class for storing type information about a
 *              {@link DocumentEntry}.
 * @Author zhangzuoqiang
 * @Date 2012-2-23
 */
public class FieldInformation {

	/**
	 * The default value for both the {@link #clazz} and the {@link #type}
	 * properties.
	 */
	public static final String UNKNOWN = "unknown";

	/**
	 * The class of the {@link DocumentEntry}.
	 */
	protected String clazz = UNKNOWN;
	/**
	 * The type of the {@link DocumentEntry}.
	 */
	protected String type = UNKNOWN;

	/**
	 * Empty constructor that uses the default values.
	 */
	public FieldInformation() {
	}

	/**
	 * Constructor that allows to set the class and type properties.
	 * 
	 * @param clazz
	 *            The class of the {@link DocumentEntry}.
	 * @param type
	 *            The type of the {@link DocumentEntry}.
	 */
	public FieldInformation(String clazz, String type) {
		this.setClazz(clazz);
		this.setType(type);
	}

	/**
	 * @return the clazz
	 */
	public String getClazz() {
		return clazz;
	}

	/**
	 * @param clazz
	 *            the clazz to set
	 */
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

}
