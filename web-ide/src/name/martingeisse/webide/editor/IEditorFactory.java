/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.editor;

import name.martingeisse.webide.workbench.IEditor;

/**
 * A factory for workbench editors. The factory is typically
 * created by an editor family from a JSON-based editor
 * description in an editor extension.
 */
public interface IEditorFactory {

	/**
	 * Creates an editor.
	 * @return the editor
	 */
	public IEditor createEditor();
	
}
