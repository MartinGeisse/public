package name.martingeisse.esdk.picoblaze.assembler.parser;

import name.martingeisse.esdk.picoblaze.assembler.Range;

/**
 * A token from the .psm input file. All indices (offset, line, column)
 * stored in this class are 0-based. The starting position is inclusive,
 * the ending position is exclusive.
 */
public class Token extends Range {

	/**
	 * This constant is used to indicate the absence of a valid token.
	 */
	public static final int TOKEN_INVALID = -2;

	/**
	 * This constant is used to indicate end-of-file.
	 */
	public static final int TOKEN_EOF = -1;

	/**
	 * This constant is used to indicate a number.
	 */
	public static final int TOKEN_NUMBER = 0;

	/**
	 * This constant is used to indicate an identifier
	 */
	public static final int TOKEN_IDENTIFIER = 1;

	/**
	 * This constant is used to indicate a comma.
	 */
	public static final int TOKEN_COMMA = 100;

	/**
	 * This constant is used to indicate a colon.
	 */
	public static final int TOKEN_COLON = 101;

	/**
	 * This constant is used to indicate an opening parenthesis.
	 */
	public static final int TOKEN_LPAREN = 102;

	/**
	 * This constant is used to indicate a closing parenthesis.
	 */
	public static final int TOKEN_RPAREN = 103;

	/**
	 * This constant is used to indicate a newline character.
	 * The value of this token, if not null, is a comment at
	 * the end of the line, not including the semicolon
	 * or the newline character, and with enclosing whitespace
	 * trimmed.
	 */
	public static final int TOKEN_NEWLINE = 200;

	/**
	 * The token code (one of TOKEN_*)
	 */
	private final int code;

	/**
	 * The value of the token (where applicable)
	 */
	private final Object value;

	/**
	 * Constructor.
	 * @param code the token code (one of TOKEN_*)
	 * @param value the value of the token (where applicable)
	 * @param startOffset the offset of the start of this range
	 * @param startLine the line that contains the start of this range
	 * @param startColumn the column that contains the start of this range
	 * @param endOffset the offset of the end of this range
	 * @param endLine the line that contains the end of this range
	 * @param endColumn the column that contains the end of this range
	 */
	public Token(final int code, final Object value, final int startOffset, final int startLine, final int startColumn, final int endOffset, final int endLine, final int endColumn) {
		super(startOffset, startLine, startColumn, endOffset, endLine, endColumn);
		this.code = code;
		this.value = value;
	}
	
	/**
	 * Getter method for the code.
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Getter method for the value.
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @return the identifier, assuming this is an identifier token.
	 */
	public String getIdentifier() {
		return (String)value;
	}

	/**
	 * @return the numerical value, assuming this is a number token
	 */
	public int getNumber() {
		return (Integer)value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		switch (code) {
		case TOKEN_INVALID:
			return "TOKEN_INVALID";
		case TOKEN_EOF:
			return "TOKEN_EOF";
		case TOKEN_NUMBER:
			return "TOKEN_NUMBER(" + value + ")";
		case TOKEN_IDENTIFIER:
			return "TOKEN_IDENTIFIER(" + value + ")";
		case TOKEN_COMMA:
			return "TOKEN_COMMA";
		case TOKEN_COLON:
			return "TOKEN_COLON";
		case TOKEN_LPAREN:
			return "TOKEN_LPAREN";
		case TOKEN_RPAREN:
			return "TOKEN_RPAREN";
		case TOKEN_NEWLINE:
			if (value == null) {
				return "TOKEN_NEWLINE without comment";
			} else {
				return "TOKEN_NEWLINE with comment: " + value;
			}
		default:
			return "unknown token code";
		}
	}

}
