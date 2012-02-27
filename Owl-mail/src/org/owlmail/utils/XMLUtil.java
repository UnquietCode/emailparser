package org.owlmail.utils;

import java.io.InputStream;

import javax.swing.Icon;

import org.w3c.dom.Node;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2011-12-29
 */
public class XMLUtil {

	/**
	 * 
	 * @param filePath
	 * @return
	 */
	public static InputStream getXMLFile(String filePath) {
		return ClassLoader.getSystemResourceAsStream(filePath);
	}

	/**
	 * 
	 * @param node
	 * @param name
	 * @return
	 */
	public static String getStringAttribute(Node node, String name) {
		Node attribute = node.getAttributes().getNamedItem(name);
		if (attribute != null) {
			return attribute.getNodeValue();
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param node
	 * @param name
	 * @return
	 */
	public static Icon getIconAttribute(Node node, String name) {
		String iconURL = getStringAttribute(node, name);
		if (iconURL != null && !iconURL.isEmpty()) {
			return ImageUtil.getImageIcon(iconURL);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param node
	 * @param name
	 * @return
	 */
	public static int getIntAttribute(Node node, String name) {
		String value = getStringAttribute(node, name);
		if (value != null && !value.isEmpty()) {
			return Integer.valueOf(value).intValue();
		} else {
			return 0;
		}
	}
}
