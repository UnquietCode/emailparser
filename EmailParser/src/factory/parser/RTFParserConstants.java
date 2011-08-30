/**
 * @Package: factory.parser
 * @Title: RTFParserConstants.java
 * @Author: zhangzuoqiang
 * @Time: 6:10:50 PM Aug 29, 2011
 * @Version: 
 */
package factory.parser;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public interface RTFParserConstants {

	int EOF = 0;
	int C_ESC = 5;
	int C_BRACE_OPEN = 6;
	int C_BRACE_CLOSE = 7;
	int C_NONBREAKING_SPACE = 8;
	int C_OPTIONAL_HYPHEN = 9;
	int C_NONBREAKING_HYPHEN = 10;
	int C_GROUP_START = 11;
	int C_GROUP_END = 12;
	int C_BACKSLASH = 13;
	int C_ESC_CHAR = 14;
	int C_CODEPAGE = 15;
	int C_COMMAND = 16;
	int C_SKIP_NEXT = 17;
	int STRING = 18;

	int DEFAULT = 0;

	String[] tokenImage = { "<EOF>", "\" \"", "\"\\n\"", "\"\\r\"",
			"\"\\r\\n\"", "\"\\\\\"", "<C_BRACE_OPEN>", "<C_BRACE_CLOSE>",
			"\"\\\\~\"", "\"\\\\-\"", "\"\\\\_\"", "\"{\"", "\"}\"",
			"\"\\\\\\\\\"", "<C_ESC_CHAR>", "<C_CODEPAGE>", "<C_COMMAND>",
			"<C_SKIP_NEXT>", "<STRING>", };

}