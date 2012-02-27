package com.eams.mboxparser.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-23
 */
public class HTMLParserUtil implements Enumeration<Object> {

	private String htmlSource;
	private String lowerCaseHtmlSource;
	private int enumPosition;
	private String enumTag;
	private String plainText = null;

	private final static int OPTIONS_OR = 1;
	private final static int OPTIONS_AND = 2;
	private final static int OPTIONS_PHRASE = 4;
	private final static int OPTIONS_WORDS = 8;
	private final static int OPTIONS_CASE = 16;

	private final String org[] = { "ä", "ö", "ü", "Ä", "Ö", "Ü", "ß", "&",
			"\"", "<", ">", " " };
	private final String rep[] = { "&auml;", "&ouml;", "&uuml;", "&Auml;",
			"&Ouml;", "&Uuml;", "&szlig;", "&amp;", "&quot;", "&lt;", "&gt;",
			"&nbsp;" };
	private final String delimiter = " .,;:()!?\t\n\r";
	private final String tagDelimiter = " \t<>";

	public HTMLParserUtil(URL url) throws IOException {
		InputStream is;
		BufferedReader br;
		String line;
		StringBuffer sb;

		is = url.openStream();
		br = new BufferedReader(new InputStreamReader(is));
		sb = new StringBuffer();
		while ((line = br.readLine()) != null) {
			sb.append(line + '\n');
		}
		br.close();
		htmlSource = sb.toString();
		lowerCaseHtmlSource = htmlSource.toLowerCase();
	}

	public HTMLParserUtil(File f) throws IOException {
		BufferedReader br;
		String line;
		StringBuffer sb;

		br = new BufferedReader(new FileReader(f));
		sb = new StringBuffer();
		while ((line = br.readLine()) != null) {
			sb.append(line + '\n');
		}
		br.close();
		htmlSource = sb.toString();
		lowerCaseHtmlSource = htmlSource.toLowerCase();
	}

	public HTMLParserUtil(String htmlSource) {
		this.htmlSource = htmlSource;
		lowerCaseHtmlSource = htmlSource.toLowerCase();
	}

	private boolean isWord(String text, int startIndex, int endIndex) {
		startIndex--;
		if (((startIndex == -1) || (delimiter.indexOf(text.charAt(startIndex)) != -1))
				&& ((endIndex == text.length()) || (delimiter.indexOf(text
						.charAt(endIndex)) != -1))) {
			return true;
		} else {
			return false;
		}
	}

	public boolean fullTextSearch(String searchText, int options) {
		StringTokenizer st;
		String curWord;
		String text;
		int startIndex, endIndex;
		boolean result;

		if (plainText == null) {
			plainText = getPlainText(getTagContent("body"));
		}

		text = plainText;

		if ((options & OPTIONS_CASE) == 0) {
			text = text.toLowerCase();
			searchText = searchText.toLowerCase();
		}

		if ((options & OPTIONS_PHRASE) > 0) {
			st = new StringTokenizer(searchText, "");
		} else {
			st = new StringTokenizer(searchText);
		}

		if ((options & OPTIONS_AND) > 0) {
			result = true;
		} else {
			result = false;
		}

		while (st.hasMoreTokens()) {
			curWord = st.nextToken();
			if ((startIndex = text.indexOf(curWord)) == -1) {
				result = false;
			} else {
				if ((options & OPTIONS_WORDS) > 0) {
					result = false;
					while (!result
							&& (startIndex = text.indexOf(curWord, startIndex)) != -1) {
						endIndex = startIndex + curWord.length();
						result = isWord(text, startIndex, endIndex);
						startIndex = endIndex;
					}
				} else {
					result = true;
				}
			}
			if (!result && (options & OPTIONS_AND) > 0) {
				break;
			}
			if (result && (options & OPTIONS_OR) > 0) {
				break;
			}
		}
		return result;
	}

