/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.document;

import name.martingeisse.webide.resources.ResourceHandle;

/**
 * Document-type specific implementation that maintains the
 * actual information stored in the document.
 */
public interface IDocumentBody {

	/**
	 * Initializes this object for use in the specified document.
	 * This method is called before the body is placed in the
	 * body.
	 * 
	 * @param document the document that is to contain this body.
	 */
	public void initialize(Document document);
	
	/**
	 * Loads the contents of this document body from the specified resource handle.
	 * @param resourceHandle the resource handle to load from
	 */
	public void load(ResourceHandle resourceHandle);
	
	/**
	 * Saves the contents of this document body to the specified resource handle.
	 * @param resourceHandle the resource handle to save to
	 */
	public void save(ResourceHandle resourceHandle);
	
}
