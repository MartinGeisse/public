/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.util;

/**
 * Represents a container for name/value parameters in an abstract way.
 */
public interface IParameterSet {

	/**
	 * Returns the value of the specified parameter, or null if not found.
	 * @param name the parameter name
	 * @return the parameter value
	 */
	public String getParameter(String name);
	
}
