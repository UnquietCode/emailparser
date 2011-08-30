/**
 * @Package: factory.parser
 * @Title: RTFParserTokenManager.java
 * @Author: zhangzuoqiang
 * @Time: 6:15:57 PM Aug 29, 2011
 * @Version: 
 */
package factory.parser;

import java.io.PrintStream;

import exception.Token;

/**
 * @Description:
 * @Author: zhangzuoqiang
 * @Date: Aug 29, 2011
 */
public class RTFParserTokenManager implements RTFParserConstants {
	
	public PrintStream debugStream = System.out;

	public void setDebugStream(java.io.PrintStream ds) {
		debugStream = ds;
	}

	private final int jjStopStringLiteralDfa_0(int pos, long active0) {
		switch (pos) {
		case 0:
			if ((active0 & 0x1aL) != 0L)
				return 12;
			if ((active0 & 0x2720L) != 0L)
				return 14;
			return -1;
		default:
			return -1;
		}
	}

	private final int jjStartNfa_0(int pos, long active0) {
		return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
	}

	private final int jjStopAtPos(int pos, int kind) {
		jjmatchedKind = kind;
		jjmatchedPos = pos;
		return pos + 1;
	}

	private final int jjStartNfaWithStates_0(int pos, int kind, int state) {
		jjmatchedKind = kind;
		jjmatchedPos = pos;
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			return pos + 1;
		}
		return jjMoveNfa_0(state, pos + 1);
	}

	private final int jjMoveStringLiteralDfa0_0() {
		switch (curChar) {
		case 13:
			jjmatchedKind = 3;
			return jjMoveStringLiteralDfa1_0(0x10L);
		case 32:
			return jjStartNfaWithStates_0(0, 1, 12);
		case 92:
			jjmatchedKind = 5;
			return jjMoveStringLiteralDfa1_0(0x2700L);
		case 123:
			return jjStopAtPos(0, 11);
		case 125:
			return jjStopAtPos(0, 12);
		default:
			return jjMoveNfa_0(11, 0);
		}
	}

	private final int jjMoveStringLiteralDfa1_0(long active0) {
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			jjStopStringLiteralDfa_0(0, active0);
			return 1;
		}
		switch (curChar) {
		case 10:
			if ((active0 & 0x10L) != 0L)
				return jjStopAtPos(1, 4);
			break;
		case 45:
			if ((active0 & 0x200L) != 0L)
				return jjStopAtPos(1, 9);
			break;
		case 92:
			if ((active0 & 0x2000L) != 0L)
				return jjStopAtPos(1, 13);
			break;
		case 95:
			if ((active0 & 0x400L) != 0L)
				return jjStopAtPos(1, 10);
			break;
		case 126:
			if ((active0 & 0x100L) != 0L)
				return jjStopAtPos(1, 8);
			break;
		default:
			break;
		}
		return jjStartNfa_0(0, active0);
	}

	private final void jjCheckNAdd(int state) {
		if (jjrounds[state] != jjround) {
			jjstateSet[jjnewStateCnt++] = state;
			jjrounds[state] = jjround;
		}
	}

	@SuppressWarnings("unused")
	private final void jjAddStates(int start, int end) {
		do {
			jjstateSet[jjnewStateCnt++] = jjnextStates[start];
		} while (start++ != end);
	}

	@SuppressWarnings("unused")
	private final void jjCheckNAddTwoStates(int state1, int state2) {
		jjCheckNAdd(state1);
		jjCheckNAdd(state2);
	}

	private final void jjCheckNAddStates(int start, int end) {
		do {
			jjCheckNAdd(jjnextStates[start]);
		} while (start++ != end);
	}

	@SuppressWarnings("unused")
	private final void jjCheckNAddStates(int start) {
		jjCheckNAdd(jjnextStates[start]);
		jjCheckNAdd(jjnextStates[start + 1]);
	}

	static final long[] jjbitVec0 = { 0x0L, 0x0L, 0xffffffffffffffffL,
			0xffffffffffffffffL };

	@SuppressWarnings("unused")
	private final int jjMoveNfa_0(int startState, int curPos) {
		int startsAt = 0;
		jjnewStateCnt = 21;
		int i = 1;
		jjstateSet[0] = startState;
		int kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 11:
					case 12:
						if ((0xfffffffffffffbffL & l) == 0L)
							break;
						if (kind > 18)
							kind = 18;
						jjCheckNAdd(12);
						break;
					case 14:
						if ((0x3ff000000000000L & l) != 0L) {
							if (kind > 16)
								kind = 16;
							jjCheckNAdd(19);
						} else if (curChar == 42) {
							if (kind > 17)
								kind = 17;
						} else if (curChar == 39)
							jjstateSet[jjnewStateCnt++] = 17;
						break;
					case 1:
						if ((0x3ff000000000000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 2;
						break;
					case 2:
						if ((0x3ff000000000000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 3;
						break;
					case 3:
						if ((0x3ff000000000000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 4;
						break;
					case 4:
						if ((0x3ff000000000000L & l) != 0L && kind > 15)
							kind = 15;
						break;
					case 16:
						if (curChar == 39)
							jjstateSet[jjnewStateCnt++] = 17;
						break;
					case 17:
						if ((0x3ff000000000000L & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 18;
						break;
					case 18:
						if ((0x3ff000000000000L & l) != 0L && kind > 14)
							kind = 14;
						break;
					case 19:
						if ((0x3ff000000000000L & l) == 0L)
							break;
						if (kind > 16)
							kind = 16;
						jjCheckNAdd(19);
						break;
					case 20:
						if (curChar == 42 && kind > 17)
							kind = 17;
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 11:
						if ((0xd7ffffffefffffffL & l) != 0L) {
							if (kind > 18)
								kind = 18;
							jjCheckNAdd(12);
						} else if (curChar == 92)
							jjCheckNAddStates(0, 4);
						if (curChar == 92)
							jjstateSet[jjnewStateCnt++] = 10;
						break;
					case 14:
						if ((0x7fffffe07fffffeL & l) != 0L) {
							if (kind > 16)
								kind = 16;
							jjCheckNAdd(19);
						} else if (curChar == 125) {
							if (kind > 7)
								kind = 7;
						} else if (curChar == 123) {
							if (kind > 6)
								kind = 6;
						}
						if (curChar == 97)
							jjstateSet[jjnewStateCnt++] = 9;
						break;
					case 0:
						if (curChar == 103)
							jjstateSet[jjnewStateCnt++] = 1;
						break;
					case 5:
						if (curChar == 112)
							jjstateSet[jjnewStateCnt++] = 0;
						break;
					case 6:
						if (curChar == 99)
							jjstateSet[jjnewStateCnt++] = 5;
						break;
					case 7:
						if (curChar == 105)
							jjstateSet[jjnewStateCnt++] = 6;
						break;
					case 8:
						if (curChar == 115)
							jjstateSet[jjnewStateCnt++] = 7;
						break;
					case 9:
						if (curChar == 110)
							jjstateSet[jjnewStateCnt++] = 8;
						break;
					case 10:
						if (curChar == 97)
							jjstateSet[jjnewStateCnt++] = 9;
						break;
					case 12:
						if ((0xd7ffffffefffffffL & l) == 0L)
							break;
						if (kind > 18)
							kind = 18;
						jjCheckNAdd(12);
						break;
					case 13:
						if (curChar == 92)
							jjCheckNAddStates(0, 4);
						break;
					case 15:
						if (curChar == 125)
							kind = 7;
						break;
					case 17:
						if ((0x7fffffe07fffffeL & l) != 0L)
							jjstateSet[jjnewStateCnt++] = 18;
						break;
					case 18:
						if ((0x7fffffe07fffffeL & l) != 0L && kind > 14)
							kind = 14;
						break;
					case 19:
						if ((0x7fffffe07fffffeL & l) == 0L)
							break;
						if (kind > 16)
							kind = 16;
						jjCheckNAdd(19);
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 11:
					case 12:
						if ((jjbitVec0[i2] & l2) == 0L)
							break;
						if (kind > 18)
							kind = 18;
						jjCheckNAdd(12);
						break;
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 21 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	static final int[] jjnextStates = { 14, 15, 16, 19, 20, };
	public static final String[] jjstrLiteralImages = { "", null, null, null,
			null, "\134", null, null, "\134\176", "\134\55", "\134\137",
			"\173", "\175", "\134\134", null, null, null, null, null, };
	public static final String[] lexStateNames = { "DEFAULT", };
	static final long[] jjtoToken = { 0x7ffe1L, };
	static final long[] jjtoSkip = { 0x1eL, };
	protected SimpleCharStream input_stream;
	private final int[] jjrounds = new int[21];
	private final int[] jjstateSet = new int[42];
	protected char curChar;

	public RTFParserTokenManager(SimpleCharStream stream) {
		if (SimpleCharStream.staticFlag)
			throw new Error(
					"ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
		input_stream = stream;
	}

	public RTFParserTokenManager(SimpleCharStream stream, int lexState) {
		this(stream);
		SwitchTo(lexState);
	}

	public void ReInit(SimpleCharStream stream) {
		jjmatchedPos = jjnewStateCnt = 0;
		curLexState = defaultLexState;
		input_stream = stream;
		ReInitRounds();
	}

	private final void ReInitRounds() {
		int i;
		jjround = 0x80000001;
		for (i = 21; i-- > 0;)
			jjrounds[i] = 0x80000000;
	}

	public void ReInit(SimpleCharStream stream, int lexState) {
		ReInit(stream);
		SwitchTo(lexState);
	}

	public void SwitchTo(int lexState) {
		if (lexState >= 1 || lexState < 0)
			throw new TokenMgrError("Error: Ignoring invalid lexical state : "
					+ lexState + ". State unchanged.",
					TokenMgrError.INVALID_LEXICAL_STATE);
		else
			curLexState = lexState;
	}

	protected Token jjFillToken() {
		Token t = Token.newToken(jjmatchedKind);
		t.kind = jjmatchedKind;
		String im = jjstrLiteralImages[jjmatchedKind];
		t.image = (im == null) ? input_stream.GetImage() : im;
		t.beginLine = input_stream.getBeginLine();
		t.beginColumn = input_stream.getBeginColumn();
		t.endLine = input_stream.getEndLine();
		t.endColumn = input_stream.getEndColumn();
		return t;
	}

	int curLexState = 0;
	int defaultLexState = 0;
	int jjnewStateCnt;
	int jjround;
	int jjmatchedPos;
	int jjmatchedKind;

	public Token getNextToken() {
		Token matchedToken;
		int curPos = 0;

		EOFLoop: for (;;) {
			try {
				curChar = input_stream.BeginToken();
			} catch (java.io.IOException e) {
				jjmatchedKind = 0;
				matchedToken = jjFillToken();
				return matchedToken;
			}

			try {
				input_stream.backup(0);
				while (curChar <= 10 && (0x400L & (1L << curChar)) != 0L)
					curChar = input_stream.BeginToken();
			} catch (java.io.IOException e1) {
				continue EOFLoop;
			}
			jjmatchedKind = 0x7fffffff;
			jjmatchedPos = 0;
			curPos = jjMoveStringLiteralDfa0_0();
			if (jjmatchedKind != 0x7fffffff) {
				if (jjmatchedPos + 1 < curPos)
					input_stream.backup(curPos - jjmatchedPos - 1);
				if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
					matchedToken = jjFillToken();
					return matchedToken;
				} else {
					continue EOFLoop;
				}
			}
			int error_line = input_stream.getEndLine();
			int error_column = input_stream.getEndColumn();
			String error_after = null;
			boolean EOFSeen = false;
			try {
				input_stream.readChar();
				input_stream.backup(1);
			} catch (java.io.IOException e1) {
				EOFSeen = true;
				error_after = curPos <= 1 ? "" : input_stream.GetImage();
				if (curChar == '\n' || curChar == '\r') {
					error_line++;
					error_column = 0;
				} else
					error_column++;
			}
			if (!EOFSeen) {
				input_stream.backup(1);
				error_after = curPos <= 1 ? "" : input_stream.GetImage();
			}
			throw new TokenMgrError(EOFSeen, curLexState, error_line,
					error_column, error_after, curChar,
					TokenMgrError.LEXICAL_ERROR);
		}
	}

}