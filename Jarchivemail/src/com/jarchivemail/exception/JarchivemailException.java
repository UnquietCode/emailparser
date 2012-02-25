package com.jarchivemail.exception;

public class JarchivemailException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3015383583064029633L;

	public JarchivemailException(String msg) {
		super(msg);
	}

	public JarchivemailException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
