/**
 * @Package: model
 * @Title: EmailFileVO.java
 * @Author: zhangzuoqiang
 * @Time: 10:59:12 AM Aug 30, 2011
 * @Version: 
 */
package model;

import java.io.File;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 30, 2011
 */
public class EmailFileVO {

	public EmailFileVO() {

	}

	public EmailFileVO(String suffix, File file) {
		this.setSuffix(suffix);
		this.setFile(file);
		this.setFilepath(file.getParentFile().getPath());
	}

	@Override
	public String toString() {
		return "File: " + this.getFile().getName() + "\nSuffix: "
				+ this.getSuffix() + "\nFile path: " + this.getFilepath();
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	private String filepath;
	private String suffix;
	private File file;
}
