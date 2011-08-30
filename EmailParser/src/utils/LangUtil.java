/**
 * @Package: utils
 * @Title: LangUtil.java
 * @Author: zhangzuoqiang
 * @Time: 4:50:18 PM Aug 29, 2011
 * @Version: 
 */
package utils;


/**
 * @Description: 语言管理 工具类
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class LangUtil {

	/**
	 * 根据key得到value
	 * 
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		return ResourcesUtils.res().getString(key);
	}
}
