package com.jarchivemail.handler.mbox;

import java.io.File;
import java.io.FileNotFoundException;

import com.jarchivemail.handler.IHandler;
import com.jarchivemail.handler.LineReader;

/**
 * 
 * @Description MBOX格式邮件处理器
 * @Author zhangzuoqiang
 * @Date 2012-2-25
 */
public class MBOXHandler implements IHandler {

	private File emailFile;

	public MBOXHandler(File file) {
		this.emailFile = file;
	}

	@Override
	public LineReader execute() {
		LineReader lineReader = null;
		try {
			lineReader = new LineReader(this.emailFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return lineReader;
	}

}
