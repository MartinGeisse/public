/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.editor.codemirror;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.webide.editor.IEditorFactory;
import name.martingeisse.webide.editor.IEditorFamily;

/**
 * Editor family implementation for CodeMirror-based editors.
 */
public class CodeMirrorEditorFamily implements IEditorFamily {

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.editor.IEditorFamily#createEditorFactory(name.martingeisse.common.javascript.analyze.JsonAnalyzer)
	 */
	@Override
	public IEditorFactory createEditorFactory(JsonAnalyzer configuration) {
		return null;
	}

}
