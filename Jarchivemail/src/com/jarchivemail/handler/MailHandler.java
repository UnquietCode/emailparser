package com.jarchivemail.handler;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;

import com.jarchivemail.text.LineReader;
import com.jarchivemail.utils.TextConverter;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-23
 */
public class MailHandler {

	private LineReader lr;

	private String sender;
	private String receiver;
	private String subject;
	private Date receiveDate;
	private Date sendDate;
	private String mainContentType;
	private String mainContentEncoding;
	private Vector<String> contentTypes;
	private Vector<String> multipartBoundaries;
	private Vector<String> contentEncodings;
	private Vector<Integer> contentStarts;
	private Vector<Integer> contentEnds;
	private Vector<String> contentFileNames;

	public MailHandler(LineReader lr, int startLine) {
		this(lr, startLine, -1);
	}

	public MailHandler(LineReader lr, int startLine, int endLine) {
		String line;

		String fromField = null;
		boolean fromSet = false;
		String toField = null;
		boolean toSet = false;
		String subjectField = null;
		boolean subjectSet = false;
		String receivedField = null;
		boolean receivedSet = false;
		String dateField = null;
		boolean dateSet = false;
		String mimeEncodingField = null;
		boolean mimeEncodingSet = false;
		String stdEncodingField = null;
		boolean stdEncodingSet = false;
		String contentTypeField = null;
		boolean mainContentTypeSet = false;
		String contentDispositionField = null;

		contentTypes = new Vector<String>();
		contentEncodings = new Vector<String>();
		contentStarts = new Vector<Integer>();
		contentEnds = new Vector<Integer>();
		contentFileNames = new Vector<String>();

		multipartBoundaries = new Vector<String>();

		boolean isHeader = true;
		boolean isAttachmentHeader = false;
		int curLine = startLine;

		this.lr = lr;

		try {
			lr.setLineNr(startLine);
			while ((line = lr.readLine()) != null) {
				if (line.regionMatches(false, 0, "From ", 0, 5)) {
					curLine--;
					break;
				}
				curLine = lr.getLineNr() - 1;
				if (endLine >= 0 && curLine > endLine) {
					curLine = endLine;
					break;
				}

				if (isHeader && line.length() == 0) {
					sendDate = extractTimeDate(dateField);
					receiveDate = extractTimeDate(receivedField);
					subject = TextConverter.convert(subjectField);
					sender = TextConverter
							.convert(extractSenderName(fromField));
					receiver = TextConverter
							.convert(extractSenderName(toField));
					if (stdEncodingSet) {
						stdEncodingField = stdEncodingField.trim();
						if (stdEncodingField.regionMatches(true, 0,
								"ENCODING: ", 0, 10)) {
							stdEncodingField = stdEncodingField.substring(10);
						}
						stdEncodingField = stdEncodingField.trim();
						StringTokenizer st = new StringTokenizer(
								stdEncodingField, ", ");
						curLine++;
						while (st.hasMoreTokens()) {
							String s = st.nextToken();
							int lineCount = 0;
							try {
								lineCount = Integer.parseInt(s);
								if (st.hasMoreTokens()) {
									contentStarts.addElement(new Integer(
											curLine));
									contentEnds.addElement(new Integer(curLine
											+ lineCount));
									s = st.nextToken();
									if (s.equals("UUENCODE")) {
										contentTypes.addElement(new String(
												"BINARY"));
										contentEncodings.addElement(new String(
												"UUENCODE"));
										lr.setLineNr(curLine);
										contentFileNames
												.addElement(extractUUFileName(lr
														.readLine()));
									} else {
										contentTypes.addElement(new String(
												"TEXT/PLAIN"));
										contentEncodings.addElement(new String(
												"7BIT"));
										contentFileNames.addElement(null);
									}
									curLine = curLine + 1 + lineCount;
								}
							} catch (NumberFormatException e) {
							}
						}
						curLine -= 2;
						break;
					}

					mainContentType = extractContentType(contentTypeField);
					if (mainContentType != null
							&& mainContentType.regionMatches(true, 0,
									"MULTIPART", 0, 9)) {
						multipartBoundaries
								.addElement(extractMultipartBoundary(contentTypeField));
					}
					mainContentEncoding = mimeEncodingField;
					isHeader = false;
				}

				if (isHeader) {
					if (!receivedSet) {
						if (line.regionMatches(true, 0, "RECEIVED: ", 0, 10)) {
							receivedField = getField(curLine);
							receivedSet = true;
						}
					}
					if (!dateSet) {
						if (line.regionMatches(true, 0, "DATE: ", 0, 6)) {
							dateField = getField(curLine);
							dateSet = true;
						}
					}
					if (!fromSet) {
						if (line.regionMatches(true, 0, "FROM: ", 0, 6)) {
							fromField = getField(curLine);
							fromSet = true;
						}
					}
					if (!toSet) {
						if (line.regionMatches(true, 0, "TO: ", 0, 4)) {
							toField = getField(curLine);
							toSet = true;
						}
					}
					if (!subjectSet) {
						if (line.regionMatches(true, 0, "SUBJECT: ", 0, 9)) {
							subjectField = getField(curLine);
							subjectSet = true;
						}
					}
					if (!mimeEncodingSet) {
						if (line.regionMatches(true, 0,
								"CONTENT-TRANSFER-ENCODING: ", 0, 27)) {
							mimeEncodingField = getField(curLine);
							mimeEncodingSet = true;
						}
					}
					if (!stdEncodingSet) {
						if (line.regionMatches(true, 0, "ENCODING: ", 0, 10)) {
							stdEncodingField = getField(curLine);
							stdEncodingSet = true;
						}
					}
					if (!mainContentTypeSet) {
						if (line.regionMatches(true, 0, "CONTENT-TYPE: ", 0, 14)) {
							contentTypeField = getField(curLine);
							mainContentTypeSet = true;
						}
					}
				} else {
					if (multipartBoundaries.size() == 0) {
						if (contentStarts.size() == 0) {
							contentStarts.addElement(new Integer(curLine + 1));
							contentTypes.addElement(mainContentType);
							contentEncodings.addElement(mainContentEncoding);
							contentFileNames.addElement(null);
						}
					} else {
						if (isAttachmentHeader) {
							if (line.length() == 0) {
								isAttachmentHeader = false;
								contentStarts.addElement(new Integer(
										curLine + 1));
								contentTypes
										.addElement(extractContentType(contentTypeField));
								contentEncodings.addElement(mimeEncodingField);
								String fileName = extractFileName(contentDispositionField);
								if (fileName == null)
									fileName = extractFileName(contentTypeField);
								contentFileNames.addElement(TextConverter
										.convert(fileName));
								if (contentTypes.lastElement() != null
										&& (contentTypes.lastElement())
												.regionMatches(true, 0,
														"MULTIPART", 0, 9)) {
									multipartBoundaries
											.insertElementAt(
													extractMultipartBoundary(contentTypeField),
													0);
								}
							} else {
								if (line.regionMatches(true, 0,
										"CONTENT-TYPE: ", 0, 14)) {
									contentTypeField = getField(curLine);
								}
								if (line.regionMatches(true, 0,
										"CONTENT-TRANSFER-ENCODING: ", 0, 27)) {
									mimeEncodingField = getField(curLine);
								}
								if (line.regionMatches(true, 0,
										"CONTENT-DISPOSITION: ", 0, 21)) {
									contentDispositionField = getField(curLine);
								}
							}
						} else {
							if (line.startsWith("--")) {
								if (line.equals("--"
										+ multipartBoundaries.elementAt(0))) {
									if (contentStarts.size() > contentEnds
											.size()) {
										contentEnds.addElement(new Integer(
												curLine - 1));
									}
									isAttachmentHeader = true;
									mimeEncodingField = null;
									contentTypeField = null;
									contentDispositionField = null;
								}
								if (line.equals("--"
										+ multipartBoundaries.elementAt(0)
										+ "--")) {
									contentEnds.addElement(new Integer(
											curLine - 1));
									multipartBoundaries.removeElementAt(0);
								}
							}
						}
					}
				}
			}

			if (contentStarts.size() > contentEnds.size()) {
				contentEnds.addElement(new Integer(curLine));
			}
			lr.setLineNr(curLine + 1);
		} catch (IOException e) {
		}
	}

