/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.parser;

import name.martingeisse.reporting.document.Document;

/**
 * This state handles document contents and is mostly equivalent
 * to a {@link SectionState} that handles the root section.
 */
class DocumentState extends SectionState {

	/**
	 * the document
	 */
	private final Document document;
	
	/**
	 * Constructor.
	 * @param document the document to fill with contents
	 */
	public DocumentState(Document document) {
		super(document.getRootSection());
		this.document = document;
	}

	/**
	 * Getter method for the document.
	 * @return the document
	 */
	public Document getDocument() {
		return document;
	}
	
}
