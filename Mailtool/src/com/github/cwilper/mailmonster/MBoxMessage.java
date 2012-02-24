package com.github.cwilper.mailmonster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MBoxMessage implements Message {

	private final DateFormat ascTime = new SimpleDateFormat(
			"EEE MMM d HH:mm:ss yyyy");

	private final String fromLine;
	private final Header header;
	private final BufferedReader reader;

	private String nextFromLine;
	private boolean exhausted;

	public MBoxMessage(String fromLine, Header header, BufferedReader reader) {
		this.fromLine = fromLine;
		this.header = header;
		this.reader = reader;
	}

	public String getFromLine() {
		return fromLine;
	}

	public Header getHeader() {
		return header;
	}

	public void writeBody(PrintWriter writer) throws IOException {
		if (!bodyWritten()) {
			boolean readyForFrom = true;
			String prevLine = null;
			String line = reader.readLine();
			if (line == null) {
				exhausted = true;
			}
			while (!bodyWritten()) {
				if (readyForFrom && isFrom(line)) {
					nextFromLine = line;
				} else {
					if (line.length() == 0) {
						readyForFrom = true;
					} else {
						readyForFrom = false;
					}
					if (prevLine != null) {
						if (writer != null) {
							writer.print(prevLine + "\n");
						}
					}
					prevLine = line;
					line = reader.readLine();
					if (line == null) {
						if (writer != null && !readyForFrom) {
							writer.print(prevLine + "\n");
						}
						exhausted = true;
					}
				}
			}
		}
	}

	private boolean isFrom(String line) {
		if (!line.startsWith("From ")) {
			return false;
		}
		// does the remainder look like one of:
		// envsender date
		// envsender date moreinfo
		// where:
		// envsender is one word
		// date is 24 chars in asctime format
		// moreinfo is an arbitrary string
		String afterFrom = line.substring(5);
		int i = afterFrom.indexOf(" ");
		if (i == -1) {
			return false;
		}
		String afterEnvSender = afterFrom.substring(i + 1);
		return afterEnvSender.length() >= 24
				&& isAscTime(afterEnvSender.substring(0, 24));
	}

	private boolean isAscTime(String s) {
		try {
			ascTime.parse(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean bodyWritten() {
		return exhausted || nextFromLine != null;
	}

	public String getNextFromLine() throws IOException {
		writeBody(null);
		return nextFromLine;
	}

}