	public String getSender() {
		return sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public String getSubject() {
		return subject;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public int[] getContentStarts() {
		int[] n;

		n = new int[contentStarts.size()];
		for (int i = 0; i < n.length; i++) {
			n[i] = (contentStarts.elementAt(i)).intValue();
		}
		return n;
	}

	public int[] getContentEnds() {
		int[] n;

		n = new int[contentEnds.size()];
		for (int i = 0; i < n.length; i++) {
			n[i] = (contentEnds.elementAt(i)).intValue();
		}
		return n;
	}

	public String[] getContentTypes() {
		String[] s;

		s = new String[contentTypes.size()];
		for (int i = 0; i < s.length; i++) {
			s[i] = contentTypes.elementAt(i);
		}
		return s;
	}

	public String[] getContentEncodings() {
		String[] s;

		s = new String[contentEncodings.size()];
		for (int i = 0; i < s.length; i++) {
			s[i] = contentEncodings.elementAt(i);
		}
		return s;
	}

	public String[] getContentFileNames() {
		String[] s;

		s = new String[contentFileNames.size()];
		for (int i = 0; i < s.length; i++) {
			s[i] = contentFileNames.elementAt(i);
		}
		return s;
	}

	private String getField(int startPos) {
		int fieldStart;
		String out = null;
		try {
			lr.setLineNr(startPos);
			out = lr.readLine();
			if ((fieldStart = out.indexOf(":")) >= 0) {
				out = out.substring(fieldStart + 1);
			}
			out = out.trim();
			String line;
			while ((line = lr.readLine()) != null
					&& (line.startsWith("\t") || line.startsWith(" "))) {
				out += line.trim();
			}
			lr.setLineNr(lr.getLineNr() - 1);
		} catch (IOException e) {
		}
		return out;
	}

	private String extractSenderName(String s) {
		String senderName = null;
		int startIndex, endIndex;

		if (s == null || s.equals("")) {
			return null;
		}

		s = s.trim();
		if (s.regionMatches(true, 0, "FROM: ", 0, 6)) {
			s = s.substring(6);
		}
		senderName = s.trim();
		if ((startIndex = s.indexOf('<')) >= 0
				&& (endIndex = s.indexOf('>')) >= 0) {
			senderName = s.substring(0, startIndex).trim();
			if (senderName.startsWith("\"")) {
				senderName = senderName.substring(1);
			}
			if (senderName.endsWith("\"")) {
				senderName = senderName.substring(0, senderName.length() - 1);
			}
			if (senderName.equals("")) {
				senderName = s.substring(startIndex + 1, endIndex);
			}
		} else if ((startIndex = s.indexOf('(')) >= 0
				&& (endIndex = s.indexOf(')')) >= 0) {
			senderName = s.substring(startIndex + 1, endIndex);
		}
		return senderName;
	}

	private Date extractTimeDate(String s) {
		if (s == null || s.length() == 0) {
			return null;
		}
		int startIndex = s.lastIndexOf(';');
		if (startIndex > -1) {
			s = s.substring(startIndex + 1);
		} else {
			if (s.toUpperCase().startsWith("DATE: ")) {
				s = s.substring(6);
			}
		}

		StringTokenizer st = new StringTokenizer(s, "\t ,");
		int date = 0;
		int month = 0;
		int year = 0;
		int hour = 0;
		int minute = 0;
		int second = 0;
		TimeZone tz;

		String token;

		if (!st.hasMoreTokens()) {
			return null;
		}
		token = st.nextToken();
		try {
			date = Integer.parseInt(token);
		} catch (NumberFormatException e) {
			if (!st.hasMoreElements()) {
				return null;
			}
			token = st.nextToken();
			try {
				date = Integer.parseInt(token);
			} catch (NumberFormatException e2) {
				return null;
			}
		}

		if (!st.hasMoreTokens()) {
			return null;
		}
		token = st.nextToken().toUpperCase();
		if (token.startsWith("JAN")) {
			month = 0;
		} else if (token.startsWith("FEB")) {
			month = 1;
		} else if (token.startsWith("MAR")) {
			month = 2;
		} else if (token.startsWith("APR")) {
			month = 3;
		} else if (token.startsWith("MAY")) {
			month = 4;
		} else if (token.startsWith("JUN")) {
			month = 5;
		} else if (token.startsWith("JUL")) {
			month = 6;
		} else if (token.startsWith("AUG")) {
			month = 7;
		} else if (token.startsWith("SEP")) {
			month = 8;
		} else if (token.startsWith("OCT")) {
			month = 9;
		} else if (token.startsWith("NOV")) {
			month = 10;
		} else if (token.startsWith("DEC")) {
			month = 11;
		} else {
			return null;
		}

		if (!st.hasMoreTokens()) {
			return null;
		}
		token = st.nextToken();
		try {
			year = Integer.parseInt(token);
		} catch (NumberFormatException e) {
			return null;
		}
		if (year < 100) {
			if (year < 70) {
				year += 2000;
			} else {
				year += 1900;
			}
		}
		if (year > 3000) {
			return null;
		}

		if (!st.hasMoreTokens()) {
			return null;
		}
		token = st.nextToken();
		StringTokenizer timeSt = new StringTokenizer(token, ":");
		if (!timeSt.hasMoreTokens()) {
			return null;
		}
		token = timeSt.nextToken();
		try {
			hour = Integer.parseInt(token);
		} catch (NumberFormatException e) {
			return null;
		}
		if (!timeSt.hasMoreTokens()) {
			return null;
		}
		token = timeSt.nextToken();
		try {
			minute = Integer.parseInt(token);
		} catch (NumberFormatException e) {
			return null;
		}
		if (timeSt.hasMoreTokens()) {
			token = timeSt.nextToken();
			try {
				second = Integer.parseInt(token);
			} catch (NumberFormatException e) {
				second = 0;
			}
		}

		if (st.hasMoreTokens()) {
			token = "GMT" + st.nextToken();
			tz = TimeZone.getTimeZone(token);
		} else {
			tz = TimeZone.getDefault();
		}

		Calendar cal = Calendar.getInstance();

		cal.set(year, month, date, hour, minute, second);
		cal.setTimeZone(tz);

		if (cal.getTimeInMillis() < 0) {
			return null;
		}

		return cal.getTime();
	}

	private String extractMultipartBoundary(String s) {
		if (s == null || s.equals("")) {
			return null;
		}

		int startIndex = s.lastIndexOf(';');
		if (startIndex > -1) {
			s = s.substring(startIndex + 1);
			s = s.trim();
			if (s.regionMatches(true, 0, "BOUNDARY=", 0, 9)) {
				s = s.substring(9);
				s = s.replace('"', ' ');
				s = s.trim();
			}
		} else {
			return null;
		}
		return s;
	}

	private String extractContentType(String s) {
		if (s == null || s.equals("")) {
			return null;
		}

		s = s.trim();

		if (s.regionMatches(true, 0, "CONTENT-TYPE: ", 0, 14)) {
			s = s.substring(14);
			s = s.trim();
		}

		int endIndex = s.indexOf(';');
		if (endIndex > -1) {
			s = s.substring(0, endIndex);
			s = s.trim();
		}
		return s;
	}

	private String extractFileName(String s) {
		if (s == null || s.equals("")) {
			return null;
		}

		int startIndex = s.lastIndexOf(';');
		if (startIndex > -1) {
			s = s.substring(startIndex + 1);
			s = s.trim();
			if (s.regionMatches(true, 0, "FILENAME=", 0, 9)) {
				s = s.substring(9);
				s = s.replace('"', ' ');
				s = s.trim();
			} else if (s.regionMatches(true, 0, "NAME=", 0, 5)) {
				s = s.substring(5);
				s = s.replace('"', ' ');
				s = s.trim();
			} else {
				return null;
			}
		} else {
			return null;
		}
		return s;
	}

	private String extractUUFileName(String s) {
		if (s == null || s.equals("")) {
			return null;
		}

		StringTokenizer st = new StringTokenizer(s, " ");
		if (st.countTokens() < 3) {
			return null;
		}
		st.nextToken();
		st.nextToken();
		return st.nextToken("");
	}
}
