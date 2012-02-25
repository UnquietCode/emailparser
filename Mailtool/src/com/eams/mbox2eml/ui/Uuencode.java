package com.eams.mbox2eml.ui;

import java.util.StringTokenizer;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-23
 */
public class Uuencode {

	public static byte[] decode(String data) {
		byte[] out;

		int outLength = 0;
		StringTokenizer st = new StringTokenizer(data, "\r\n");
		boolean start = false;
		while (st.hasMoreTokens()) {
			String line = st.nextToken();
			if (line.regionMatches(true, 0, "begin ", 0, 6)) {
				start = true;
				continue;
			}
			if (start) {
				if (line.regionMatches(true, 0, "end", 0, 3)) {
					break;
				}
				int decodedLength = decodeByte((byte) line.charAt(0));
				outLength += decodedLength;
			}
		}

		out = new byte[outLength];

		int k = 0;
		start = false;
		st = new StringTokenizer(data, "\r\n");
		while (st.hasMoreTokens()) {
			String line = st.nextToken();
			if (line.regionMatches(true, 0, "begin ", 0, 6)) {
				start = true;
				continue;
			}
			if (start) {
				if (line.regionMatches(true, 0, "end", 0, 3)) {
					break;
				}
				byte[] bytes = line.getBytes();
				byte lastC = 0;
				for (int i = 1; i < bytes.length; i++) {
					int j = (i - 1) % 4;
					if (j == 0) {
						lastC = 0;
					}

					byte c = decodeByte(bytes[i]);
					if (j > 0 && k < out.length) {
						out[k++] = (byte) (lastC | (c >> (6 - 2 * j)));
					}
					lastC = (byte) (c << (2 * (j + 1)));
				}
			}
		}
		return out;
	}

	private static byte decodeByte(byte in) {
		if (in == 0x60) {
			return 0;
		}
		return (byte) (in - 0x20);
	}
}
