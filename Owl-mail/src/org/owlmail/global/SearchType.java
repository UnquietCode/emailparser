package org.owlmail.global;

public enum SearchType {

	CONTENT(1), PERSON(2), RECEIVER_ONLY(3), SENDER_ONLY(4), SUBJECT(5);

	private int type;

	private SearchType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
}
