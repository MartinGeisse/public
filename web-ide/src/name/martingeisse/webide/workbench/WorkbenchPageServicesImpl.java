/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench;

import java.io.Serializable;

import name.martingeisse.webide.features.html.editor.HtmlEditor;
import name.martingeisse.webide.features.java.editor.JavaEditor;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.workbench.services.IWorkbenchEditorService;
import name.martingeisse.webide.workbench.services.IWorkbenchServicesProvider;
import name.martingeisse.webide.workbench.services.UnknownEditorIdException;

/**
 * Implementation of {@link IWorkbenchServicesProvider} for a {@link WorkbenchPage}.
 */
public class WorkbenchPageServicesImpl implements IWorkbenchServicesProvider, IWorkbenchEditorService, Serializable {

	/**
	 * the page
	 */
	private final WorkbenchPage page;

	/**
	 * Constructor.
	 * @param page the page
	 */
	public WorkbenchPageServicesImpl(final WorkbenchPage page) {
		this.page = page;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.services.IWorkbenchServicesProvider#getEditorService()
	 */
	@Override
	public IWorkbenchEditorService getEditorService() {
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.services.IWorkbenchEditorService#openDefaultEditor(name.martingeisse.webide.resources.ResourcePath)
	 */
	@Override
	public void openDefaultEditor(final ResourcePath path) {
		openEditor(path, getEditorIdFromExtension(path.getExtension()));
	}
	
	/**
	 * 
	 */
	private String getEditorIdFromExtension(String extension) {
		if (extension == null) {
			return "webide.editors.java";
		} else if (extension.equals("java")) {
			return "webide.editors.java";
		} else if (extension.equals("html")) {
			return "webide.editors.html";
		} else {
			return "webide.editors.java";
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.services.IWorkbenchEditorService#openEditor(name.martingeisse.webide.resources.ResourcePath, java.lang.String)
	 */
	@Override
	public void openEditor(final ResourcePath path, final String editorId) {
		
		// create the editor
		IEditor editor;
		if (editorId.equals("webide.editors.java")) {
			editor = new JavaEditor();
		} else if (editorId.equals("webide.editors.html")) {
			editor = new HtmlEditor();
		} else {
			throw new UnknownEditorIdException(editorId);
		}
		
		// load the resource into the editor
		editor.initialize(path);
		
		// replace the workbench editor
		page.replaceEditor(editor);
		
	}

}
