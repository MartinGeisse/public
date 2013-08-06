/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.docfmt.core;

import javax.xml.stream.XMLEventWriter;

/**
 * Hides the logic that consumes the output XSL:FO document.
 */
public interface IOutputConsumer {

	/**
	 * This method is used to provide the {@link XMLEventWriter} for the main output document.
	 * 
	 * @return the main output writer
	 */
	public XMLEventWriter getMainOutput();

	/**
	 * Releases all resources allocated by the above methods. The returned event writers
	 * are no longer valid after calling this method.
	 */
	public void close();

}
