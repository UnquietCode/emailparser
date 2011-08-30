/**
 * @Package: factory.parser
 * @Title: HtmlFromRtf.java
 * @Author: zhangzuoqiang
 * @Time: 6:19:19 PM Aug 29, 2011
 * @Version: 
 */
package factory.parser;

import java.io.StringReader;

import exception.ParseException;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class HtmlFromRtf {

	private static final String HTML_START = "{\\*\\htmltag";
	final String htmlText;

	public HtmlFromRtf(String bodyText) throws ParseException {
		htmlText = extractHtml(bodyText);
	}

	public String getHTML() {
		return htmlText;
	}

	/*
	 * 
	 * The Code looks like this:
	 * 
	 * {\rtf1\ansi\ansicpg1252\fromhtml1 \deff0{\fonttbl {\f0\fswiss Arial;}
	 * {\f1\fmodern Courier New;} {\f2\fnil\fcharset2 Symbol;}
	 * {\f3\fmodern\fcharset0 Courier New;} {\f4\fswiss\fcharset0 Arial;}}
	 * {\colortbl\red0\green0\blue0;\red0\green0\blue255;} {\*\htmltag19 <html
	 * xmlns:o="urn:schemas-microsoft-com:office:office"
	 * xmlns:w="urn:schemas-microsoft-com:office:word"
	 * xmlns="http://www.w3.org/TR/REC-html40">} {\*\htmltag2 \par }
	 * {\*\htmltag2 \par } {\*\htmltag34 <head>} {\*\htmltag1 \par }
	 * {\*\htmltag1 \par } {\*\htmltag161 <meta name=Generator
	 * content="Microsoft Word 11 (filtered medium)">} {\*\htmltag1 \par }
	 * {\*\htmltag241 <style>}
	 * 
	 * So we will try to remove all leading { and } and removing all \htmltag
	 * and hoping the best
	 */
	private String extractHtml(String rtf) throws ParseException {
		RTFParser parser = new RTFParser(new StringReader(rtf));
		parser.parse();
		return parser.getHTML();
	}

	/**
	 * @return the htmlStart
	 */
	public static String getHtmlStart() {
		return HTML_START;
	}
}