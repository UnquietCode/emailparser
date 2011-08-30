/**
 * @Package: utils
 * @Title: ParserManager.java
 * @Author: zhangzuoqiang
 * @Time: 6:30:44 PM Aug 29, 2011
 * @Version: 
 */
package utils;

import java.io.File;
import java.io.FileNotFoundException;

import model.EmailFileVO;
import model.EmailVO;
import factory.ParserFactory;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class ParserManager {

	public static void parser(String file_name) throws Exception {
		EmailFileVO vo = initFileVO(file_name);
		EmailVO email = null;
		if (null == vo) {
			return;
		}
		if (vo.getSuffix().equals("msg")) {
			email = ParserFactory.getInstance().msgParser(vo);
		} else if (vo.getSuffix().equals("eml")) {
			email = ParserFactory.getInstance().emlParser(vo);
		} else if (vo.getSuffix().equals("mbox")) {
			email = ParserFactory.getInstance().mboxParser(vo);
		}
		trace.pl(email.toString());
	}

	private static EmailFileVO initFileVO(String file_name) {
		File file = new File(file_name);
		if (!file.exists()) {
			System.out.println("文件不存在" + file_name);
			return null;
		}
		int idx = file.getName().lastIndexOf(".");
		if (idx < 0) {
			try {
				throw new FileNotFoundException("Cannot identify file type");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		String suffix = file.getName().substring(idx + 1).toLowerCase();

		return new EmailFileVO(suffix, file);
	}

}