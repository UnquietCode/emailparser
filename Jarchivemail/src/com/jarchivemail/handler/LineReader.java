package com.jarchivemail.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-23
 */
public class LineReader {

	private RandomAccessFile br;
	private Vector<Integer> offset;
	private byte[] buf;
	private String bufString = "";
	private int curOffset = 0;
	private int curLine = 1;
	private File file;

	private static int BUFFERSIZE = 1024;

	public LineReader(File in) throws FileNotFoundException {
		file = in;
		br = new RandomAccessFile(file, "r");
		offset = new Vector<Integer>();
		offset.addElement(new Integer(0));
		buf = new byte[BUFFERSIZE];
	}

	public int getLineNr() {
		return curLine;
	}

	public void setLineNr(int lineNr) throws IOException {
		if (lineNr <= offset.size()) {
			curOffset = (offset.elementAt(lineNr - 1)).intValue();
			br.seek(curOffset);
			bufString = "";
			curLine = lineNr;
		} else {
			curLine = offset.size();
			curOffset = (offset.elementAt(curLine - 1)).intValue();
			br.seek(curOffset);
			bufString = "";
			while (readLine() != null && curLine < lineNr) {
				;
			}
		}
	}

	public String readLine() throws IOException {
		int crPos = -1, lfPos = -1;
		String out = "";
		int numBytes = BUFFERSIZE;

		crPos = bufString.indexOf('\r');
		lfPos = bufString.indexOf('\n');

		while (crPos < 0 && lfPos < 0 && numBytes == BUFFERSIZE) {
			numBytes = br.read(buf, 0, BUFFERSIZE);
			if (numBytes > 0) {
				bufString += new String(buf, 0, numBytes);
			}
			crPos = bufString.indexOf('\r');
			lfPos = bufString.indexOf('\n');
		}

		if (crPos >= 0 || lfPos >= 0) {
			numBytes = 0;
			if (lfPos < 0 || (crPos >= 0 && crPos < lfPos)) {
				out = bufString.substring(0, crPos);
				numBytes = crPos + 1;
				if (bufString.length() <= crPos + 1) {
					int c = br.read();
					if (c > 0) {
						bufString += (char) c;
					}
				}
				if (bufString.length() > crPos + 1
						&& bufString.charAt(crPos + 1) == '\n') {
					bufString = bufString.substring(crPos + 2);
					numBytes++;
				} else {
					bufString = bufString.substring(crPos + 1);
				}
			} else {
				out = bufString.substring(0, lfPos);
				numBytes = lfPos + 1;
				bufString = bufString.substring(lfPos + 1);
			}
			curLine++;
			curOffset += numBytes;
			if (curLine > offset.size()) {
				offset.addElement(new Integer(curOffset));
			}
		} else {
			if (bufString.length() > 0) {
				out += bufString;
				bufString = "";
				curLine++;
			} else {
				return null;
			}
		}
		return out;
	}

	public void reset() throws IOException {
		br.seek(-curOffset);
		curLine = 0;
		curOffset = 0;
		bufString = "";
	}

	public void close() throws IOException {
		br.close();
	}
}
