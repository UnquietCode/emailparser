package org.owlmail.res;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.owlmail.global.MailSetting;

/**
 * 
 * @Description 资源管理类
 * @Author zhangzuoqiang
 * @Date 2012-2-27
 */
public class Resource {

	private static final String BUNDLE_NAME = "org.owlmail.res.translation";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME, MailSetting.getCurrentLocale());

	private Resource() {
	}

	public static String getValue(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
