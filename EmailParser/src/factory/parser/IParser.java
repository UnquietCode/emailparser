/**
 * @Package: factory.parser
 * @Title: IParser.java
 * @Author: zhangzuoqiang
 * @Time: 4:45:20 PM Aug 30, 2011
 * @Version: 
 */
package factory.parser;

import java.io.File;
import java.io.IOException;

import com.auxilii.msgparser.Message;

/**
 * @Description: 
 * @Author: zhangzuoqiang
 * @Date: Aug 30, 2011
 */
public interface IParser {
	
	public Message parse(File file) throws IOException, Exception;
	
	public Message parse(String content) throws Exception;
	
	public void parseHeader(String header) throws Exception;
	
	public void parseBody(String body);
	
	
}
