/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.java.editor;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.List;

import name.martingeisse.webide.resources.MarkerData;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.operation.FetchSingleResourceOperation;
import name.martingeisse.webide.workbench.IEditor;

import org.apache.wicket.Component;
import org.apache.wicket.model.PropertyModel;

/**
 * {@link IEditor} implementation for the Java source code editor.
 */
public class JavaEditor implements IEditor, Serializable {

	/**
	 * the workspaceResourcePath
	 */
	private ResourcePath workspaceResourcePath;
	
	/**
	 * the contents
	 */
	private String contents;
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.IEditor#initialize(java.lang.String)
	 */
	@Override
	public void initialize(final ResourcePath workspaceResourcePath) {
		FetchSingleResourceOperation operation = new FetchSingleResourceOperation(workspaceResourcePath);
		operation.run();
		if (operation.getResult() == null) {
			throw new IllegalArgumentException("file not found: " + workspaceResourcePath);
		}
		this.workspaceResourcePath = workspaceResourcePath;
		this.contents = new String(operation.getResult().getContents(), Charset.forName("utf-8"));
	}

	/**
	 * Getter method for the contents.
	 * @return the contents
	 */
	public String getContents() {
		return contents;
	}
	
	/**
	 * Setter method for the contents.
	 * @param contents the contents to set
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.IEditor#createComponent(java.lang.String)
	 */
	@Override
	public Component createComponent(final String id) {
		return new JavaEditorPanel(id, new PropertyModel<String>(this, "contents"), workspaceResourcePath);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.IEditor#getWorkspaceResourcePath()
	 */
	@Override
	public ResourcePath getWorkspaceResourcePath() {
		return workspaceResourcePath;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.IEditor#getDocument()
	 */
	@Override
	public Object getDocument() {
		return contents;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.IEditor#updateMarkers(java.util.List)
	 */
	@Override
	public void updateMarkers(final List<MarkerData> markers) {
		// TODO: not yet needed -- the java editor panel adds a future for the compilation result and markers itself
	}

}
