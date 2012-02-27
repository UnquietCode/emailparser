package com.eams.mboxparser;

import java.io.File;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MboxParser parser = new MboxParser(new File(
				"D:\\Desktop\\mbox\\testMbox.mbox"));
		
		System.out.println(parser.getAttachmentFileNames(0).toString());
		System.out.println(parser.toString());
	}

}
