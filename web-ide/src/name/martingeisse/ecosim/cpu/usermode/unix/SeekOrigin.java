/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.usermode.unix;

/**
 * Seek origin selectors. These are passed in calls to the seek() syscall
 * to determine the meaning of (offset == 0). The actual offset is
 * always added to this value.
 */
public enum SeekOrigin {

	/**
	 * (offset == 0) indicates the start of the file.
	 */
	START,
	
	/**
	 * (offset == 0) indicates the current position in the file.
	 */
	CURRENT,
	
	/**
	 * (offset == 0) indicates the end of the file.
	 */
	END;
	
}
