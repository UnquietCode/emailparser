package com.jarchivemail.parser;


/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-25
 */
public abstract class AbstractParser {

	public Archivemail archivemail;

	public AbstractParser(Archivemail _archivemail) {
		this.archivemail = _archivemail;

	}
}
