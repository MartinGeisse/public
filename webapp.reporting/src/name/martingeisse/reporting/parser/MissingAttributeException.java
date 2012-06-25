/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser;

/**
 * This exception type indicates that an attribute was expected for
 * an element but no such attribute was found.
 */
public class MissingAttributeException extends ParserException {

	/**
	 * the elementNamespace
	 */
	private final String elementNamespace;
	
	/**
	 * the elementName
	 */
	private final String elementName;
	
	/**
	 * the attributeName
	 */
	private final String attributeName;
	
	/**
	 * Constructor.
	 * @param elementNamespace the namespace URI of the element for which an attribute is missing
	 * @param elementName the name of the element for which an attribute is missing
	 * @param attributeName the name of the missing attribute
	 */
	public MissingAttributeException(String elementNamespace, String elementName, String attributeName) {
		super(createMessage(elementNamespace, elementName, attributeName));
		this.elementNamespace = elementNamespace;
		this.elementName = elementName;
		this.attributeName = attributeName;
	}

	/**
	 * 
	 */
	private static String createMessage(String elementNamespace, String elementName, String attributeName) {
		return "element [" + elementNamespace + "]:" + elementName + " is missing attribute " + attributeName;
	}
	
	/**
	 * Getter method for the elementNamespace.
	 * @return the elementNamespace
	 */
	public String getElementNamespace() {
		return elementNamespace;
	}
	
	/**
	 * Getter method for the elementName.
	 * @return the elementName
	 */
	public String getElementName() {
		return elementName;
	}
	
	/**
	 * Getter method for the attributeName.
	 * @return the attributeName
	 */
	public String getAttributeName() {
		return attributeName;
	}
	
}
