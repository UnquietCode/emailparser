/**
 * Messages.java
 * 
 * @author Goldenbogen, Pierre
 *         Created: 15.12.2011 13:17:49
 */
package net.goldenbogen.jmsgreader;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.goldenbogen.jmsgreader.core.UserSettings;

/**
 * 
 * @Description 资源管理类
 * @Author zhangzuoqiang
 * @Date 2012-2-27
 */
public class Resource {

	private static final String BUNDLE_NAME = "net.goldenbogen.jmsgreader.translation";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME, UserSettings.getCurrentLocale());

	private Resource() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
