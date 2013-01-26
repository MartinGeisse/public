/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.java.editor;

import name.martingeisse.webide.editor.codemirror.AbstractCodeMirrorEditor;
import name.martingeisse.webide.editor.codemirror.modes.StandardCodeMirrorModes;
import name.martingeisse.webide.editor.codemirror.panel.CodeMirrorEditorPanel;
import name.martingeisse.webide.workbench.IEditor;

import org.apache.wicket.Component;
import org.apache.wicket.model.PropertyModel;

/**
 * {@link IEditor} implementation for the Java source code editor.
 */
public class JavaEditor extends AbstractCodeMirrorEditor {

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.IEditor#createComponent(java.lang.String)
	 */
	@Override
	public Component createComponent(final String id) {
		return new CodeMirrorEditorPanel(id, new PropertyModel<String>(this, "document"), getWorkspaceResourcePath(), StandardCodeMirrorModes.JAVA);
	}

}
