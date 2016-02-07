/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package tex;

import java.util.Arrays;
import name.martingeisse.jtex.io.Input;

/**
 * TeX's input buffer handling, moved to its own class to clean things up.
 * The actual buffer is still in the {@link Tex} object, but all handling
 * methods are moved to this class. Eventually, the buffer itself will be
 * moved here. Then, I'll decide if the buffer will be kept in the code
 * or broken up into individual buffers in input stack elements. TeX uses
 * a unified buffer probably to save memory.
 */
public final class PrelimInputBuffer {

	/**
	 * the tex
	 */
	private final Tex tex;

	/**
	 * Constructor.
	 * @param tex the tex engine
	 */
	PrelimInputBuffer(Tex tex) {
		this.tex = tex;
	}
	
	/**
	 * Initializes and clears the input buffer.
	 */
	public void initialize() {
		Arrays.fill(tex.buffer, 0);
		tex.first = 0;
		// TODO last ?
	}
	
	/**
	 * Reads a line of input from the specified character file and appends
	 * it to the input buffer, starting at the 'first' field. Sets the
	 * 'last' field to the *first unused* character, such that if last=first,
	 * then the resulting line is empty.
	 * 
	 * TODO: what about the newline character?
	 * 
	 * Edge case: Spaces that are already present at the end of the input
	 * buffer before this function gets called are *not* removed by
	 * this, only end-of-line spaces actually read from the file during
	 * this call are.
	 * 
	 * @param input the input to read from
	 * @return true if successful, false on EOF
	 */
	public boolean appendRightTrimmedLineFromFile(final Input input) {
		tex.last = tex.first;
		String line = input.readLine();
		if (line == null) {
			return false;
		}
		// TODO use StringUtils to right-trim
		while (line.endsWith(" ")) {
			line = line.substring(0, line.length() - 1);
		}
		for (int i=0; i<line.length(); i++) {
			tex.buffer[tex.last] = line.charAt(i);
			tex.last++;
		}
		return true;
	}

	/**
	 * Appends the specified character sequence as a line, starting at the
	 * 'first' field, and setting the 'last' field to the *first unused*
	 * character, such that if last=first, then the resulting line is empty.
	 * 
	 * @param s the line to append
	 */
	public void appendLine(final CharSequence s) {
		for (int i = 0; i < s.length(); i++) {
			tex.buffer[tex.first + i] = s.charAt(i);
		}
		tex.last = tex.first + s.length();
	}

	/**
	 * Checks whether the current input line, starting at 'first' (inclusive)
	 * and ending at 'last' (exclusive), respectively.
	 * 
	 * @return true if empty, false if not
	 */
	public boolean isEmpty() {
		return (tex.last <= tex.first);
	}
	
	/**
	 * Copies characters from the specified source to the internal buffer backing the input buffer.
	 * 
	 * @param source the source buffer to copy from
	 * @param sourceStart the index in the source buffer of the first character to copy
	 * @param bufferStart the index in the input buffer of the first character to copy
	 * @param length the number of characters to copy
	 */
	public void copyToInternalBuffer(int[] source, int sourceStart, int bufferStart, int length) {
		System.arraycopy(source, sourceStart, tex.buffer, bufferStart, length);
	}
	
}
