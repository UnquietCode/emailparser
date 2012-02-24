package com.github.cwilper.mailmonster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Header {

	private final String raw;

	private final List<Field> fields;

	public Header(String raw) {
		this.raw = raw;
		this.fields = parse(raw);
	}

	public List<Field> getFields() {
		return fields;
	}

	public List<String> getValues(String name) {
		List<String> values = new ArrayList<String>();
		for (Field field : getFields()) {
			if (field.getName().equalsIgnoreCase(name)) {
				values.add(field.getValue());
			}
		}
		return values;
	}

	public String getValue(String name) {
		for (Field field : getFields()) {
			if (field.getName().equalsIgnoreCase(name)) {
				return field.getValue();
			}
		}
		return null;
	}

	public Date getDate() {
		return DateParser.parse(getValue("Date"));
	}

	private List<Field> parse(String raw) {
		try {
			List<Field> list = new ArrayList<Field>();
			BufferedReader reader = new BufferedReader(new StringReader(raw));
			String name = null;
			StringBuilder value = null;
			String line = reader.readLine();
			while (line != null) {
				if (name != null
						&& (line.startsWith(" ") || line.startsWith("\t"))) {
					value.append(" ");
					value.append(line.trim());
				} else {
					if (name != null) {
						list.add(new Field(name, value.toString()));
					}
					value = new StringBuilder();
					int i = line.indexOf(":");
					if (i == -1) {
						throw new RuntimeException("Delimiter not found: "
								+ line);
					}
					name = line.substring(0, i);
					value.append(line.substring(i + 1).trim());
				}
				line = reader.readLine();
			}
			if (name != null && value.length() > 0) {
				list.add(new Field(name, value.toString()));
			}
			return list;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String getRaw() {
		return raw;
	}

}
