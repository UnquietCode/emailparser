package com.eams.mbox2eml;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-23
 */
public class Base64 {

	public static byte[] decode(byte[] data) {
		byte[] out;

		if (4 * (data.length / 4) != data.length) {
			return new byte[0];
		}

		int outLength = 3 * (data.length / 4);
		if (data[data.length - 1] == '=')
			outLength--;
		if (data[data.length - 2] == '=')
			outLength--;

		out = new byte[outLength];

		int k = 0;
		for (int i = 0; i < data.length; i += 4) {
			byte lastC = 0;
			for (int j = 0; j < 4; j++) {
				byte c = data[i + j];
				if (c >= 'A' && c <= 'Z') {
					c = (byte) (c - 'A');
				} else if (c >= 'a' && c <= 'z') {
					c = (byte) (c - 'a' + 26);
				} else if (c >= '0' && c <= '9') {
					c = (byte) (c - '0' + 52);
				} else if (c == '+') {
					c = (byte) 62;
				} else if (c == '/') {
					c = (byte) 63;
				} else
					break;
				if (j > 0) {
					out[k++] = (byte) (lastC | (c >> (6 - 2 * j)));
				}
				lastC = (byte) (c << (2 * (j + 1)));
			}
		}
		return out;
	}
}
