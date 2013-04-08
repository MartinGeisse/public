/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.verilog.wave;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

import name.martingeisse.webide.editor.AbstractEditor;
import name.martingeisse.webide.editor.IEditor;
import name.martingeisse.webide.resources.FetchMarkerResult;

import org.apache.wicket.Component;

/**
 * {@link IEditor} implementation for the VCD viewer.
 */
public class WaveEditor extends AbstractEditor<ValueChangeDump> {

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.IEditor#createComponent(java.lang.String)
	 */
	@Override
	public Component createComponent(String id) {
		return new WaveEditorPanel(id, createDocumentModel());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.IEditor#updateMarkers(java.util.List)
	 */
	@Override
	public void updateMarkers(List<FetchMarkerResult> markers) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.editor.AbstractEditor#createDocument(byte[])
	 */
	@Override
	protected ValueChangeDump createDocument(byte[] resourceData) {
		try {
			return new ValueChangeDump(new InputStreamReader(new ByteArrayInputStream(resourceData), Charset.forName("utf-8")));
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (SyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
}
