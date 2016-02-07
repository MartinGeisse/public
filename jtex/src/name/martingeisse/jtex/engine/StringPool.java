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

	/**
	 * Constructor.
	 * @param tex the TeX engine
	 */
	public StringPool(final Tex tex) {
		this.tex = tex;
	}

	/**
	 * Resets the pool to empty.
	 */
	public void reset() {
		tex.poolptr = 0;
		tex.strptr = 0;
		tex.strstart[0] = 0;
	}
	
	/**
	 * Creates the 256 one-character strings (or their replacements) for initex.
	 */
	public void createCharacterStrings() {
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
	public void loadPoolFile(File poolFile) {
		try (Input poolfile = Input.from(poolFile)) {
			while(true) {
				int initialCharacter = poolfile.readCharacter();
				if (initialCharacter < 0) {
					throw new RuntimeException("missing checksum in tex.pool");
				} else if (initialCharacter == '*') {
					// the initial value for x is the reversed expected checksum
					for (int x = 218082072; x != 0; x >>>= 1) {
						if (poolfile.readCharacter() != (x % 10)) {
							throw new RuntimeException("pool file "+ poolFile + " has invalid checksum");
						}
					}
					break;
				} else {
					int secondaryCharacter = poolfile.readCharacter();
					if (initialCharacter < '0' || initialCharacter > '9' || secondaryCharacter < '0' || secondaryCharacter > '9') {
						throw new RuntimeException("tex.pool line doesn't begin with two digits");
					}
					int desiredLength = (initialCharacter - '0') * 10 + (secondaryCharacter - '0');
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
	public String getString(int stringId) {
		StringBuilder builder = new StringBuilder();
		int start = tex.strstart[stringId];
		int end = tex.strstart[stringId + 1];
		for (int i = start; i < end; i++) {
			builder.append((char)tex.strpool[i]);
		}
		return builder.toString();
	}

	/**
	 * Appends the specified character to the string being built.
	 * @param c the character to append
	 */
	public void append(char c) {
		tex.strpool[tex.poolptr] = c;
		tex.poolptr++;
	}

	/**
	 * Appends a (lowercase) hex digit to the string being built.
	 * @param digit the digit in the range 0..15
	 */
	public void appendHexDigit(int digit) {
		append((char)(digit < 10 ? ('0' + digit) : ('a' + digit - 10)));
	}

	/**
	 * Finishes building a string and returns its newly assigned ID.
	 * 
	 * @return the string ID
	 */
	public int makeString() {
		if (tex.strptr == Tex.maxstrings) {
			throw new RuntimeException("too many strings in the string pool");
		}
		tex.strptr = tex.strptr + 1;
		tex.strstart[tex.strptr] = tex.poolptr;
		return tex.strptr - 1;
	}

}