	public String getTagContent(String tag, int fromPosition) {
		int startIndex, endIndex = 0;
		String content = "";

		startIndex = getTagPosition(tag, fromPosition);
		endIndex = getTagPosition("/" + getTagName(tag), startIndex);

		if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
			content = htmlSource.substring(
					startIndex + getTag(startIndex).length(), endIndex).trim();
		}
		return content;
	}

	public String getTagContent(String tag) {
		return getTagContent(tag, 0);
	}

	public String getTagContent(int fromPosition) {
		return getTagContent(getTag(fromPosition), fromPosition);
	}

	public boolean isTag(int fromPosition) {
		return getTag(fromPosition) != "";
	}

	public String getTag(int fromPosition) {
		int startIndex, endIndex;

		startIndex = fromPosition;
		endIndex = fromPosition;

		while (startIndex > 0 && htmlSource.charAt(startIndex) != '<') {
			startIndex--;
			if (htmlSource.charAt(startIndex) == '>') {
				return "";
			}
		}
		while (endIndex < htmlSource.length() - 1
				&& htmlSource.charAt(endIndex) != '>') {
			endIndex++;
			if (htmlSource.charAt(endIndex) == '<') {
				return "";
			}
		}

		if (htmlSource.charAt(startIndex) == '<'
				&& htmlSource.charAt(endIndex) == '>') {
			return htmlSource.substring(startIndex, endIndex + 1);
		}
		return "";
	}

	public String getTagName(String tag) {
		StringTokenizer st;

		st = new StringTokenizer(tag, "<> \t");

		if (st.hasMoreTokens()) {
			return (st.nextToken());
		}
		return "";
	}

	public String getTagName(int fromPosition) {
		return getTagName(getTag(fromPosition));
	}

	public int getTagPosition(String tag, int fromPosition) {
		int index = 0;
		String s;

		index = fromPosition;
		tag = tag.toLowerCase();

		while ((index = lowerCaseHtmlSource.indexOf(tag, index)) != -1) {
			s = getTag(index).toLowerCase();
			if (getTagName(s).equals(getTagName(tag))) {
				index -= s.indexOf(tag);
				return (index);
			}
			index++;
		}
		return -1;
	}

	public int getTagPosition(String tag) {
		return getTagPosition(tag, 0);
	}

	public String getKeyValue(String key, int fromPosition) {
		String tag;
		int index;

		tag = getTag(fromPosition);

		index = tag.toLowerCase().indexOf(key.toLowerCase());
		if (index < 1 || tagDelimiter.indexOf(tag.charAt(index - 1)) == -1) {
			return "";
		}

		tag = tag.substring(index + key.length()).trim();

		if (tag.length() == 0 || tag.charAt(0) != '=') {
			return "";
		}

		tag = tag.substring(1).trim();

		if (tag.length() == 0) {
			return "";
		}

		if (tag.charAt(0) == '"') {
			tag = tag.substring(1).trim();
			if ((index = tag.indexOf('"')) == -1) {
				index = tag.length();
			}
		} else {
			index = 0;
			while (index < tag.length() - 1
					&& tagDelimiter.indexOf(tag.charAt(index)) == -1) {
				index++;
			}
		}

		tag = tag.substring(0, index);
		return tag;
	}

	public String getPlainText(String htmlSource) {
		int startIndex = 0, endIndex;
		StringBuffer sb;

		sb = new StringBuffer();

		while ((endIndex = htmlSource.indexOf('<', startIndex)) != -1) {
			sb.append(htmlSource.substring(startIndex, endIndex));
			if ((startIndex = htmlSource.indexOf('>', endIndex) + 1) == 0) {
				startIndex = htmlSource.length();
			}
		}

		if (startIndex < htmlSource.length()) {
			sb.append(htmlSource.substring(startIndex));
		}

		return namedCharToChar(sb.toString());
	}

	public String getPlainText() {
		return getPlainText(htmlSource);
	}

	public String getHTML() {
		return htmlSource;
	}

	public String charToNamedChar(String text) {
		int startIndex, endIndex;
		StringBuffer sb;
		int i;

		sb = new StringBuffer();

		for (i = 0; i < org.length; i++) {
			startIndex = 0;
			while ((endIndex = text.indexOf(org[i], startIndex)) != -1) {
				sb.append(text.substring(startIndex, endIndex));
				sb.append(rep[i]);
				startIndex = endIndex + org[i].length();
			}
			if (startIndex < text.length()) {
				sb.append(text.substring(startIndex));
			}
			text = sb.toString();
			sb.setLength(0);
		}
		return text;
	}

	public String namedCharToChar(String text) {
		int startIndex, endIndex;
		StringBuffer sb;
		int i;

		sb = new StringBuffer();
		for (i = 0; i < org.length; i++) {
			startIndex = 0;
			while ((endIndex = text.indexOf(rep[i], startIndex)) != -1) {
				sb.append(text.substring(startIndex, endIndex));
				sb.append(org[i]);
				startIndex = endIndex + rep[i].length();
			}
			if (startIndex < text.length()) {
				sb.append(text.substring(startIndex));
			}
			text = sb.toString();
			sb.setLength(0);
		}
		return text;
	}

	public Enumeration<?> tagPositions(String tag) {
		enumPosition = 0;
		enumTag = tag;
		return this;
	}

	public boolean hasMoreElements() {
		return getTagPosition(enumTag, enumPosition) != -1;
	}

	public Object nextElement() {
		int index;

		index = getTagPosition(enumTag, enumPosition);
		if (index == -1) {
			throw new NoSuchElementException();
		}
		enumPosition = index + getTag(index).length();
		return new Integer(index);
	}
}