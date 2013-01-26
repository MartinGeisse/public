/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench;

import java.io.Serializable;
import java.util.regex.Pattern;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.common.util.ReturnValueUtil;
import name.martingeisse.webide.editor.IEditorFactory;
import name.martingeisse.webide.editor.IEditorFamily;
import name.martingeisse.webide.plugin.ExtensionQuery;
import name.martingeisse.webide.plugin.PluginBundleHandle;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.workbench.services.IWorkbenchEditorService;
import name.martingeisse.webide.workbench.services.IWorkbenchServicesProvider;
import name.martingeisse.webide.workbench.services.UnknownEditorIdException;

import org.apache.log4j.Logger;

/**
 * Implementation of {@link IWorkbenchServicesProvider} for a {@link WorkbenchPage}.
 */
public class WorkbenchPageServicesImpl implements IWorkbenchServicesProvider, IWorkbenchEditorService, Serializable {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(WorkbenchPageServicesImpl.class);
	
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
		openEditor(path, getDefaultEditorIdFromFilename(path.getLastSegment()));
	}
	
	/**
	 * 
	 */
	private String getDefaultEditorIdFromFilename(String filename) {
		
		// TODO
		long userId = 1;
		
		ExtensionQuery query = new ExtensionQuery(userId, "webide.editor.association");
		for (ExtensionQuery.Result result : query.fetch()) {
			JsonAnalyzer editorAssociation = new JsonAnalyzer(result.getDescriptor());
			String targetType = editorAssociation.analyzeMapElement("target_type").expectString();
			String targetSpec = editorAssociation.analyzeMapElement("target_spec").expectString();
			if (targetType.equals("filename_pattern")) {
				if (Pattern.matches(targetSpec, filename)) {
					return editorAssociation.analyzeMapElement("editor").expectString();
				}
			} else {
				logger.error("unknown editor association target type: " + targetType);
			}
		}
		
		// TODO user a better default
		return "webide.editors.java";
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.services.IWorkbenchEditorService#openEditor(name.martingeisse.webide.resources.ResourcePath, java.lang.String)
	 */
	@Override
	public void openEditor(final ResourcePath path, final String editorId) {
		try {
			IEditor editor = ReturnValueUtil.nullNotAllowed(getEditorFactoryForId(editorId).createEditor(), "editorFactory.createEditor");
			editor.initialize(path);
			page.replaceEditor(editor);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 */
	private IEditorFactory getEditorFactoryForId(String editorId) throws Exception {
		
		// TODO
		long userId = 1;
		
		ExtensionQuery query = new ExtensionQuery(userId, "webide.editor");
		for (ExtensionQuery.Result result : query.fetch()) {
			JsonAnalyzer editorSpec = new JsonAnalyzer(result.getDescriptor());
			if (editorSpec.analyzeMapElement("id").expectString().equals(editorId)) {
				String familyId = editorSpec.analyzeMapElement("family").expectString();
				IEditorFamily family = getEditorFamilyForId(familyId);
				return ReturnValueUtil.nullNotAllowed(family.createEditorFactory(editorSpec), "family.createEditorFactory");
			}
		}
		throw new UnknownEditorIdException(editorId);
		
	}
	
	/**
	 * 
	 */
	private IEditorFamily getEditorFamilyForId(String editorFamilyId) throws Exception {
		
		// TODO
		long userId = 1;
		
		ExtensionQuery query = new ExtensionQuery(userId, "webide.editor.family");
		for (ExtensionQuery.Result result : query.fetch()) {
			JsonAnalyzer editorAssociation = new JsonAnalyzer(result.getDescriptor());
			String id = editorAssociation.analyzeMapElement("id").expectString();
			if (id.equals(editorFamilyId)) {
				String className = editorAssociation.analyzeMapElement("class").expectString();
				PluginBundleHandle handle = new PluginBundleHandle(result.getPluginBundleId());
				return handle.createObject(IEditorFamily.class, className);
			}
			
		}
		throw new IllegalArgumentException("unknown editor family: " + editorFamilyId);
		
	}

}
