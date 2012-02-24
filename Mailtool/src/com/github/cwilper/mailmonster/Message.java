package com.github.cwilper.mailmonster;

import java.io.PrintWriter;

public interface Message {

	String getFromLine();

	Header getHeader();

	void writeBody(PrintWriter writer) throws Exception;

}
