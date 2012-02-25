package com.jarchivemail.parser;

/**
 * 
 * @Description
 * @Author zhangzuoqiang
 * @Date 2012-2-25
 */
public abstract class AbstractParser {

	protected Archivemail archivemail;

	public AbstractParser(Archivemail _archivemail) {
		this.archivemail = _archivemail;

	}

	public Archivemail archivemail() {
		return archivemail;
	}

}
