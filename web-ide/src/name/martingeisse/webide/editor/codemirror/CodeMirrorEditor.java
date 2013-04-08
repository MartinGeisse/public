/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.editor.codemirror;

import name.martingeisse.webide.editor.codemirror.panel.CodeMirrorEditorPanel;

import org.apache.wicket.Component;

/**
 * TODO: merge with abstract
 */
public class CodeMirrorEditor extends AbstractCodeMirrorEditor {

	/**
	 * the mode
	 */
	private final CodeMirrorMode mode;

	/**
	 * Constructor.
	 * @param mode the CodeMirror mode
	 */
	public CodeMirrorEditor(final CodeMirrorMode mode) {
		this.mode = mode;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.IEditor#createComponent(java.lang.String)
	 */
	@Override
	public Component createComponent(final String id) {
		return new CodeMirrorEditorPanel(id, createDocumentModel(), getWorkspaceResourceHandle(), mode);
	}

}
