/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.editor.codemirror;

import java.nio.charset.Charset;
import java.util.List;

import name.martingeisse.webide.editor.AbstractEditor;
import name.martingeisse.webide.resources.operation.FetchMarkerResult;
import name.martingeisse.webide.workbench.IEditor;

/**
 * Base implementation of {@link IEditor} for CodeMirror-based editors.
 */
public abstract class AbstractCodeMirrorEditor extends AbstractEditor<String> {

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.editor.AbstractEditor#createDocument(byte[])
	 */
	@Override
	protected String createDocument(byte[] resourceData) {
		return new String(resourceData, Charset.forName("utf-8"));
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.IEditor#updateMarkers(java.util.List)
	 */
	@Override
	public void updateMarkers(final List<FetchMarkerResult> markers) {
		// TODO: not yet needed -- right now the editor panel adds a future for the compilation result and markers itself
	}
	
}
