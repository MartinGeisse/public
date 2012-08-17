/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

/**
 * This interface is implemented by objects -- currently only pages --
 * that know their own location in the navigation tree. Pages only
 * need to implement this interface if they do not support the
 * location page parameter.
 */
public interface INavigationLocationAware {

	/**
	 * Determines the current location of this object in the navigation tree.
	 * @return the location of this object, or null if this object doesn't actually know
	 */
	public String getNavigationPath();

}
