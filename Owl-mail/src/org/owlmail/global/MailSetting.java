package org.owlmail.global;

import java.util.Locale;
import java.util.prefs.Preferences;

import org.owlmail.view.Owlmail;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-27
 */
public class MailSetting {

	private static Preferences prefs = Preferences.userRoot().node(
			Owlmail.class
					.getName()
					.toLowerCase()
					.substring(
							0,
							Owlmail.class.getName().toLowerCase()
									.lastIndexOf(".")).replace(".", "/"));

	private static boolean manualSearch;
	private static boolean enableCaching;
	private static Locale currentLocale = Locale.getDefault();

	public static boolean isManualSearch() {
		int tmp = 0;
		tmp = prefs.getInt(new String("ManualSearch").toLowerCase(), tmp);
		if (tmp == 1) {
			manualSearch = true;
		} else {
			manualSearch = false;
		}
		return manualSearch;
	}

	/**
	 * 
	 * @param ManualSearch
	 *            the liveSearch to set
	 */
	public static void setManualSearch(boolean ManualSearch) {
		int tmp = 0;
		if (ManualSearch) {
			tmp = 1;
		}
		prefs.putInt(new String("ManualSearch").toLowerCase(), tmp);
		manualSearch = ManualSearch;
	}

	public static boolean isEnableCaching() {
		int tmp = 0;
		tmp = prefs.getInt(new String("EnableCaching").toLowerCase(), tmp);
		if (tmp == 1) {
			enableCaching = true;
		} else {
			enableCaching = false;
		}
		return enableCaching;
	}

	/**
	 * 
	 * @param EnableCaching
	 *            the enableCaching to set
	 */
	public static void setEnableCaching(boolean EnableCaching) {
		int tmp = 0;
		if (EnableCaching) {
			tmp = 1;
		}
		prefs.putInt(new String("EnableCaching").toLowerCase(), tmp);
		enableCaching = EnableCaching;
	}

	/**
	 * 
	 * @return the currentLocalte
	 */
	public static Locale getCurrentLocale() {
		return currentLocale;
	}

	/**
	 * the currentLocalte
	 * 
	 * @param currentLocale
	 */
	public static void setCurrentLocale(Locale currentLocale) {
		MailSetting.currentLocale = currentLocale;
	}
}
