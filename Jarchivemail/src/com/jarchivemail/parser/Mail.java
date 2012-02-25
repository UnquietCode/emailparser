package com.jarchivemail.parser;

import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import com.jarchivemail.handler.LineReader;

public abstract class Mail implements IHeader, IBody {

	protected LineReader lineReader;

	protected String sender;
	protected String receiver;
	protected String subject;
	protected Date receiveDate;
	protected Date sendDate;
	protected String mainContentType;
	protected String mainContentEncoding;
	protected Vector<String> contentTypes;
	protected Vector<String> multipartBoundaries;
	protected Vector<String> contentEncodings;
	protected Vector<Integer> contentStarts;
	protected Vector<Integer> contentEnds;
	protected Vector<String> contentFileNames;

	public Mail(LineReader lr, int startLine) {
		this(lr, startLine, -1);
	}

	public Mail(LineReader lr, int startLine, int endLine) {

		this.lineReader = lr;

		contentTypes = new Vector<String>();
		contentEncodings = new Vector<String>();
		contentStarts = new Vector<Integer>();
		contentEnds = new Vector<Integer>();
		contentFileNames = new Vector<String>();

		multipartBoundaries = new Vector<String>();
	}

	public String getField(int startPos) {
		int fieldStart;
		String out = null;
		try {
			lineReader.setLineNr(startPos);
			out = lineReader.readLine();
			if ((fieldStart = out.indexOf(":")) >= 0) {
				out = out.substring(fieldStart + 1);
			}
			out = out.trim();
			String line;
			while ((line = lineReader.readLine()) != null
					&& (line.startsWith("\t") || line.startsWith(" "))) {
				out += line.trim();
			}
			lineReader.setLineNr(lineReader.getLineNr() - 1);
		} catch (IOException e) {
		}
		return out;
	}

	public LineReader getLineReader() {
		return lineReader;
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

}
