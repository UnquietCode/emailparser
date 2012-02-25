package com.jarchivemail.metadata;

import com.jarchivemail.metadata.Property.PropertyType;
import com.jarchivemail.metadata.Property.ValueType;

/**
 * 
 * @Description XMP property definition violation exception. This is thrown when
 *              you try to set a {@link Property} value with an incorrect type,
 *              such as storing an Integer when the property is of type Date.
 * @Author zhangzuoqiang
 * @Date 2012-2-25
 */
public final class PropertyTypeException extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7267590642707789093L;

	public PropertyTypeException(PropertyType expected, PropertyType found) {
		super("Expected a property of type " + expected + ", but received "
				+ found);
	}

	public PropertyTypeException(ValueType expected, ValueType found) {
		super("Expected a property with a " + expected
				+ " value, but received a " + found);
	}
}
