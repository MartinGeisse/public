/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.editor;

import name.martingeisse.webide.document.Document;
import name.martingeisse.webide.document.DocumentHub;
import name.martingeisse.webide.document.IDocumentBody;
import name.martingeisse.webide.resources.ResourceHandle;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Base class for {@link IEditor} implementations that use a {@link Document}
 * from the {@link DocumentHub}.
 */
public abstract class AbstractDocumentEditor implements IEditor {

	/**
	 * the documentType
	 */
	private Class<? extends IDocumentBody> documentType;
	
	/**
	 * the resourceHandle
	 */
	private ResourceHandle resourceHandle;
	
	/**
	 * the document
	 */
	private transient Document document;

	/**
	 * Constructor.
	 * @param documentType the document type to request from the {@link DocumentHub}
	 */
	public AbstractDocumentEditor(Class<? extends IDocumentBody> documentType) {
		this.documentType = documentType;
	}
	
	/**
	 * Getter method for the documentType.
	 * @return the documentType
	 */
	public Class<? extends IDocumentBody> getDocumentType() {
		return documentType;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.editor.IEditor#initialize(name.martingeisse.webide.resources.ResourceHandle)
	 */
	@Override
	public void initialize(ResourceHandle resourceHandle) {
		this.resourceHandle = resourceHandle;
		this.document = null;
	}

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
	public final Document getDocument() {
		if (document == null) {
			document = DocumentHub.getDocument(new DocumentHub.Key(resourceHandle, documentType));
		}
		return document;
	}
	
	/**
	 * Creates an IModel for this editor's document.
	 * @return the document model
	 */
	public final IModel<Document> createDocumentModel() {
		return new PropertyModel<Document>(this, "document");
	}
	
}
