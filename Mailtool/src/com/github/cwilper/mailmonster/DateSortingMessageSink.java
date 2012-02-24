package com.github.cwilper.mailmonster;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.github.cwilper.ttff.AbstractSink;

public class DateSortingMessageSink extends AbstractSink<Message> {

	private final File outputDir;

	public DateSortingMessageSink(File outputDir) {
		this.outputDir = outputDir;
	}

	@Override
	public void put(Message message) throws IOException {
		appendMessage(getFile(message.getHeader().getDate()), message);
	}

	private void appendMessage(File file, Message message) {
		try {
			if (!file.exists()) {
				file.getParentFile().mkdirs();
			}
			PrintWriter writer = new PrintWriter(new FileOutputStream(file,
					true));
			try {
				writeMessage(writer, message);
			} finally {
				writer.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void writeMessage(PrintWriter writer, Message message)
			throws Exception {
		writer.print(message.getFromLine() + "\n");
		writer.print(message.getHeader().getRaw() + "\n");
		message.writeBody(writer);
		writer.print("\n");
	}

	private File getFile(Date date) {
		String yearString = "0000";
		String monthString = "00";
		if (date != null) {
			// need year and month
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			cal.setTime(date);
			int year = cal.get(Calendar.YEAR);
			yearString = "" + year;
			int month = cal.get(Calendar.MONTH) + 1;
			monthString = "" + month;
			if (monthString.length() < 2) {
				monthString = "0" + monthString;
			}
		}
		return new File(outputDir, yearString + "/" + yearString + "-"
				+ monthString + ".mbox");
	}

}
