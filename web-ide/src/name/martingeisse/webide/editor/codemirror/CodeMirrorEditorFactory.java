/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.editor.codemirror;

import name.martingeisse.webide.editor.IEditorFactory;
import name.martingeisse.webide.workbench.IEditor;

/**
 * Editor factory implementation for CodeMirror-based editors.
 */
public final class CodeMirrorEditorFactory implements IEditorFactory {

	/**
	 * the mode
	 */
	private final CodeMirrorMode mode;

	/**
	 * Constructor.
	 * @param mode the CodeMirror mode
	 */
	public CodeMirrorEditorFactory(final CodeMirrorMode mode) {
		this.mode = mode;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.editor.IEditorFactory#createEditor()
	 */
	@Override
	public IEditor createEditor() {
		return new CodeMirrorEditor(mode);
	}

}
