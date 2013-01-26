/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench.services;

/**
 * This exception type is thrown when trying to open an editor
 * with an unknown editor ID.
 */
public class UnknownEditorIdException extends IllegalArgumentException {

	/**
	 * the editorId
	 */
	private final String editorId;
	
	/**
	 * Constructor.
	 * @param editorId the editor ID
	 */
	public UnknownEditorIdException(String editorId) {
		super("unknown editor ID: " + editorId);
		this.editorId = editorId;
	}
	
	/**
	 * Getter method for the editorId.
	 * @return the editorId
	 */
	public String getEditorId() {
		return editorId;
	}

}
