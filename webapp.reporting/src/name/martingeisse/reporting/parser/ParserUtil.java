/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser;

/**
 * Utility methods for the report definition parser.
 */
public class ParserUtil {

	/**
	 * Checks whether the specified element is a specific element from the "core" namespace.
	 * @param namespaceUri the namespace URI of the element to check
	 * @param name the name of the element to check
	 * @param expectedName the known name to check for
	 * @return true if the element is the expected known element, false if not
	 */
	public static boolean isCoreElement(String namespaceUri, String name, String expectedName) {
		return (ParserConstants.CORE_NAMESPACE.equals(namespaceUri) && expectedName.equals(name));
	}

}
