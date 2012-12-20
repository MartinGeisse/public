/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.java;

import javax.tools.FileObject;

/**
 * Common interface for in-memory file implementations.
 */
public interface IMemoryFileObject extends FileObject {

	/**
	 * @return the binary contents of this file
	 */
	public byte[] getBinaryContent();
	
}
