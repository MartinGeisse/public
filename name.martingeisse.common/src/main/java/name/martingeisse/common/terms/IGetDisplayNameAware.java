/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.terms;

/**
 * Implementors are able to return a display name.
 */
public interface IGetDisplayNameAware {

	/**
	 * Getter method for the displayName.
	 * @return the displayName
	 */
	public String getDisplayName();

}
