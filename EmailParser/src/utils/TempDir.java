/**
 * @Package: utils
 * @Title: TempDir.java
 * @Author: zhangzuoqiang
 * @Time: 6:20:35 PM Aug 29, 2011
 * @Version: 
 */
package utils;

import java.io.File;
import java.io.IOException;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class TempDir {
	
	public static File getTempDir(String prefix, String suffix)
			throws IOException {
		if (prefix == null) {
			prefix = "xxxx";
		}
		File tmpfile = File.createTempFile(prefix, suffix);
		if (!tmpfile.delete()) {
			return null;
		}
		if (!tmpfile.mkdirs()) {
			return null;
		}
		tmpfile.deleteOnExit();
		return tmpfile;
	}
}