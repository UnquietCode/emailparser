/**
 * @Package: filter
 * @Title: MsgFilter.java
 * @Author: zhangzuoqiang
 * @Time: 4:44:44 PM Aug 29, 2011
 * @Version: 
 */
package filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import utils.LangUtil;

/**
 * @Description: *.msg 格式文件过滤器
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class MsgFilter extends FileFilter {

	@Override
	public boolean accept(File file) {
		if (file.toString().toLowerCase().endsWith(".msg")) {
			return true;
		}
		return false;
	}

	@Override
	public String getDescription() {
		return LangUtil.get("10002");
	}

}
