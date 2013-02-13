/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.verilog.compiler.wave;

import java.io.IOException;
import java.io.Reader;

/**
 * Splits the characters from a {@link Reader} into whitespace-separated tokens.
 */
final class Tokenizer {

	/**
	 * the reader
	 */
	private final Reader reader;
	
	/**
	 * the builder
	 */
	private final StringBuilder builder;
	
	/**
	 * Constructor.
	 * @param reader the reader to read from
	 */
	public Tokenizer(Reader reader) {
		this.reader = reader;
		this.builder = new StringBuilder();
	}
	
	/**
	 * Reads the next token from the reader.
	 * 
	 * @return the token, or null if there are no more tokens
	 * @throws IOException on I/O errors
	 */
	public String nextToken() throws IOException {
		builder.setLength(0);
		while (true) {
			int c = reader.read();
			if (c == -1) {
				if (builder.length() == 0) {
					return null;
				} else {
					return builder.toString();
				}
			}
			if (c <= 32) {
				if (builder.length() > 0) {
					return builder.toString();
				}
			} else {
				builder.append((char)c);
			}
		}
	}
	
	/**
	 * Like nextToken(), but throws a {@link SyntaxException} if EOF is
	 * found instead of a token.
	 * 
	 * @return the token
	 * @throws IOException on I/O errors
	 * @throws SyntaxException on EOF
	 */
	public String expectToken() throws IOException, SyntaxException {
		String token = nextToken();
		if (token == null) {
			throw new SyntaxException("unexpected EOF");
		}
		return token;
	}

	/**
	 * Like nextToken(), but throws a {@link SyntaxException} if EOF or
	 * a non.keyword token is found.
	 * 
	 * @return the token
	 * @throws IOException on I/O errors
	 * @throws SyntaxException on EOF or a non-keyword token
	 */
	public String expectKeywordToken() throws IOException, SyntaxException {
		String token = expectToken();
		if (token.charAt(0) != '$') {
			throw new SyntaxException("expected keyword");
		}
		return token;
	}
	
	/**
	 * Reads a token and throws a {@link SyntaxException} if it is EOF or
	 * another token than the specified one.
	 * 
	 * @param token the expected token
	 * @throws IOException on I/O errors
	 * @throws SyntaxException if something else than the specified token is found
	 */
	public void expectToken(String token) throws IOException, SyntaxException {
		if (!token.contentEquals(expectToken())) {
			throw new SyntaxException("expected: " + token);
		}
	}
	
	/**
	 * Reads a token and throws a {@link SyntaxException} if it is not 
	 * the $end keyword.
	 * 
	 * @throws IOException on I/O errors
	 * @throws SyntaxException if something else than the $end keyword is found
	 */
	public void expectEnd() throws IOException, SyntaxException {
		expectToken("$end");
	}
	
	/**
	 * Reads an integer number (in decimal format) and returns it as an int value.
	 * 
	 * @return the number
	 * @throws IOException on I/O errors
	 * @throws SyntaxException if EOF or a non-number value is found
	 */
	public int expectInt() throws IOException, SyntaxException {
		String token = expectToken();
		try {
			return Integer.parseInt(token);
		} catch (NumberFormatException e) {
			throw new SyntaxException("expected number");
		}
	}
	
	/**
	 * Reads an integer number (in decimal format) and returns it as a long value.
	 * 
	 * @return the number
	 * @throws IOException on I/O errors
	 * @throws SyntaxException if EOF or a non-number value is found
	 */
	public long expectLong() throws IOException, SyntaxException {
		String token = expectToken();
		try {
			return Long.parseLong(token);
		} catch (NumberFormatException e) {
			throw new SyntaxException("expected number");
		}
	}
	
}
