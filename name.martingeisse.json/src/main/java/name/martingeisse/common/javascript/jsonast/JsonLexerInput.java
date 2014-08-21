/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.jsonast;

/**
 * Helper class for the {@link JsonLexer}.
 * 
 * This class treats both CR and NL as newline characters
 * independently.
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
	private StringBuilder segment;
	
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
		this.segment = new StringBuilder();
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
	public StringBuilder getSegment() {
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
	 * 
	 */
	private void stepInternalNoNewline() {
		column++;
		inputPosition++;
	}
	
	/**
	 * Reads a segment that represents a number and stores it as the current
	 * segment. This method also sets the 'floatingPoint' flag to indicate
	 * whether a decimal point or exponent sign was found.
	 */
	public void readNumber() {
		segment.setLength(0);
		floatingPoint = false;
		while (inputPosition < inputLength) {
			char c = input.charAt(inputPosition);
			if (c == '.' || c == 'e') {
				floatingPoint = true;
			} else if (c < '0' || c > '9') {
				break;
			}
			stepInternalNoNewline();
			segment.append(c);
		}
	}

	/**
	 * Reads a segment that represents a keyword and stores it as the current
	 * segment.
	 */
	public void readKeyword() {
		segment.setLength(0);
		while (inputPosition < inputLength) {
			char c = input.charAt(inputPosition);
			if (c < 'a' || c > 'z') {
				break;
			}
			stepInternalNoNewline();
			segment.append(c);
		}
	}

	/**
	 * Reads a segment that represents a string literal and stores it as the current
	 * segment.
	 */
	public void readString() {
		stepInternalNoNewline();
		segment.setLength(0);
		while (inputPosition < inputLength) {
			char c = input.charAt(inputPosition);
			if (c == '"') {
				stepInternalNoNewline();
				break;
			} else if (c == '\\') {
				stepInternalNoNewline();
				if (inputPosition == inputLength) {
					throw new JsonLexerInputException("backslash right before EOF");
				}
				c = input.charAt(inputPosition);
				if (!isEscapableCharacter(c)) {
					throw new JsonLexerInputException("invalid escape sequence");
				}
				switch (c) {
				
				// literal escapes
				case '"':
				case '\\':
				case '/':
					segment.append(c);
					stepInternalNoNewline();
					break;
					
				// ASCII backspace
				case 'b':
					segment.append('\b');
					stepInternalNoNewline();
					break;
					
				// ASCII form feed
				case 'f':
					segment.append('\f');
					stepInternalNoNewline();
					break;

				// ASCII form feed
				case 'n':
					segment.append('\n');
					stepInternalNoNewline();
					break;
					
				// ASCII form feed
				case 'r':
					segment.append('\r');
					stepInternalNoNewline();
					break;
					
				// ASCII form feed
				case 't':
					segment.append('\t');
					stepInternalNoNewline();
					break;
					
				// Unicode escapes
				case 'u':
					stepInternalNoNewline();
					if (inputLength - inputPosition < 4) {
						throw new JsonLexerInputException("partial unicode escape before EOF");
					}
					int unicodeValue;
					try {
						unicodeValue = Integer.parseInt(input.subSequence(inputPosition, inputPosition + 4).toString(), 16);
					} catch (NumberFormatException e) {
						throw new JsonLexerInputException("malformed unicode escape");
					}
					segment.append((char)unicodeValue);
					stepInternalNoNewline();
					break;
				}
				
			} else {
				segment.append(c);
				stepInternal(c);
			}
		}
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
			return (c == 'r' || c == 't' || c == 'u');
		}
	}
	
}
