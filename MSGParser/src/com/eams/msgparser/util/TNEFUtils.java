/*
 *  (c) copyright 2003-2007 Amichai Rothman
 *
 *  This file is part of the Java TNEF package.
 *
 *  The Java TNEF package is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  The Java TNEF package is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.eams.msgparser.util;

import java.io.UnsupportedEncodingException;

/**
 * 
 * @Description The <code>TNEFUtils</code> class provides utility methods used
 *              by the TNEF processing classes.
 * @Author zhangzuoqiang
 * @Date 2012-2-23
 */
public class TNEFUtils {

	/**
	 * Returns an unsigned 8-bit value from a byte array.
	 * 
	 * @param buf
	 *            a byte array from which byte value is taken
	 * @param offset
	 *            the offset within buf from which byte value is taken
	 * @return an unsigned 8-bit value as an int.
	 */
	public static int getU8(byte[] buf, int offset) {
		return buf[offset] & 0xFF;
	}

	/**
	 * Returns an unsigned 16-bit value from little-endian ordered bytes.
	 * 
	 * @param b1
	 *            first byte value
	 * @param b2
	 *            second byte value
	 * @return an unsigned 16-bit value as an int.
	 */
	public static int getU16(int b1, int b2) {
		return ((b1 & 0xFF) | ((b2 & 0xFF) << 8)) & 0xFFFF;
	}

	/**
	 * Returns an unsigned 16-bit value from little-endian ordered bytes.
	 * 
	 * @param buf
	 *            a byte array from which byte values are taken
	 * @param offset
	 *            the offset within buf from which byte values are taken
	 * @return an unsigned 16-bit value as an int.
	 */
	public static int getU16(byte[] buf, int offset) {
		return ((buf[offset] & 0xFF) | ((buf[offset + 1] & 0xFF) << 8)) & 0xFFFF;
	}

	/**
	 * Returns an unsigned 32-bit value from little-endian ordered bytes.
	 * 
	 * @param b1
	 *            first byte value
	 * @param b2
	 *            second byte value
	 * @param b3
	 *            third byte value
	 * @param b4
	 *            fourth byte value
	 * @return an unsigned 32-bit value as a long.
	 */
	public static long getU32(int b1, int b2, int b3, int b4) {
		return ((b1 & 0xFF) | ((b2 & 0xFF) << 8) | ((b3 & 0xFF) << 16) | ((b4 & 0xFF) << 24)) & 0x00000000FFFFFFFFL;
	}

	/**
	 * Returns an unsigned 32-bit value from little-endian ordered bytes.
	 * 
	 * @param buf
	 *            a byte array from which byte values are taken
	 * @param offset
	 *            the offset within buf from which byte values are taken
	 * @return an unsigned 32-bit value as a long.
	 */
	public static long getU32(byte[] buf, int offset) {
		return ((buf[offset] & 0xFF) | ((buf[offset + 1] & 0xFF) << 8)
				| ((buf[offset + 2] & 0xFF) << 16) | ((buf[offset + 3] & 0xFF) << 24)) & 0x00000000FFFFFFFFL;
	}

	/**
	 * Returns a 64-bit value from little-endian ordered bytes.
	 * 
	 * @param buf
	 *            a byte array from which byte values are taken
	 * @param offset
	 *            the offset within buf from which byte values are taken
	 * @return a 64-bit value as a long.
	 */
	public static long getU64(byte[] buf, int offset) {
		return ((getU32(buf, offset + 4) & 0x00000000FFFFFFFFL) << 32)
				| (getU32(buf, offset) & 0x00000000FFFFFFFFL);
	}

	/**
	 * Returns a 32-bit value containing a combined attribute type and ID.
	 * 
	 * @param atp
	 *            the attribute type
	 * @param id
	 *            the attribute ID
	 * @return a 32-bit value containing a combined attribute type and ID.
	 */
	public static int attribute(int atp, int id) {
		return ((atp << 16) | id);
	}

	/**
	 * Returns the ID part of a 32-bit combined attribute type and ID value.
	 * 
	 * @param att
	 *            the combined attribute type and ID value
	 * @return the ID part of a 32-bit combined attribute type and ID value.
	 */
	public static int attID(int att) {
		return (att & 0x0000FFFF);
	}

	/**
	 * Returns the type part of a 32-bit combined attribute type and ID value.
	 * 
	 * @param att
	 *            the combined attribute type and ID value
	 * @return the type part of a 32-bit combined attribute type and ID value.
	 */
	public static int attType(int att) {
		return ((att >> 16) & 0x0000FFFF);
	}

