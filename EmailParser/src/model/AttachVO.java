/**
 * @Package: model
 * @Title: AttachVO.java
 * @Author: zhangzuoqiang
 * @Time: 1:03:18 PM Aug 30, 2011
 * @Version: 
 */
package model;

import java.io.InputStream;

import utils.StringUtils;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 30, 2011
 */
public class AttachVO {

	public AttachVO() {

	}

	@Override
	public String toString() {
		return "--------------------\n" + "Attachment File name: "
				+ this.getFileName() + "\nAttachment File: "
				+ StringUtils.inputStream2String(this.getFile())
				+ "--------------------";
	}

	public AttachVO(String filename, InputStream is) {
		this.setFileName(filename);
		this.setFile(is);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public InputStream getFile() {
		return file;
	}

	public void setFile(InputStream file) {
		this.file = file;
	}

	private String fileName;
	private InputStream file;
}
