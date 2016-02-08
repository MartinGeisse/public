/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.jtex.engine;

import java.io.File;
import name.martingeisse.jtex.io.Input;
import tex.Tex;

/**
 * Stores string constants.
 *
 * TODO: Currently, the actual data is still stored in {@link Tex}.
 *
 * TODO: the code has already been modified for an infinite pool (checks removed), so the pool
 * should be made infinite too
 */
public final class StringPool {

	/**
	 * TODO remove once the pool doesn't have a fixed size anymore
	 */
	public static final int stringvacancies = 16000;

	private final Tex tex;
	private StringBuilder interceptingStringBuilder;

	/**
	 * Constructor.
	 * @param tex the TeX engine
	 */
	public StringPool(final Tex tex) {
		this.tex = tex;
	}

	/**
	 * @return the number of strings in the pool
	 */
	public int getStringCount() {
		return tex.strptr;
	}

	/**
	 * Getter method for the interceptingStringBuilder.
	 * @return the interceptingStringBuilder
	 */
	public StringBuilder getInterceptingStringBuilder() {
		return interceptingStringBuilder;
	}

	/**
	 * Setter method for the interceptingStringBuilder.
	 * @param interceptingStringBuilder the interceptingStringBuilder to set
	 */
	public void setInterceptingStringBuilder(final StringBuilder interceptingStringBuilder) {
		this.interceptingStringBuilder = interceptingStringBuilder;
	}

	/**
	 * Resets the pool to empty.
	 */
	public void reset() {
		disallowInterceptingStringBuilder();
		tex.poolptr = 0;
		tex.strptr = 0;
		tex.strstart[0] = 0;
	}
	
	/**
	 * 
	 */
	private void disallowInterceptingStringBuilder() {
		if (interceptingStringBuilder != null) {
			throw new IllegalStateException("cannot call this method while an intercepting string builder is set");
		}
	}

	/**
	 * Creates the 256 one-character strings (or their replacements) for initex.
	 */
	public void createCharacterStrings() {
		disallowInterceptingStringBuilder();
		for (int k = 0; k < 256; k++) {
			if (k < 32) {
				append('^');
				append('^');
				append((char)(k + 64));
			} else if (k < 127) {
				append((char)k);
			} else if (k < 128) {
				append((char)(k - 64));
			} else {
				append('^');
				append('^');
				appendHexDigit(k >> 4);
				appendHexDigit(k & 15);
			}
			makeString();
		}
	}

	/**
	 * Loads strings from the specified pool file.
	 *
	 * @param poolFile the pool file
	 */
	public void loadPoolFile(final File poolFile) {
		disallowInterceptingStringBuilder();
		try (Input poolfile = Input.from(poolFile)) {
			while (true) {
				final int initialCharacter = poolfile.readCharacter();
				if (initialCharacter < 0) {
					throw new RuntimeException("missing checksum in tex.pool");
				} else if (initialCharacter == '*') {
					// the initial value for x is the reversed expected checksum
					for (int x = 218082072; x != 0; x >>>= 1) {
						if (poolfile.readCharacter() != (x % 10)) {
							throw new RuntimeException("pool file " + poolFile + " has invalid checksum");
						}
					}
					break;
				} else {
					final int secondaryCharacter = poolfile.readCharacter();
					if (initialCharacter < '0' || initialCharacter > '9' || secondaryCharacter < '0' || secondaryCharacter > '9') {
						throw new RuntimeException("tex.pool line doesn't begin with two digits");
					}
					final int desiredLength = (initialCharacter - '0') * 10 + (secondaryCharacter - '0');
					String line = poolfile.readLine();
					if (line.length() > desiredLength) {
						line = line.substring(0, desiredLength);
					}
					while (line.length() < desiredLength) {
						line += ' ';
					}
					makeString();
				}
			}
		}
	}

	/**
	 * Returns a string by ID.
	 *
	 * @param stringId the string ID
	 * @return the string
	 */
	public String getString(final int stringId) {
		if (stringId < 0 || stringId >= tex.strptr) {
			throw new IndexOutOfBoundsException("invalid string ID: " + stringId);
		}
		final StringBuilder builder = new StringBuilder();
		final int start = tex.strstart[stringId];
		final int end = tex.strstart[stringId + 1];
		for (int i = start; i < end; i++) {
			builder.append((char)tex.strpool[i]);
		}
		return builder.toString();
	}

	/**
	 * Appends the specified character to the string being built.
	 * @param c the character to append
	 */
	public void append(final char c) {
		if (interceptingStringBuilder == null) {
			tex.strpool[tex.poolptr] = c;
			tex.poolptr++;
		} else {
			interceptingStringBuilder.append(c);
		}
	}

	/**
	 * Appends a (lowercase) hex digit to the string being built.
	 * @param digit the digit in the range 0..15
	 */
	public void appendHexDigit(final int digit) {
		append((char)(digit < 10 ? ('0' + digit) : ('a' + digit - 10)));
	}

	/**
	 * Finishes building a string and returns its newly assigned ID.
	 *
	 * @return the string ID
	 */
	public int makeString() {
		disallowInterceptingStringBuilder();
		if (tex.strptr == Tex.maxstrings) {
			throw new RuntimeException("too many strings in the string pool");
		}
		tex.strptr = tex.strptr + 1;
		tex.strstart[tex.strptr] = tex.poolptr;
		return tex.strptr - 1;
	}

}
