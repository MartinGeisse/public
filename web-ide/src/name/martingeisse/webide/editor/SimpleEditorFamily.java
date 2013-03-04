/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.editor;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;

/**
 * This editor family takes a class name in the editor specification.
 * That class must implement {@link IEditor}.
 */
public class SimpleEditorFamily implements IEditorFamily {

	/**
	 * Constructor.
	 */
	public SimpleEditorFamily() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.editor.IEditorFamily#createEditorFactory(name.martingeisse.common.javascript.analyze.JsonAnalyzer)
	 */
	@Override
	public IEditorFactory createEditorFactory(final JsonAnalyzer configuration) {
		final String className = configuration.analyzeMapElement("class").expectString();
		return new IEditorFactory() {
			@Override
			public IEditor createEditor() {
				try {
					// TODO handle classes from plugin bundles
					Class<?> editorClass = getClass().getClassLoader().loadClass(className);
					return (IEditor)(editorClass.newInstance());
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
	}

}
