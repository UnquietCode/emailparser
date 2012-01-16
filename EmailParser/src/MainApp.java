import java.io.FileNotFoundException;
import java.io.IOException;

import utils.ParserManager;
import utils.trace;

/**
 * @Package: 
 * @Title: MainApp.java
 * @Author: zhangzuoqiang
 * @Time: 6:25:23 PM Aug 29, 2011
 * @Version: 
 */

/**
 * @Description: 系统启动类
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class MainApp {

	public static void main(String[] args) throws FileNotFoundException,
			IOException, Exception {
		ParserManager
				.parser("C:\\Users\\zuoqiang\\Desktop\\mbox\\testEml.eml");
		trace.pl("-------------------------------------------------------------------------------------");
		ParserManager
				.parser("C:\\Users\\zuoqiang\\Desktop\\mbox\\testMSG.msg");
		trace.pl("-------------------------------------------------------------------------------------");
		ParserManager
				.parser("C:\\Users\\zuoqiang\\Desktop\\mbox\\testMBox.mbox");
		trace.pl("-------------------------------------------------------------------------------------");
	}
}