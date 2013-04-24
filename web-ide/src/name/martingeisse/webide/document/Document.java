/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.document;

import name.martingeisse.webide.resources.ResourceHandle;

/**
 * A shared document, backed by a workspace resource.
 */
public final class Document {

	/**
	 * the resourceHandle
	 */
	private final ResourceHandle resourceHandle;
	
	/**
	 * the documentType
	 */
	private final Class<? extends IDocumentBody> documentType;
	
	/**
	 * the body
	 */
	private IDocumentBody body;
	
	/**
	 * Constructor.
	 */
	Document(ResourceHandle resourceHandle, Class<? extends IDocumentBody> documentType) {
		this.resourceHandle = resourceHandle;
		this.documentType = documentType;
		this.body = null;
	}

	/**
	 * Getter method for the resourceHandle.
	 * @return the resourceHandle
	 */
	public ResourceHandle getResourceHandle() {
		return resourceHandle;
	}
	
	/**
	 * Getter method for the documentType.
	 * @return the documentType
	 */
	public Class<? extends IDocumentBody> getDocumentType() {
		return documentType;
	}
	
	/**
	 * Getter method for the body.
	 * @return the body
	 */
	public synchronized IDocumentBody getBody() {
		return body;
	}

	/**
	 * Initializes the document body. This happens only when requested for the first time.
	 */
	synchronized void initializeBody(DocumentHub.Key key) {
		try {
			body = key.getDocumentType().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * (Re-)loads this document from its resource. Note that the document hub loads the
	 * resource initially.
	 */
	public void load() {
		getBody().load(resourceHandle);
	}
	
	/**
	 * Saves this document to its workspace resource.
	 */
	public void save() {
		getBody().save(resourceHandle);
	}

}
