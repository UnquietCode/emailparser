package com.github.cwilper.mailmonster;

import java.io.IOException;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

final class DateParser {

	private DateParser() {
	}

	private static String[] MONTHS = { "Jan", "Feb", "Mar", "Apr", "May",
			"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	public static Date parse(String line) {
		if (line == null || line.length() == 0) {
			return null;
		}
		StringReader reader = new StringReader(line.trim());
		int day = consumeInt(reader);
		if (day == -1) {
			// System.err.println("Unparsable Date (day?): " + line);
			return null;
		}
		int month = consumeMonth(reader);
		if (month == -1) {
			// System.err.println("Unparsable Date (month?): " + line);
			return null;
		}
		int year = consumeInt(reader);
		if (year == -1) {
			// System.err.println("Unparsable Date (year?): " + line);
			return null;
		}
		if (year < 100) {
			if (year < 70) {
				year = year + 2000;
			} else {
				year = year + 1900;
			}
			// System.err.println("Two-digit year in Date (guessing " + year +
			// "): " + line);
		}
		String timeString = consumeToken(reader);
		int hh = 0;
		int mm = 0;
		int ss = 0;
		TimeZone tz = TimeZone.getTimeZone("GMT");
		if (timeString == null) {
			// System.err.println("Time not given in Date (assuming 00:00:00 GMT): "
			// + line);
		} else {
			StringReader timeReader = new StringReader(timeString);
			hh = consumeInt(timeReader);
			if (hh == -1) {
				// System.err.println("Unparsable hours (assuming 00:00:00): " +
				// line);
				hh = 0;
			} else {
				mm = consumeInt(timeReader);
				if (mm == -1) {
					// System.err.println("Unparsable minutes (assuming " + hh +
					// ":00:00): " + line);
					mm = 0;
				} else {
					ss = consumeInt(timeReader);
					if (ss == -1) {
						// System.err.println("Unparsable seconds (assuming " +
						// hh + ":" + mm + ":00): " + line);
						ss = 0;
					}
				}
			}
			tz = consumeTimeZone(reader);
			if (tz == null) {
				// System.err.println("Unparsable timezone (assuming GMT): " +
				// line);
				tz = TimeZone.getTimeZone("GMT");
			}
		}
		Calendar cal = Calendar.getInstance(tz);
		cal.set(year, month - 1, day, hh, mm, ss);
		return cal.getTime();
		/*
		 * Date date = cal.getTime(); DateFormat df = new
		 * SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		 * df.setTimeZone(TimeZone.getTimeZone("UTC")); System.out.println(year
		 * + "/" + month + "/" + day + " " + hh + " " + mm + " " + ss + " " +
		 * tz.getID() + " => " + df.format(date));
		 */
	}

	// return -1 if no match/unparsable
	private static int consumeMonth(StringReader reader) {
		String abbrev = consumeLetters(reader, 3);
		if (abbrev == null) {
			return -1;
		} else {
			for (int i = 0; i < MONTHS.length; i++) {
				if (abbrev.equalsIgnoreCase(MONTHS[i])) {
					return i + 1;
				}
			}
			return -1;
		}
	}

	private static TimeZone consumeTimeZone(StringReader reader) {
		String token = consumeToken(reader);
		if (token == null) {
			return null;
		}
		char c = token.charAt(0);
		if (c == '+' || c == '-') {
			return TimeZone.getTimeZone("GMT" + token);
		} else {
			String t = token.toUpperCase();
			if (t.equals("ADT")) {
				return TimeZone.getTimeZone("GMT-03:00");
			} else if (t.equals("AST")) {
				return TimeZone.getTimeZone("GMT-04:00");
			} else if (t.equals("EDT")) {
				return TimeZone.getTimeZone("GMT-04:00");
			} else if (t.equals("EST")) {
				return TimeZone.getTimeZone("GMT-05:00");
			} else if (t.equals("CDT")) {
				return TimeZone.getTimeZone("GMT-05:00");
			} else if (t.equals("CST")) {
				return TimeZone.getTimeZone("GMT-06:00");
			} else if (t.equals("MDT")) {
				return TimeZone.getTimeZone("GMT-06:00");
			} else if (t.equals("MST")) {
				return TimeZone.getTimeZone("GMT-07:00");
			} else if (t.equals("PDT")) {
				return TimeZone.getTimeZone("GMT-07:00");
			} else if (t.equals("PST")) {
				return TimeZone.getTimeZone("GMT-08:00");
			} else if (t.equals("GMT") || t.equals("UT") || t.equals("UTC")) {
				return TimeZone.getTimeZone("GMT");
			} else {
				// System.err.println("NOTE: Getting timezone from java '" +
				// token + "'");
				return TimeZone.getTimeZone(token);
			}
		}
	}

	private static String consumeToken(StringReader reader) {
		StringBuilder chars = new StringBuilder();
		try {
			int c = reader.read();
			while (c != -1 && c == ' ') {
				c = reader.read();
			}
			if (c != -1) {
				chars.append((char) c);
				c = reader.read();
				while (c != -1 && c != ' ') {
					chars.append((char) c);
					c = reader.read();
				}
				return chars.toString();
			} else {
				return null;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static String consumeLetters(StringReader reader, int maxLetters) {
		StringBuilder letters = new StringBuilder(maxLetters);
		try {
			int c = reader.read();
			while (c != -1 && !isLetter(c)) {
				c = reader.read();
			}
			if (c != -1) {
				letters.append((char) c);
				c = reader.read();
				while (c != -1 && isLetter(c) && letters.length() < maxLetters) {
					letters.append((char) c);
					c = reader.read();
				}
				return letters.toString();
			} else {
				return null;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static boolean isLetter(int c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}

	// scan till we get digits, start capturing them until
	// we see a non-digit or EOF, and return the captured
	// digits as an int. if we saw EOF before any digits,
	// return -1
	private static int consumeInt(StringReader reader) {
		StringBuilder digits = new StringBuilder();
		try {
			int c = reader.read();
			while (c != -1 && (c < '0' || c > '9')) {
				c = reader.read();
			}
			if (c != -1) {
				digits.append((char) c);
				c = reader.read();
				while (c != -1 && (c >= '0' && c <= '9')) {
					digits.append((char) c);
					c = reader.read();
				}
				return Integer.parseInt(digits.toString());
			} else {
				return -1;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
