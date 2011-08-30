/**
 * @Package: factory.parser
 * @Title: RTFParser.java
 * @Author: zhangzuoqiang
 * @Time: 6:10:26 PM Aug 29, 2011
 * @Version: 
 */
package factory.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import exception.ParseException;
import exception.Token;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class RTFParser implements RTFParserConstants {

	RTFGroup current_group = null;
	List<RTFGroup> groups = new ArrayList<RTFGroup>();
	String characterSet = "";

	public List<RTFGroup> getGroups() {
		return groups;
	}

	public String getHTML() {
		StringBuilder sb = new StringBuilder();

		for (RTFGroup group : groups) {
			if (!group.isEmptyText()) {
				String content = group.getTextContent();
				sb.append(content);

				if (content.contains("<head>")) {
					/*
					 * if( !characterSet.isEmpty() ) { sb.append(
					 * "\n<meta http-equiv=\"Content-Type\" content=\"text/html charset="
					 * ); sb.append(codePage2Iso(characterSet));
					 * sb.append("\">"); }
					 */
				}
			}
		}

		return sb.toString();
	}

	String codePage2Iso(String codepage) {
		if (codepage.equals("1252")) {
			return "iso-8859-1";
		}
		return codepage;
	}

	/*
	 * <VALUE> TOKEN: { < STRING: (~["\\","\n","}","{"])*>:DEFAULT }
	 */
	final public void parse() throws ParseException {
		group();
		label_1: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case C_GROUP_START:
				;
				break;
			default:
				jj_la1[0] = jj_gen;
				break label_1;
			}
			group();
		}
		jj_consume_token(0);
	}

	final public void group() throws ParseException {
		group_start();
		label_2: while (true) {
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case C_BRACE_OPEN:
			case C_BRACE_CLOSE:
			case C_NONBREAKING_SPACE:
			case C_OPTIONAL_HYPHEN:
			case C_NONBREAKING_HYPHEN:
			case C_GROUP_START:
			case C_BACKSLASH:
			case C_ESC_CHAR:
			case C_CODEPAGE:
			case C_COMMAND:
			case C_SKIP_NEXT:
			case STRING:
				;
				break;
			default:
				jj_la1[1] = jj_gen;
				break label_2;
			}
			switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
			case C_CODEPAGE:
				set_codepage();
				break;
			case C_COMMAND:
				command();
				break;
			case STRING:
				string_sequence();
				break;
			case C_SKIP_NEXT:
				jj_consume_token(C_SKIP_NEXT);
				break;
			case C_BRACE_OPEN:
				brace_open();
				break;
			case C_BRACE_CLOSE:
				brace_close();
				break;
			case C_ESC_CHAR:
				esc_special_char();
				break;
			case C_BACKSLASH:
				jj_consume_token(C_BACKSLASH);
				current_group.addTextContent("\\");
				break;
			case C_NONBREAKING_HYPHEN:
				jj_consume_token(C_NONBREAKING_HYPHEN);
				current_group.addTextContent("_");
				break;
			case C_OPTIONAL_HYPHEN:
				jj_consume_token(C_OPTIONAL_HYPHEN);
				current_group.addTextContent("-");
				break;
			case C_NONBREAKING_SPACE:
				jj_consume_token(C_NONBREAKING_SPACE);
				current_group.addTextContent("&nbsp;");
				break;
			case C_GROUP_START:
				group();
				break;
			default:
				jj_la1[2] = jj_gen;
				jj_consume_token(-1);
				throw new ParseException();
			}
		}
		group_end();
	}

	final public void esc_special_char() throws ParseException {
		Token esc_char = null;
		esc_char = jj_consume_token(C_ESC_CHAR);
		// System.out.println("found xxxxxxxxxxxxxxxxxxxxxxxxxx");
		current_group.addTextContent(ConvertCharset.convertCharacter(
				characterSet, esc_char.image.substring(2)));
	}

	final public void set_codepage() throws ParseException {
		Token codepage = null;
		codepage = jj_consume_token(C_CODEPAGE);
		// System.out.println("found xxxxxxxxxxxxxxxxxxxxxxxxxx");
		characterSet = codepage.image.substring(8);
	}

	final public void brace_open() throws ParseException {
		jj_consume_token(C_BRACE_OPEN);
		current_group.addTextContent("{");
	}

	final public void brace_close() throws ParseException {
		jj_consume_token(C_BRACE_CLOSE);
		current_group.addTextContent("}");
	}

	final public void string_sequence() throws ParseException {
		Token string = null;
		string = jj_consume_token(STRING);
		current_group.addTextContent(string.image);
	}

	final public void command() throws ParseException {
		Token command = null;
		command = jj_consume_token(C_COMMAND);
		current_group.addCommand(command.image);
	}

	final public void group_start() throws ParseException {
		jj_consume_token(C_GROUP_START);
		current_group = new RTFGroup();
		groups.add(current_group);
	}

	final public void group_end() throws ParseException {
		jj_consume_token(C_GROUP_END);
	}

	public RTFParserTokenManager token_source;
	SimpleCharStream jj_input_stream;
	public Token token, jj_nt;
	private int jj_ntk;
	private int jj_gen;
	final private int[] jj_la1 = new int[3];
	static private int[] jj_la1_0;
	static {
		jj_la1_0();
	}

	private static void jj_la1_0() {
		jj_la1_0 = new int[] { 0x800, 0x7efc0, 0x7efc0, };
	}

	public RTFParser(java.io.InputStream stream) {
		this(stream, null);
	}

	public RTFParser(java.io.InputStream stream, String encoding) {
		try {
			jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
		} catch (java.io.UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		token_source = new RTFParserTokenManager(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 3; i++) {
			jj_la1[i] = -1;
		}
	}

	public void ReInit(java.io.InputStream stream) {
		ReInit(stream, null);
	}

	public void ReInit(java.io.InputStream stream, String encoding) {
		try {
			jj_input_stream.ReInit(stream, encoding, 1, 1);
		} catch (java.io.UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		token_source.ReInit(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 3; i++)
			jj_la1[i] = -1;
	}

	public RTFParser(java.io.Reader stream) {
		jj_input_stream = new SimpleCharStream(stream, 1, 1);
		token_source = new RTFParserTokenManager(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 3; i++) {
			jj_la1[i] = -1;
		}
	}

	public void ReInit(java.io.Reader stream) {
		jj_input_stream.ReInit(stream, 1, 1);
		token_source.ReInit(jj_input_stream);
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 3; i++) {
			jj_la1[i] = -1;
		}
	}

	public RTFParser(RTFParserTokenManager tm) {
		token_source = tm;
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 3; i++) {
			jj_la1[i] = -1;
		}
	}

	public void ReInit(RTFParserTokenManager tm) {
		token_source = tm;
		token = new Token();
		jj_ntk = -1;
		jj_gen = 0;
		for (int i = 0; i < 3; i++) {
			jj_la1[i] = -1;
		}
	}

	final private Token jj_consume_token(int kind) throws ParseException {
		Token oldToken;
		if ((oldToken = token).next != null) {
			token = token.next;
		} else {
			token = token.next = token_source.getNextToken();
		}
		jj_ntk = -1;
		if (token.kind == kind) {
			jj_gen++;
			return token;
		}
		token = oldToken;
		jj_kind = kind;
		throw generateParseException();
	}

	final public Token getNextToken() {
		if (token.next != null) {
			token = token.next;
		} else {
			token = token.next = token_source.getNextToken();
		}
		jj_ntk = -1;
		jj_gen++;
		return token;
	}

	final public Token getToken(int index) {
		Token t = token;
		for (int i = 0; i < index; i++) {
			if (t.next != null) {
				t = t.next;
			} else {
				t = t.next = token_source.getNextToken();
			}
		}
		return t;
	}

	final private int jj_ntk() {
		if ((jj_nt = token.next) == null) {
			return (jj_ntk = (token.next = token_source.getNextToken()).kind);
		} else {
			return (jj_ntk = jj_nt.kind);
		}
	}

	private Vector<int[]> jj_expentries = new Vector<int[]>();
	private int[] jj_expentry;
	private int jj_kind = -1;

	public ParseException generateParseException() {
		jj_expentries.removeAllElements();
		boolean[] la1tokens = new boolean[19];
		for (int i = 0; i < 19; i++) {
			la1tokens[i] = false;
		}
		if (jj_kind >= 0) {
			la1tokens[jj_kind] = true;
			jj_kind = -1;
		}
		for (int i = 0; i < 3; i++) {
			if (jj_la1[i] == jj_gen) {
				for (int j = 0; j < 32; j++) {
					if ((jj_la1_0[i] & (1 << j)) != 0) {
						la1tokens[j] = true;
					}
				}
			}
		}
		for (int i = 0; i < 19; i++) {
			if (la1tokens[i]) {
				jj_expentry = new int[1];
				jj_expentry[0] = i;
				jj_expentries.addElement(jj_expentry);
			}
		}
		int[][] exptokseq = new int[jj_expentries.size()][];
		for (int i = 0; i < jj_expentries.size(); i++) {
			exptokseq[i] = (int[]) jj_expentries.elementAt(i);
		}
		return new ParseException(token, exptokseq, tokenImage);
	}

	final public void enable_tracing() {
	}

	final public void disable_tracing() {
	}

}