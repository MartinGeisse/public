/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.editor;

import java.io.Serializable;

import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.operation.FetchSingleResourceOperation;
import name.martingeisse.webide.resources.operation.WorkspaceResourceNotFoundException;
import name.martingeisse.webide.workbench.IEditor;

/**
 * Base class for {@link IEditor} implementations.
 *
 * @param <D> the document type
 */
public abstract class AbstractEditor<D extends Serializable> implements IEditor, Serializable {

	/**
	 * the workspaceResourcePath
	 */
	private ResourcePath workspaceResourcePath;
	
	/**
	 * the document
	 */
	private D document;

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.IEditor#initialize(name.martingeisse.webide.resources.ResourcePath)
	 */
	@Override
	public final void initialize(ResourcePath workspaceResourcePath) {
		FetchSingleResourceOperation operation = new FetchSingleResourceOperation(workspaceResourcePath);
		operation.run();
		if (operation.getResult() == null) {
			throw new WorkspaceResourceNotFoundException(workspaceResourcePath);
		}
		this.workspaceResourcePath = workspaceResourcePath;
		this.document = createDocument(operation.getResult().getContents());
	}

	/**
	 * Creates the edited document from the specified resource data.
	 * @param resourceData the resource data
	 * @return the document
	 */
	protected abstract D createDocument(byte[] resourceData);

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.IEditor#getWorkspaceResourcePath()
	 */
	@Override
	public final ResourcePath getWorkspaceResourcePath() {
		return workspaceResourcePath;
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
	public void setDocument(D document) {
		this.document = document;
	}
	
}
