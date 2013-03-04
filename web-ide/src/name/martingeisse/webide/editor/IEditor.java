/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.editor;

import java.util.List;

import name.martingeisse.webide.resources.FetchMarkerResult;
import name.martingeisse.webide.resources.ResourcePath;

import org.apache.wicket.Component;

/**
 * Represents an editor instance. Only one editor is running for each
 * workbench page.
 */
public interface IEditor {

	/**
	 * This method is invoked after creation to initialize the editor
	 * and load its document. Currently only workspace resources are
	 * supported as document sources.
	 * 
	 * @param workspaceResourcePath the path to the workspace resource to load
	 */
	public void initialize(final ResourcePath workspaceResourcePath);
	
	/**
	 * Creates a user interface component for this editor.
	 * 
	 * @param id the wicket id
	 * @return the component
	 */
	public Component createComponent(String id);
	
	/**
	 * Returns the path to the workspace resource being edited.
	 * 
	 * @return the workspace resource path
	 */
	public ResourcePath getWorkspaceResourcePath();
	
	/**
	 * Returns the document being edited.
	 * 
	 * @return the document
	 */
	public Object getDocument();

	/**
	 * Updates the editor markers. Only markers for the workspace resource being
	 * edited are passed to this method.
	 * 
	 * @param markers the markers for the resource being edited
	 */
	public void updateMarkers(List<FetchMarkerResult> markers);
	
}
