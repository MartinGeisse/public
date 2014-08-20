/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.jsonast;

/**
 * Helper class for the {@link JsonLexer}.
 */
final class JsonLexerInput {

	/**
	 * the input
	 */
	private final CharSequence input;
	
	/**
	 * the inputLength
	 */
	private final int inputLength;
	
	/**
	 * the inputPosition
	 */
	private int inputPosition;
	
	/**
	 * the line
	 */
	private int line;
	
	/**
	 * the column
	 */
	private int column;
	
	/**
	 * the segment
	 */
	private String segment;
	
	/**
	 * the floatingPoint
	 */
	private boolean floatingPoint;
	
	/**
	 * Constructor.
	 * @param input the input to split into tokens
	 */
	public JsonLexerInput(CharSequence input) {
		this.input = input;
		this.inputLength = input.length();
		this.inputPosition = 0;
		this.line = 0;
		this.column = 0;
	}

	/**
	 * Getter method for the line.
	 * @return the line
	 */
	public int getLine() {
		return line;
	}
	
	/**
	 * Getter method for the column.
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}
	
	/**
	 * Reads the current character from the input
	 * @return the current character, or -1 for EOF
	 */
	public int getCurrentCharacter() {
		return (inputPosition < inputLength ? input.charAt(inputPosition) : -1);
	}
	
	/**
	 * Getter method for the segment.
	 * @return the segment
	 */
	public String getSegment() {
		return segment;
	}
	
	/**
	 * Getter method for the floatingPoint.
	 * @return the floatingPoint
	 */
	public boolean isFloatingPoint() {
		return floatingPoint;
	}
	
	/**
	 * Steps to the next input character. Has no effect if at EOF.
	 */
	public void step() {
		if (inputPosition < inputLength) {
			stepInternal(input.charAt(inputPosition));
		}
	}
	
	/**
	 * Skips all current whitespace characters in the input.
	 */
	public void skipSpaces() {
		while (inputPosition < inputLength) {
			char c = input.charAt(inputPosition);
			if (c > 32) {
				break;
			} else {
				stepInternal(c);
			}
		}
	}
	
	/**
	 * 
	 */
	private void stepInternal(char c) {
		if (c == '\n' || c == '\r') {
			line++;
			column = 0;
		} else {
			column++;
		}
		inputPosition++;
	}

	/**
	 * Reads a segment that represents a number and stores it as the current
	 * segment. This method also sets the 'floatingPoint' flag to indicate
	 * whether a decimal point or exponent sign was found.
	 */
	public void readNumber() {
		int startPosition = inputPosition;
		floatingPoint = false;
		while (inputPosition < inputLength) {
			char c = input.charAt(inputPosition);
			if (c == '.' || c == 'e') {
				floatingPoint = true;
			} else if (c < '0' || c > '9') {
				break;
			}
			column++;
			inputPosition++;
		}
		segment = input.subSequence(startPosition, inputPosition).toString();
	}

	/**
	 * Reads a segment that represents a keyword and stores it as the current
	 * segment.
	 */
	public void readKeyword() {
		int startPosition = inputPosition;
		while (inputPosition < inputLength) {
			char c = input.charAt(inputPosition);
			if (c < 'a' || c > 'z') {
				break;
			}
			column++;
			inputPosition++;
		}
		segment = input.subSequence(startPosition, inputPosition).toString();
	}

	/**
	 * Reads a segment that represents a string literal and stores it as the current
	 * segment.
	 * 
	 * TODO cannot handle unicode escapes yet
	 */
	public void readString() {
		
		// skip the initial quote character
		this.column++;
		this.inputPosition++;
		int contentStartPosition = inputPosition;
		
		// find content
		boolean escaped = false;
		while (inputPosition < inputLength) {
			char c = input.charAt(inputPosition);
			if (escaped) {
				if (isEscapableCharacter(c)) {
					escaped = false;
				} else {
					// TODO error
					escaped = false;
				}
			} else if (c == '\\') {
				escaped = true;
			} else if (c == '"') {
				break;
			}
			inputPosition++;
		}
		int contentEndPosition = inputPosition;
		
		// skip the final quote character
		this.column++;
		this.inputPosition++;

		// extract content and apply escaping TODO slow
		segment = input.subSequence(contentStartPosition, contentEndPosition).toString();
		segment = segment.replace("\\b", "\b");
		segment = segment.replace("\\f", "\f");
		segment = segment.replace("\\n", "\n");
		segment = segment.replace("\\r", "\r");
		segment = segment.replace("\\t", "\t");
		segment = segment.replace("\\\"", "\"");
		segment = segment.replace("\\/", "/");
		segment = segment.replace("\\\\", "\\");
		
	}
	
	/**
	 * Checks whether the specified character may appear after a
	 * backslash inside a string literal.
	 * 
	 * @param c the character
	 * @return true if escapable, false if not
	 */
	static boolean isEscapableCharacter(char c) {
		if (c < 'a') {
			return (c == '"' || c == '\\' || c == '/');
		} else if (c < 'o') {
			return (c == 'b' || c == 'f' || c == 'n');
		} else {
			return (c == 'r' || c == 't'); // TODO || c == 'u');
		}
	}
	
}