	/**
	 * Returns the checksum of a given byte array.
	 * 
	 * @param data
	 *            the byte array on which to calculate the checksum.
	 * @return the checksum of a given byte array.
	 */
	public static int calculateChecksum(byte[] data) {
		return calculateChecksum(data, 0, data.length);
	}

	/**
	 * Returns the checksum of a range of bytes within a given byte array.
	 * 
	 * @param data
	 *            the byte array on which to calculate the checksum
	 * @param offset
	 *            the offset within the array from which to begin
	 * @param length
	 *            the number of bytes to calculate checksum on
	 * @return the checksum of a range of bytes within a given byte array.
	 */
	public static int calculateChecksum(byte[] data, int offset, int length) {
		// NOTE: the AND operation expands the byte to an int containing the
		// unsigned byte value. This is necessary since a Java byte is signed.
		long checksum = 0;
		length += offset; // now marks the end index itself
		for (int i = offset; i < length; i++) {
			checksum += (data[i] & 0xFF);
		}
		return (int) (checksum % 65536);
	}

	/**
	 * Removes all null characters ('\0') from the end of a given String. Useful
	 * for converting a C-style null terminated string to a Java String.
	 * 
	 * @param s
	 *            a String
	 * @return a String identical to the given string, with trailing null
	 *         characters removed.
	 */
	public static String removeTerminatingNulls(String s) {
		if (s == null) {
			return null;
		}
		int len = s.length();
		while (len > 0 && s.charAt(len - 1) == '\0') {
			len--;
		}
		return len == s.length() ? s : s.substring(0, len);
	}

	/**
	 * Replaces all occurences of given substring within string with a
	 * replacement string.
	 * 
	 * @param s
	 *            the string to be modified
	 * @param search
	 *            the substring to search for
	 * @param replace
	 *            the string with which to replace occurences of the search
	 *            substring
	 * @return a new string consisting of the given string, with all occurences
	 *         of search string replaced by replace string. If given string or
	 *         search string are empty or null, the string itself is returned.
	 */
	public static String replace(String s, String search, String replace) {
		if (s == null || search == null || search.length() == 0) {
			return s;
		}
		if (replace == null) {
			replace = "";
		}
		int len = s.length();
		int slen = search.length();
		int rlen = replace.length();
		int ind = 0;
		while (ind < len && (ind = s.indexOf(search, ind)) > -1) {
			s = s.substring(0, ind) + replace + s.substring(ind + slen);
			ind += rlen;
		}
		return s;
	}

	/**
	 * Creates a String from a C-style null terminated byte sequence.
	 * 
	 * The null terminated byte sequence is interpreted as 8-bit ISO-8859-1
	 * characters (a.k.a. ISO-Latin-1), which is a superset of US-ASCII. This
	 * way we don't lose any 8-bit values and remain fully compatible: If the
	 * source charset is unknown, a str.getByte("ISO8859_1") will reconstruct
	 * the exact original byte sequence which the application can then process
	 * in any charset it sees fit.
	 * 
	 * @param bytes
	 *            a byte array containing a C-style null terminated string
	 * @param offset
	 *            the offset within bytes where the string begins
	 * @param length
	 *            the length of the C-style string in bytes, which may include
	 *            any number of terminating null ('\0') characters
	 * @return a String containing the C-style string's characters, interpreted
	 *         as ISO-8859-1 characters.
	 */
	public static String createString(byte[] bytes, int offset, int length) {
		try {
			return removeTerminatingNulls(new String(bytes, offset, length,
					"ISO8859_1"));
		} catch (UnsupportedEncodingException uee) {
		}
		return "";
	}

	/**
	 * Creates a String from a C-style null terminated Unicode byte sequence.
	 * 
	 * The null terminated byte sequence is interpreted as 16-bit Unicode
	 * characters (UTF-16), stored in Little Endian order.
	 * 
	 * @param bytes
	 *            a byte array containing a C-style null terminated Unicode
	 *            string
	 * @param offset
	 *            the offset within bytes where the string begins
	 * @param length
	 *            the length of the C-style string in bytes, which may include
	 *            any number of terminating null ('\0') characters
	 * @return a String containing the C-style string's characters, interpreted
	 *         as Unicode (UTF-16 Little Endian) characters.
	 */
	public static String createStringUnicode(byte[] bytes, int offset,
			int length) {
		try {
			return removeTerminatingNulls(new String(bytes, offset, length,
					"UTF-16LE"));
		} catch (UnsupportedEncodingException uee) {
		}
		return "";
	}

