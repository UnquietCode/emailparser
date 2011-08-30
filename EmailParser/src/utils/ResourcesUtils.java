/**
 * @Package: utils
 * @Title: ResourcesUtils.java
 * @Author: zhangzuoqiang
 * @Time: 4:51:49 PM Aug 29, 2011
 * @Version: 
 */
package utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public abstract class ResourcesUtils {

	private static final Locale locale = Locale.getDefault();

	public static final ResourceBundle res() {
		return ResourceBundle.getBundle("res.resource", locale);
	}
}