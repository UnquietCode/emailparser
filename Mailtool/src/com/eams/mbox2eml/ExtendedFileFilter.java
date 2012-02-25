package com.eams.mbox2eml;

import java.io.File;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.filechooser.FileFilter;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-23
 */
public class ExtendedFileFilter extends FileFilter {

	private Vector<String> extensionList;
	private String description, fullDescription;

	public ExtendedFileFilter() {
		this("", "");
	}

	public ExtendedFileFilter(String extensionList, String description) {
		StringTokenizer st;

		st = new StringTokenizer(extensionList, " ,");

		this.extensionList = new Vector<String>();
		this.description = description;
		this.fullDescription = this.description;

		while (st.hasMoreTokens()) {
			addExtension(st.nextToken());
		}
	}

	public void addExtension(String extension) {
		if (extension != null && extension.length() > 0) {
			extensionList.addElement(extension.toLowerCase());
			generateFullDescription();
		}
	}

	public void setDescription(String description) {
		this.description = description;
		generateFullDescription();
	}

	private String getExtension(File f) {
		if (f != null) {
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if (i > 0 && i < filename.length() - 1) {
				return filename.substring(i + 1).toLowerCase();
			}
		}
		return null;
	}

	private void generateFullDescription() {
		int i;

		fullDescription = description + " (";

		for (i = 0; i < extensionList.size(); i++) {
			fullDescription += (String) extensionList.elementAt(i);
			if (i < extensionList.size() - 1) {
				fullDescription += ", ";
			}
		}
		fullDescription += ")";
	}

	public boolean accept(File f) {
		if (f != null) {
			if (f.isDirectory()) {
				return true;
			}
			String extension = getExtension(f);
			if (extension != null && extensionList.contains("*." + extension)
					|| extension == null && extensionList.contains("*")) {
				return true;
			}
		}
		return false;
	}

	public String getDescription() {
		return (fullDescription);
	}
}