	/**
	 * Creates a String containing the hexadecimal representation of the given
	 * bytes.
	 * 
	 * @param bytes
	 *            a byte array who's content is to be displayed
	 * @return a String containing the hexadecimal representation of the given
	 *         bytes.
	 */
	public static String toHexString(byte[] bytes) {
		return toHexString(bytes, 0, bytes != null ? bytes.length : 0, -1);
	}

	/**
	 * Creates a String containing the hexadecimal representation of the given
	 * bytes.
	 * 
	 * @param bytes
	 *            a byte array who's content is to be displayed
	 * @param max
	 *            the maximum number of bytes to be displayed (-1 means no
	 *            limit)
	 * @return a String containing the hexadecimal representation of the given
	 *         bytes.
	 */
	public static String toHexString(byte[] bytes, int max) {
		return toHexString(bytes, 0, bytes != null ? bytes.length : 0, max);
	}

	/**
	 * Creates a String containing the hexadecimal representation of the given
	 * bytes.
	 * 
	 * @param bytes
	 *            a byte array who's content is to be displayed
	 * @param offset
	 *            the offset within the byte array to start at
	 * @param len
	 *            the number of bytes to process
	 * @param max
	 *            the maximum number of bytes to be displayed (-1 means no
	 *            limit)
	 * @return a String containing the hexadecimal representation of the given
	 *         bytes.
	 */
	public static String toHexString(byte[] bytes, int offset, int len, int max) {
		int count = max > -1 ? Math.min(max, len) : len;
		StringBuffer s = new StringBuffer();
		s.append('[');
		if (bytes == null) {
			s.append((Object) null);
		} else {
			String b;
			for (int i = 0; i < count; i++) {
				b = Integer.toHexString(bytes[offset + i] & 0xFF).toUpperCase();
				if (b.length() == 1)
					s.append('0');
				s.append(b);
			}
			if (count < len) {
				s.append("... (" + len + " bytes)");
			}
		}
		s.append(']');
		return s.toString();
	}

	/**
	 * Compares two byte array sections for equality.
	 * 
	 * @param src
	 *            the source byte array
	 * @param srcoffset
	 *            the offset within src from which to start comparison
	 * @param dst
	 *            the destination byte array
	 * @param dstoffset
	 *            the offset within dst from which to start comparison
	 * @param length
	 *            the number of bytes to compare
	 * @return true if the byte sequences in the respective specified locations
	 *         are identical, false if there are any differences.
	 */
	public static boolean equals(byte[] src, int srcoffset, byte[] dst,
			int dstoffset, int length) {
		boolean equals = true;
		for (int i = 0; equals && i < length; i++) {
			equals = src[srcoffset + i] == dst[dstoffset + i];
		}
		return equals;
	}

	/**
	 * Checks whether the given string contains a TNEF mime type
	 * 
	 * @param mimeType
	 *            the mimeType to check
	 * @return true if the given string contains a TNEF mime type, false
	 *         otherwise.
	 */
	static public boolean isTNEFMimeType(String mimeType) {
		return mimeType != null
				&& ((mimeType = mimeType.toLowerCase())
						.startsWith("application/ms-tnef") || mimeType
						.startsWith("application/vnd.ms-tnef"));
	}

	/** The lookup table used in the CRC32 calculation */
	static int[] CRC32_TABLE;
	static {
		CRC32_TABLE = new int[256];
		for (int i = 0; i < 256; i++) {
			int c = i;
			for (int j = 0; j < 8; j++) {
				c = ((c & 1) == 1) ? 0xEDB88320 ^ (c >>> 1) : c >>> 1;
			}
			CRC32_TABLE[i] = c;
		}
	}

	/**
	 * Calculates the CRC32 of the given bytes. The CRC32 calculation is similar
	 * to the standard one as demonstrated in RFC 1952, but with the inversion
	 * (before and after the calculation) ommited.
	 * 
	 * @param buf
	 *            the byte array to calculate CRC32 on
	 * @param off
	 *            the offset within buf at which the CRC32 calculation will
	 *            start
	 * @param len
	 *            the number of bytes on which to calculate the CRC32
	 * @return the CRC32 value.
	 */
	static public int calculateCRC32(byte[] buf, int off, int len) {
		int c = 0;
		int end = off + len;
		for (int i = off; i < end; i++) {
			c = CRC32_TABLE[(c ^ buf[i]) & 0xFF] ^ (c >>> 8);
		}
		return c;
	}

