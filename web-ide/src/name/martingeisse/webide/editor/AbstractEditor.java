/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.editor;

import java.io.Serializable;

import name.martingeisse.webide.resources.ResourceHandle;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Base class for {@link IEditor} implementations.
 *
 * @param <D> the document type
 */
public abstract class AbstractEditor<D extends Serializable> implements IEditor {

	/**
	 * the resourceHandle
	 */
	private ResourceHandle resourceHandle;
	
	/**
	 * the document
	 */
	private D document;

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.editor.IEditor#initialize(name.martingeisse.webide.resources.ResourceHandle)
	 */
	@Override
	public void initialize(ResourceHandle resourceHandle) {
		this.resourceHandle = resourceHandle;
		this.document = createDocument(resourceHandle.readBinaryFile(true));
	}

	/**
	 * Creates the edited document from the specified resource data.
	 * @param resourceData the resource data
	 * @return the document
	 */
	protected abstract D createDocument(byte[] resourceData);

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.editor.IEditor#getResourceHandle()
	 */
	@Override
	public ResourceHandle getResourceHandle() {
		return resourceHandle;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.IEditor#getDocument()
	 */
	@Override
	public final D getDocument() {
		return document;
	}
	
	/**
	 * Setter method for the document.
	 * @param document the document to set
	 */
	public final void setDocument(D document) {
		this.document = document;
	}
	
	/**
	 * Creates an IModel for this editor's document.
	 * @return the document model
	 */
	public final IModel<D> createDocumentModel() {
		return new PropertyModel<D>(this, "document");
	}
	
}
