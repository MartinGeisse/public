/**
 * Copyright (c) 2015 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.jtex.engine;

import tex.Tex;

/**
 * Wraps all DVI writing logic.
 *
 * TODO: Currently, the actual data is still stored in {@link Tex}.
 */
public class DviWriter {

	private final Tex tex;

	/**
	 * Constructor.
	 * @param tex the TeX engine
	 */
	public DviWriter(final Tex tex) {
		this.tex = tex;
	}
	
	/**
	 * Writes a byte to the DVI buffer.
	 * 
	 * @param value the byte value to write
	 */
	public void writeByte(int value) {
		tex.dvibuf[tex.dviptr] = value & 0xff;
		tex.dviptr++;
		if (tex.dviptr == tex.dvilimit) {
			tex.dviswap();
		}
	}
	
	/**
	 * Writes a short to the DVI buffer.
	 * 
	 * @param value the short value to write
	 */
	public void writeShort(int value) {
		writeByte(value >> 8);
		writeByte(value);
	}
	
	/**
	 * Writes an int to the DVI buffer.
	 * 
	 * @param value the int value to write
	 */
	public void writeInt(int value) {
		writeByte(value >> 24);
		writeByte(value >> 16);
		writeByte(value >> 8);
		writeByte(value);
	}

}
