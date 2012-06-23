/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser;

/**
 * This exception type indicates an unexpected XML element.
 */
public class UnexpectedElementException extends ParserException {

	/**
	 * the namespaceUri
	 */
	private final String namespaceUri;
	
	/**
	 * the name
	 */
	private final String name;
	
	/**
	 * the info
	 */
	private final String info;
	
	/**
	 * Constructor.
	 * @param namespaceUri the namespace URI of the unexpected element
	 * @param name the local name of the unexpected element
	 * @param info an informational string about what was expected instead
	 */
	public UnexpectedElementException(String namespaceUri, String name, String info) {
		super(createMessage(namespaceUri, name, info));
		this.namespaceUri = namespaceUri;
		this.name = name;
		this.info = info;
	}

	/**
	 * 
	 */
	private static String createMessage(String namespaceUri, String name, String info) {
		return "unexpected element [" + namespaceUri + "]:" + name + " (" + info + ")";
	}

	/**
	 * Getter method for the namespaceUri.
	 * @return the namespaceUri
	 */
	public String getNamespaceUri() {
		return namespaceUri;
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter method for the info.
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}
	
}
