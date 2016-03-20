/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.guiserver.xml.attribute;

/**
 * This exception type gets thrown if a required attribute is missing.
 */
public final class MissingAttributeException extends RuntimeException {

	/**
	 * the attributeName
	 */
	private final String attributeName;

	/**
	 * Constructor.
	 * @param attributeName the attribute name
	 */
	public MissingAttributeException(String attributeName) {
		super("attribute '" + attributeName + "' is missing");
		this.attributeName = attributeName;
	}

	/**
	 * Getter method for the attributeName.
	 * @return the attributeName
	 */
	public String getAttributeName() {
		return attributeName;
	}

}
