package com.jarchivemail;

import java.io.File;

import com.jarchivemail.handler.IHandler;
import com.jarchivemail.handler.LineReader;
import com.jarchivemail.handler.MailType;
import com.jarchivemail.parser.AbstractParser;
import com.jarchivemail.parser.Archivemail;
import com.jarchivemail.parser.mbox.MBOXParser;

public class MainApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File file = new File("D:\\Desktop\\mbox\\testMbox.mbox");
		IHandler handler = MailType.autoDetect(file);
		LineReader lReader = handler.execute();
		Archivemail archivemail = new Archivemail(lReader);
		AbstractParser parser = new MBOXParser(archivemail);
		System.out.println(parser.archivemail().toString());
	}

}
