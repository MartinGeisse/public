/**
 * Copyright (c) 2012 Shopgate GmbH
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
