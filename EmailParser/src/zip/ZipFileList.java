/**
 * @Package: zip
 * @Title: ZipFileList.java
 * @Author: zhangzuoqiang
 * @Time: 4:24:19 PM Aug 29, 2011
 * @Version: 
 */
package zip;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import utils.LangUtil;

/**
 * @Description: 获取压缩文件文件目录
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class ZipFileList {

	public static void main(String[] args) throws IOException {

		ZipFile zf = new ZipFile("E:\\control.zip");
		Enumeration<? extends ZipEntry> e = zf.entries();

		System.out.println(LangUtil.get("20001"));
		System.out.println("-------------------------");

		while (e.hasMoreElements()) {
			ZipEntry ze = (ZipEntry) e.nextElement();
			String name = ze.getName();

			long uncompressedSize = ze.getSize();
			long compressedSize = ze.getCompressedSize();
			long crc = ze.getCrc();
			int method = ze.getMethod();
			String comment = ze.getComment();

			System.out.println(name + " was stored at "
					+ new Date(ze.getTime()));
			if (method == ZipEntry.STORED) {
				System.out.println("with a size of  " + uncompressedSize
						+ " bytes");
			} else if (method == ZipEntry.DEFLATED) {
				System.out.println("from " + uncompressedSize + " bytes to "
						+ compressedSize);
			} else {
				System.out.println("from " + uncompressedSize + " bytes to "
						+ compressedSize);
			}
			System.out.println("Its CRC is " + crc);
			if (comment != null && !comment.equals("")) {
				System.out.println(comment);
			}
			if (ze.isDirectory()) {
				System.out.println(name + " is a directory");
			}
			
			System.out.println("-------------------------");
		}
	}

}
