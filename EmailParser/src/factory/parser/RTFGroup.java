/**
 * @Package: factory.parser
 * @Title: RTFGroup.java
 * @Author: zhangzuoqiang
 * @Time: 6:12:27 PM Aug 29, 2011
 * @Version: 
 */
package factory.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class RTFGroup {

	private StringBuilder text_content;
	private List<String> commands = new ArrayList<String>();
	private String last_command = "";

	public RTFGroup() {

	}

	void addCommand(String command) {

		if (command.equals("\\par"))
			addTextContent("\n");
		else if (command.equals("\\tab"))
			addTextContent("\t");
		else {
			commands.add(command);
			last_command = command;
		}
	}

	public void addTextContent(String text) {
		if (text_content == null)
			text_content = new StringBuilder();

		if (last_command.startsWith("\\html"))
			text_content.append(text);
	}

	public boolean isEmpty() {
		if (text_content == null && commands.isEmpty())
			return true;

		if (text_content.length() > 0)
			return false;

		return false;
	}

	public String getTextContent() {
		return text_content.toString();
	}

	public List<String> getCommands() {
		return commands;
	}

	boolean isEmptyText() {

		if (text_content == null)
			return true;

		return text_content.length() == 0;
	}

}