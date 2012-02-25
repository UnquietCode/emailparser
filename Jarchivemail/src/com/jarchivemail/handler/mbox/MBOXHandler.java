package com.jarchivemail.handler.mbox;

import java.io.InputStream;

import com.jarchivemail.handler.IDetector;
import com.jarchivemail.handler.IHandler;
import com.jarchivemail.metadata.MailType;
import com.jarchivemail.metadata.Metadata;

public class MBOXHandler implements IDetector, IHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6061020745215201523L;

	@Override
	public MailType detect(InputStream input, Metadata metadata) {
		return null;
	}

}
