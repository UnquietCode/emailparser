package com.github.cwilper.mailmonster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;

import com.github.cwilper.ttff.AbstractSource;

public class MBoxStreamMessageSource extends AbstractSource<Message> {

	private final BufferedReader reader;

	private MBoxMessage latestMessage;

	public MBoxStreamMessageSource(InputStream in) {
		try {
			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		} catch (UnsupportedEncodingException wontHappen) {
			throw new RuntimeException(wontHappen);
		}
	}

	@Override
	protected Message computeNext() throws IOException {
		// populate fromLine and position at start of header
		String fromLine;
		if (latestMessage != null) {
			fromLine = latestMessage.getNextFromLine();
		} else {
			fromLine = "";
			while (fromLine != null && !fromLine.startsWith("From ")) {
				fromLine = reader.readLine();
			}
		}
		if (fromLine == null) {
			// no more fromLines; end of mbox
			return endOfData();
		}
		// consume header, then construct MBoxMessage
		StringBuilder rawHeader = new StringBuilder();
		String line = reader.readLine();
		while (line != null) {
			if (line.length() != 0) {
				rawHeader.append(line + "\n");
				line = reader.readLine();
			} else {
				latestMessage = new MBoxMessage(fromLine, new Header(
						rawHeader.toString()), reader);
				return latestMessage;
			}
		}
		// end of mbox encountered while reading header
		return endOfData();
	}

	@Override
	public void close() {
		IOUtils.closeQuietly(reader);
	}

}
