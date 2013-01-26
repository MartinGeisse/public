/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.editor;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;

/**
 * Implemented by editor families, which group editors very broadly.
 * For example, all CodeMirror based editors use the same editor family.
 */
public interface IEditorFamily {

	/**
	 * Creates an editor factory from the specified configuration.
	 * @param configuration the configuration
	 * @return the editor factory
	 */
	public IEditorFactory createEditorFactory(JsonAnalyzer configuration);
	
}
