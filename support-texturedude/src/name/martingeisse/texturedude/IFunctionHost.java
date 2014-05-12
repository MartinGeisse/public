/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.texturedude;

import java.io.EOFException;
import java.io.IOException;

/**
 * Represents the virtual machine as seen by functions.
 */
public interface IFunctionHost {

	/**
	 * Fetches and returns a 1-byte unsigned argument for the current instruction.
	 * Used by functions with arguments.
	 * 
	 * @return the argument byte
	 * @throws EOFException if the program doesn't contain any more bytes
	 * @throws IOException on I/O errors
	 */
	public int fetchArgumentByte() throws EOFException, IOException;

	/**
	 * Fetches and returns a 2-byte unsigned argument for the current instruction.
	 * Used by functions with arguments.
	 * 
	 * @return the next instruction byte
	 * @throws EOFException if the program doesn't contain any more bytes
	 * @throws IOException on I/O errors
	 */
	public int fetchArgumentShort() throws EOFException, IOException;
	
	/**
	 * Returns the virtual machine that holds this function host.
	 * @return the virtual machine
	 */
	public TextureDude getDude();
	
}
