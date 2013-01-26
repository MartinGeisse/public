/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.html.editor;

import name.martingeisse.webide.editor.codemirror.AbstractCodeMirrorEditor;
import name.martingeisse.webide.workbench.IEditor;

import org.apache.wicket.Component;
import org.apache.wicket.model.PropertyModel;

/**
 * {@link IEditor} implementation for the HTML source code editor.
 */
public class HtmlEditor extends AbstractCodeMirrorEditor {

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.IEditor#createComponent(java.lang.String)
	 */
	@Override
	public Component createComponent(final String id) {
		return new HtmlEditorPanel(id, new PropertyModel<String>(this, "document"), getWorkspaceResourcePath());
	}

}
