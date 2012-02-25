package com.jarchivemail.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.jarchivemail.handler.LineReader;
import com.jarchivemail.utils.Base64;
import com.jarchivemail.utils.HTMLParser;
import com.jarchivemail.utils.QuotedPrintable;
import com.jarchivemail.utils.Uuencode;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-26
 */
public class Archivemail implements IAttachment {

	private Vector<String> senders;
	private Vector<String> receivers;
	private Vector<String> subjects;
	private Vector<Date> dates;
	private Vector<String[]> contentTypes;
	private Vector<String[]> contentEncodings;
	private Vector<int[]> contentStarts;
	private Vector<int[]> contentEnds;
	private Vector<String[]> contentFileNames;
	private Vector<Integer> msgOffsets;
	private Vector<Integer> sorting;

	private LineReader lineReader = null;
	public int numMessages = 0;
	private String lastLine;

	public Archivemail(LineReader lReader) {
		if (null == lReader) {
			return;
		}
		this.lineReader = lReader;

		senders = new Vector<String>();
		receivers = new Vector<String>();
		subjects = new Vector<String>();
		dates = new Vector<Date>();
		contentTypes = new Vector<String[]>();
		contentEncodings = new Vector<String[]>();
		contentStarts = new Vector<int[]>();
		contentEnds = new Vector<int[]>();
		contentFileNames = new Vector<String[]>();
		msgOffsets = new Vector<Integer>();
		sorting = new Vector<Integer>();
	}

	public String getSender(int index) {
		return senders.elementAt((sorting.elementAt(index)).intValue());
	}

	public String getReceiver(int index) {
		return receivers.elementAt((sorting.elementAt(index)).intValue());
	}

	public String getFormattedDate(int index) {
		Date date = dates.elementAt((sorting.elementAt(index)).intValue());
		String formattedDate;
		if (date.getTime() != 0L) {
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyyMMdd, HH:mm:ss");
			formattedDate = formatter.format(date);
		} else {
			formattedDate = "<unknown>";
		}
		return formattedDate;
	}

	public Date getDate(int index) {
		return dates.elementAt((sorting.elementAt(index)).intValue());
	}

	public String getSubject(int index) {
		return subjects.elementAt((sorting.elementAt(index)).intValue());
	}

	public String getContentType(int index) {
		return contentTypes.elementAt((sorting.elementAt(index)).intValue())
				.toString();
	}