	/**
	 * Prebuffered bytes used in RTF-compressed format (found them in
	 * RTFLIB32.LIB)
	 * */
	static byte[] COMPRESSED_RTF_PREBUF;
	static {
		try {
			String prebuf = "{\\rtf1\\ansi\\mac\\deff0\\deftab720{\\fonttbl;}"
					+ "{\\f0\\fnil \\froman \\fswiss \\fmodern \\fscript "
					+ "\\fdecor MS Sans SerifSymbolArialTimes New RomanCourier"
					+ "{\\colortbl\\red0\\green0\\blue0\n\r\\par "
					+ "\\pard\\plain\\f0\\fs20\\b\\i\\u\\tab\\tx";
			COMPRESSED_RTF_PREBUF = prebuf.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException uee) {
			// never happens
		}
	}

	/**
	 * Decompresses compressed-RTF data.
	 * 
	 * @param src
	 *            the compressed-RTF data bytes
	 * @return an array containing the decompressed bytes.
	 * @throws IllegalArgumentException
	 *             if src does not contain valid compressed-RTF bytes.
	 */
	public static byte[] decompressRTF(byte[] src) {
		byte[] dst; // destination for uncompressed bytes
		int in = 0; // current position in src array
		int out = 0; // current position in dst array

		// get header fields (as defined in RTFLIB.H)
		if (src == null || src.length < 16) {
			throw new IllegalArgumentException("Invalid compressed-RTF header");
		}

		int compressedSize = (int) getU32(src, in);
		in += 4;
		int uncompressedSize = (int) getU32(src, in);
		in += 4;
		int magic = (int) getU32(src, in);
		in += 4;
		int crc32 = (int) getU32(src, in);
		in += 4;

		if (compressedSize != src.length - 4) {// check size excluding the size
												// field itself
			throw new IllegalArgumentException(
					"compressed-RTF data size mismatch");
		}

		if (crc32 != calculateCRC32(src, 16, src.length - 16)) {
			throw new IllegalArgumentException("compressed-RTF CRC32 failed");
		}

		// process the data
		if (magic == 0x414c454d) { // magic number that identifies the stream as
									// a uncompressed stream
			dst = new byte[uncompressedSize];
			System.arraycopy(src, in, dst, out, uncompressedSize); // just copy
																	// it as it
																	// is
		} else if (magic == 0x75465a4c) { // magic number that identifies the
											// stream as a compressed stream
			dst = new byte[COMPRESSED_RTF_PREBUF.length + uncompressedSize];
			System.arraycopy(COMPRESSED_RTF_PREBUF, 0, dst, 0,
					COMPRESSED_RTF_PREBUF.length);
			out = COMPRESSED_RTF_PREBUF.length;
			int flagCount = 0;
			int flags = 0;
			while (out < dst.length) {
				// each flag byte flags 8 literals/references, 1 per bit
				flags = (flagCount++ % 8 == 0) ? getU8(src, in++) : flags >> 1;
				if ((flags & 1) == 1) { // each flag bit is 1 for reference, 0
										// for literal
					int offset = getU8(src, in++);
					int length = getU8(src, in++);
					offset = (offset << 4) | (length >>> 4); // the offset
																// relative to
																// block start
					length = (length & 0xF) + 2; // the number of bytes to copy
					// the decompression buffer is supposed to wrap around back
					// to the beginning when the end is reached. we save the
					// need for such a buffer by pointing straight into the data
					// buffer, and simulating this behaviour by modifying the
					// pointers appropriately.
					offset = (out / 4096) * 4096 + offset;
					if (offset >= out) {// take from previous block
						offset -= 4096;
					}
					// note: can't use System.arraycopy, because the referenced
					// bytes can cross through the current out position.
					int end = offset + length;
					while (offset < end) {
						dst[out++] = dst[offset++];
					}
				} else { // literal
					dst[out++] = src[in++];
				}
			}
			// copy it back without the prebuffered data
			src = dst;
			dst = new byte[uncompressedSize];
			System.arraycopy(src, COMPRESSED_RTF_PREBUF.length, dst, 0,
					uncompressedSize);
		} else { // unknown magic number
			throw new IllegalArgumentException(
					"Unknown compression type (magic number " + magic + ")");
		}
		return dst;
	}

}
