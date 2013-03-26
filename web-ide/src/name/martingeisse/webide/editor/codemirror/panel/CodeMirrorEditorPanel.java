/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.editor.codemirror.panel;

import name.martingeisse.webide.editor.codemirror.CodeMirrorMode;
import name.martingeisse.webide.resources.ResourceHandle;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.IModel;

/**
 * Default implementation of {@link AbstractCodeMirrorEditorPanel}.
 */
public final class CodeMirrorEditorPanel extends AbstractCodeMirrorEditorPanel {

	/**
	 * the mode
	 */
	private final CodeMirrorMode mode;

	/**
	 * Constructor.
	 * 
	 * TODO: should not get the resource handle; should save through an interface
	 * 
	 * @param id the wicket id
	 * @param contentsModel the model
	 * @param resourceHandle the handle of the workspace resource being edited
	 * @param mode the CodeMirror mode
	 */
	public CodeMirrorEditorPanel(final String id, final IModel<String> contentsModel, final ResourceHandle resourceHandle, final CodeMirrorMode mode) {
		super(id, contentsModel, resourceHandle);
		this.mode = mode;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.editor.codemirror.AbstractCodeMirrorEditorPanel#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(final IHeaderResponse response) {
		super.renderHead(response);
		mode.renderResourceReferences(response);
		mode.renderInitializerForTextArea(response, getTextArea(), getResourceHandle().getName());
	}

}