	/**
	 * 转换为EML格式文件
	 * 
	 * @param messages
	 * @param useReceiver
	 * @param directory
	 * @return
	 */
	public int saveAsEml(int[] messages, boolean useReceiver, String directory) {
		Vector<Integer> toSave = new Vector<Integer>();
		int successfulExported = 0;

		for (int i = 0; i < messages.length; i++) {
			Integer index = sorting.elementAt(messages[i]);
			toSave.addElement(index);
		}

		try {
			for (int i = 0; i < toSave.size(); i++) {
				int index = (toSave.elementAt(i)).intValue();
				int msgOffset = (msgOffsets.elementAt(index)).intValue();
				Date msgDate = dates.elementAt(index);

				int msgOffsetNext;
				if (index < msgOffsets.size() - 1) {
					msgOffsetNext = (msgOffsets.elementAt(index + 1))
							.intValue();
				} else {
					msgOffsetNext = -1;
				}

				lineReader.setLineNr(msgOffset + 1);
				boolean msgEnd = false;

				String baseFileName;
				if (useReceiver) {
					baseFileName = makeFilename(receivers.elementAt(index),
							subjects.elementAt(index));
				} else {
					baseFileName = makeFilename(senders.elementAt(index),
							subjects.elementAt(index));
				}
				String fileName = baseFileName + ".eml";

				File outFile = new File(directory);
				if (!outFile.exists()) {
					outFile.mkdirs();
				}
				int j = 1;
				while ((outFile = new File(directory, fileName)).exists()) {
					fileName = baseFileName + "(" + j + ")" + ".eml";
					j++;
				}

				String line;

				try {
					BufferedWriter bw = new BufferedWriter(new FileWriter(
							outFile));
					while (!msgEnd && (line = lineReader.readLine()) != null) {
						bw.write(line, 0, line.length());
						bw.newLine();
						if (msgOffsetNext >= 0
								&& lineReader.getLineNr() >= msgOffsetNext - 1) {
							msgEnd = true;
						}
					}
					bw.close();
					if (msgDate != null && msgDate.getTime() >= 0) {
						outFile.setLastModified(msgDate.getTime());
					}
					successfulExported++;
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return successfulExported;
	}

	private String makeFilename(String name, String subject) {
		int index;

		if (name == null) {
			name = new String("");
		}
		if (subject == null) {
			subject = new String("");
		}

		name = name.trim();
		subject = subject.trim();

		name = name.replaceAll("[\\x01-\\x1F]|[<>|?/\\\\\":*\\r\\n]", " ");
		subject = subject
				.replaceAll("[\\x01-\\x1F]|[<>|?/\\\\\":*\\r\\n]", " ");
		name = name.replaceAll(" {2,}", " ");
		subject = subject.replaceAll(" {2,}", " ");

		if ((index = name.indexOf("@")) >= 0) {
			name = name.substring(0, index);
		}

		if (subject.equals("")) {
			subject = "<<no subject>>";
		}

		if (name.length() + subject.length() > 60) {
			if (subject.length() > 40) {
				subject = subject.substring(0, 40);
			}
			if (name.length() + subject.length() > 60) {
				name = name.substring(0, 20);
			}
		}

		if (name.length() == 0) {
			return subject;
		}
		return name + " - " + subject;
	}

	public void close() {
		try {
			lineReader.close();
		} catch (IOException e) {
		}
	}

	public void sort(String criterion) {
		Vector<?> toSort;
		if (criterion.equals("Date")) {
			toSort = dates;
		} else if (criterion.equals("Sender")) {
			toSort = senders;
		} else if (criterion.equals("Receiver")) {
			toSort = receivers;
		} else if (criterion.equals("Subject")) {
			toSort = subjects;
		} else {
			return;
		}
		sorting = indexSort(sorting, toSort);
	}

	public Vector<Integer> indexSort(Vector<Integer> index, Vector<?> items) {
		Vector<Object> buffer = new Vector<Object>();
		Vector<Integer> newIndex = new Vector<Integer>();

		boolean isString = false;

		if (index.size() > 0) {
			isString = items.elementAt(0) instanceof String;
		}

		for (int i = 0; i < index.size(); i++) {
			Object item;
			if (isString) {
				item = ((String) items.elementAt(((Integer) index.elementAt(i))
						.intValue())).toUpperCase();
			} else {
				item = items.elementAt(((Integer) index.elementAt(i))
						.intValue());
			}
			@SuppressWarnings("unchecked")
			int insertionPoint = Collections.binarySearch(
					(List<? extends Comparable<? super Object>>) buffer, item);
			if (insertionPoint < 0) {
				insertionPoint = -insertionPoint - 1;
			}
			buffer.insertElementAt(item, insertionPoint);
			newIndex.insertElementAt(index.elementAt(i), insertionPoint);
		}
		return newIndex;
	}

	public LineReader getLineReader() {
		return lineReader;
	}

	public Vector<String> getSenders() {
		return senders;
	}

	public Vector<String> getReceivers() {
		return receivers;
	}

	public Vector<String> getSubjects() {
		return subjects;
	}

	public Vector<Date> getDates() {
		return dates;
	}

	public Vector<String[]> getContentTypes() {
		return contentTypes;
	}

	public Vector<String[]> getContentEncodings() {
		return contentEncodings;
	}

	public Vector<int[]> getContentStarts() {
		return contentStarts;
	}

	public Vector<int[]> getContentEnds() {
		return contentEnds;
	}

	public Vector<String[]> getContentFileNames() {
		return contentFileNames;
	}

	public Vector<Integer> getMsgOffsets() {
		return msgOffsets;
	}

	public Vector<Integer> getSorting() {
		return sorting;
	}

	public void setSorting(Vector<Integer> sorting) {
		this.sorting = sorting;
	}

	public String getLastLine() {
		return lastLine;
	}

	public void setLastLine(String lastLine) {
		this.lastLine = lastLine;
	}

	public String getMessageContent(int index) {
		StringBuffer outBuf = new StringBuffer("");
		String fileName;
		String[] knownContents = { "TEXT", "TEXT/PLAIN", "TEXT/HTML" };

		index = (sorting.elementAt(index)).intValue();

		int numParts = (contentStarts.elementAt(index)).length;

		int k = 0;
		do {
			for (int i = 0; i < numParts; i++) {
				String contentType = (contentTypes.elementAt(index))[i];
				if (contentType == null) {
					contentType = "TEXT/PLAIN";
				} else {
					contentType = contentType.toUpperCase();
				}

				if (contentType.equals(knownContents[k])) {
					if (outBuf.length() > 0) {
						outBuf.append("\r\n");
						outBuf.append("--------- Next Part");
						if ((fileName = (contentFileNames.elementAt(index))[i]) != null) {
							outBuf.append(": \"" + fileName + "\"");
						}
						outBuf.append(" ---------");
						outBuf.append("\r\n\r\n");
					}

					String contentEncoding = (contentEncodings.elementAt(index))[i];
					if (contentEncoding == null) {
						contentEncoding = "7BIT";
					}

					int contentStart = (contentStarts.elementAt(index))[i];
					int contentEnd = (contentEnds.elementAt(index))[i];

					try {
						lineReader.setLineNr(contentStart);

						String line;

						for (int j = contentStart; j <= contentEnd; j++) {
							line = lineReader.readLine();
							outBuf.append(line);
							if (!contentEncoding.equalsIgnoreCase("BASE64")) {
								if (j < contentEnd) {
									outBuf.append("\r\n");
								}
							}
						}

						if (contentEncoding
								.equalsIgnoreCase("QUOTED-PRINTABLE")) {
							outBuf = new StringBuffer(
									QuotedPrintable.decode(outBuf.toString()));
						}
						if (contentEncoding.equalsIgnoreCase("BASE64")) {
							outBuf = new StringBuffer(
									new String(Base64.decode(outBuf.toString()
											.getBytes())));
						}
					} catch (FileNotFoundException e) {
					} catch (IOException e) {
					}
				}
			}
			k++;
		} while (outBuf.length() == 0 && k < knownContents.length);

		if (knownContents[k - 1].equals("TEXT/HTML")) {
			HTMLParser parser = new HTMLParser(outBuf.toString());
			outBuf = new StringBuffer(parser.getPlainText());
		}
		return outBuf.toString();
	}

	@Override
	public int saveAttachments(int index, int[] attachments, String directory) {
		int successfulSaved = 0;

		index = (sorting.elementAt(index)).intValue();
		Date msgDate = dates.elementAt(index);
		String[] fileNames = contentFileNames.elementAt(index);

		try {
			for (int i = 0; i < attachments.length; i++) {
				int j = 0, k = 0;
				for (j = 0; j < fileNames.length; j++) {
					if (fileNames[j] != null) {
						if (k == attachments[i])
							break;
						k++;
					}
				}
				if (k != attachments[i]) {
					continue;
				}

				int contentStart = (contentStarts.elementAt(index))[j];
				int contentEnd = (contentEnds.elementAt(index))[j];
				String contentEncoding = (contentEncodings.elementAt(index))[j];
				if (contentEncoding == null) {
					contentEncoding = "7BIT";
				}

				String fileName = fileNames[j];
				String baseFileName = fileName;

				File outFile = new File(directory);
				if (!outFile.exists()) {
					outFile.mkdirs();
				}

				j = 1;
				while ((outFile = new File(directory, fileName)).exists()) {
					fileName = "(" + j + ")" + baseFileName;
					j++;
				}

				String line;
				int curLine;
				lineReader.setLineNr(contentStart);

				try {
					StringBuffer out = new StringBuffer();
					if (contentEncoding.equalsIgnoreCase("BASE64")) {
						while ((curLine = lineReader.getLineNr()) <= contentEnd
								&& (line = lineReader.readLine()) != null) {
							out.append(line);
						}
						byte[] outBytes = out.toString().getBytes();
						outBytes = Base64.decode(outBytes);
						FileOutputStream fo = new FileOutputStream(outFile);
						fo.write(outBytes);
						fo.close();
					} else if (contentEncoding
							.equalsIgnoreCase("QUOTED-PRINTABLE")) {
						while ((curLine = lineReader.getLineNr()) <= contentEnd
								&& (line = lineReader.readLine()) != null) {
							out.append(line);
							if (curLine < contentEnd) {
								out.append("\r\n");
							}
						}
						String outString = QuotedPrintable.decode(out
								.toString());
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								outFile));
						bw.write(outString);
						bw.close();
					} else if (contentEncoding.equalsIgnoreCase("X-UUENCODE")
							|| contentEncoding.equalsIgnoreCase("UUENCODE")) {
						while ((curLine = lineReader.getLineNr()) <= contentEnd
								&& (line = lineReader.readLine()) != null) {
							out.append(line);
							if (curLine < contentEnd) {
								out.append("\r\n");
							}
						}
						byte[] outBytes = Uuencode.decode(out.toString());
						FileOutputStream fo = new FileOutputStream(outFile);
						fo.write(outBytes);
						fo.close();
					} else {
						while ((curLine = lineReader.getLineNr()) <= contentEnd
								&& (line = lineReader.readLine()) != null) {
							out.append(line);
							if (curLine < contentEnd) {
								out.append("\r\n");
							}
						}
						BufferedWriter bw = new BufferedWriter(new FileWriter(
								outFile));
						bw.write(out.toString());
						bw.close();
					}
					if (msgDate != null) {
						outFile.setLastModified(msgDate.getTime());
					}
					successfulSaved++;
				} catch (IOException e) {
				}
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return successfulSaved;
	}

	public void addFileNames() {
		for (int i = 0; i < numMessages; i++) {
			int numContents = (contentStarts.elementAt(i)).length;
			String[] fileNames = contentFileNames.elementAt(i);
			String[] types = contentTypes.elementAt(i);

			for (int j = 0; j < numContents; j++) {
				if (fileNames[j] == null) {
					if ((types[j] != null && types[j]
							.equalsIgnoreCase("MESSAGE/RFC822"))) {
						int contentStart = (contentStarts.elementAt(i))[j];
						int contentEnd = (contentEnds.elementAt(i))[j];
						ContentParser contentParser = new ContentParser(
								lineReader, contentStart, contentEnd);
						String attachmentSubject;
						if (contentParser.getSubject() != null) {
							attachmentSubject = contentParser.getSubject();
						} else {
							attachmentSubject = "";
						}
						(contentFileNames.elementAt(i))[j] = makeFilename("",
								attachmentSubject) + ".eml";
					}
				}
			}
		}
	}

	@Override
	public String[] getAttachmentFileNames(int index) {
		index = (sorting.elementAt(index)).intValue();

		String[] fileNames = contentFileNames.elementAt(index);
		// String[] types = (String[]) contentTypes.elementAt(index);
		int numFileNames = 0;

		for (int i = 0; i < fileNames.length; i++) {
			if (fileNames[i] != null) {
				numFileNames++;
			}
		}

		String[] out = new String[numFileNames];
		int j = 0;

		for (int i = 0; i < fileNames.length; i++) {
			if (fileNames[i] != null) {
				out[j++] = fileNames[i];
			}
		}

		return out;
	}
}
