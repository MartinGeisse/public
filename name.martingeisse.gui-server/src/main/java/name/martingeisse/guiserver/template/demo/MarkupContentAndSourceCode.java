/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template.demo;

import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.MarkupContent;


/**
 * Wraps a piece of markup content and its XML source code.
 */
public final class MarkupContentAndSourceCode {

	/**
	 * the markupContent
	 */
	private final MarkupContent<ComponentGroupConfiguration> markupContent;

	/**
	 * the sourceCode
	 */
	private final String sourceCode;

	/**
	 * Constructor.
	 * @param markupContent the parsed markup content
	 * @param sourceCode the source code
	 */
	public MarkupContentAndSourceCode(MarkupContent<ComponentGroupConfiguration> markupContent, String sourceCode) {
		this.markupContent = markupContent;
		this.sourceCode = sourceCode;
	}

	/**
	 * Getter method for the markupContent.
	 * @return the markupContent
	 */
	public MarkupContent<ComponentGroupConfiguration> getMarkupContent() {
		return markupContent;
	}

	/**
	 * Getter method for the sourceCode.
	 * @return the sourceCode
	 */
	public String getSourceCode() {
		return sourceCode;
	}

}
