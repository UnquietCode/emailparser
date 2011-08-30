/**
 * @Package: utils
 * @Title: DeleteDir.java
 * @Author: zhangzuoqiang
 * @Time: 6:21:42 PM Aug 29, 2011
 * @Version: 
 */
package utils;

import java.io.File;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class DeleteDir {

	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();

			if (files == null) {
				return true;
			}
			for (File file : files) {
				if (file.isDirectory()) {
					deleteDirectory(file);
				} else {
					file.delete();
				}
			}
		}
		return path.delete();
	}
}