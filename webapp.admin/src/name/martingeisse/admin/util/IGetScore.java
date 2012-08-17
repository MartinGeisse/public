/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.util;

/**
 * Common interface for objects that have a score. The score is commonly
 * used to establish priority among contributions of different plugins.
 */
public interface IGetScore {

	/**
	 * @return the score
	 */
	public int getScore();

}
