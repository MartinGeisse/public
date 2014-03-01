/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package tex;

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
	
}
