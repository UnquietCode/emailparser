/**
 * @Package: filter
 * @Title: EmlFilter.java
 * @Author: zhangzuoqiang
 * @Time: 4:43:11 PM Aug 29, 2011
 * @Version: 
 */
package filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import utils.LangUtil;

/**
 * @Description: *.eml格式文件过滤器
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class EmlFilter extends FileFilter {

	@Override
	public boolean accept(File file) {
		if (file.toString().toLowerCase().endsWith(".eml")) {
			return true;
		}
		return false;
	}

	@Override
	public String getDescription() {
		return LangUtil.get("10001");
	}

}
