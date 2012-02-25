package com.jarchivemail.parser.mbox;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import com.jarchivemail.parser.AbstractParser;
import com.jarchivemail.parser.Archivemail;
import com.jarchivemail.parser.ContentParser;

public class MBOXParser extends AbstractParser {

	public MBOXParser(Archivemail _archivemail) {
		super(_archivemail);

		String line;

		try {
			long overallTime = 0;
			long startTime = System.currentTimeMillis();
			while ((line = archivemail.getLineReader().readLine()) != null) {
				if (line.regionMatches(false, 0, "From ", 0, 5)) {
					int curLine = archivemail.getLineReader().getLineNr() - 1;
					archivemail.getMsgOffsets()
							.addElement(new Integer(curLine));
					archivemail.numMessages++;

					ContentParser contentParser = new ContentParser(
							archivemail.getLineReader(), curLine + 1);

					if (contentParser.getSender() != null) {
						archivemail.getSenders().addElement(
								contentParser.getSender());
					} else {
						archivemail.getSenders().addElement(
								new String("<unknown>"));
					}

					if (contentParser.getReceiver() != null) {
						archivemail.getReceivers().addElement(
								contentParser.getReceiver());
					} else {
						archivemail.getReceivers().addElement(
								new String("<unknown>"));
					}

					if (contentParser.getSubject() != null) {
						archivemail.getSubjects().addElement(
								contentParser.getSubject());
					} else {
						archivemail.getSubjects().addElement(
								new String("<unknown>"));
					}

					archivemail.getContentTypes().addElement(
							contentParser.getContentTypes());
					archivemail.getContentEncodings().addElement(
							contentParser.getContentEncodings());
					archivemail.getContentStarts().addElement(
							contentParser.getContentStarts());
					archivemail.getContentEnds().addElement(
							contentParser.getContentEnds());
					archivemail.getContentFileNames().addElement(
							contentParser.getContentFileNames());

					if (contentParser.getSendDate() != null) {
						archivemail.getDates().addElement(
								contentParser.getSendDate());
					} else if (contentParser.getReceiveDate() != null) {
						archivemail.getDates().addElement(
								contentParser.getReceiveDate());
					} else {
						archivemail.getDates().addElement(new Date(0));
					}
				}
				archivemail.setLastLine(line);
			}

			archivemail.addFileNames();

			overallTime += System.currentTimeMillis() - startTime;
			System.out.println("Spend : " + overallTime + " ms.");

			for (int i = 0; i < archivemail.numMessages; i++) {
				archivemail.getSorting().addElement(new Integer(i));
			}
			archivemail.setSorting(archivemail.indexSort(
					archivemail.getSorting(), archivemail.getDates()));
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

}
