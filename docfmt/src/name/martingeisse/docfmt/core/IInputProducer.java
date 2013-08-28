/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.docfmt.core;

import javax.xml.stream.XMLEventReader;

/**
 * Hides the logic that provides the input XML document.
 */
public interface IInputProducer {

	/**
	 * This method is used first to provide the {@link XMLEventReader}
	 * for the main document.
	 * 
	 * @return the main input source
	 */
	public XMLEventReader getMainInput();

	/**
	 * This method is used for included input files.
	 * 
	 * @param specifier the input specifier, e.g. the name or path of an included file
	 * @return the included input source
	 */
	public XMLEventReader getIncludedInput(String specifier);

	/**
	 * Releases all resources allocated by the above methods. The returned event readers
	 * are no longer valid after calling this method.
	 */
	public void close();
	
}
