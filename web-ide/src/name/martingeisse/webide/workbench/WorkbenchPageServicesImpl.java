/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench;

import java.io.IOException;
import java.io.Serializable;
import java.util.regex.Pattern;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.common.util.ReturnValueUtil;
import name.martingeisse.webide.editor.IEditor;
import name.martingeisse.webide.editor.IEditorFactory;
import name.martingeisse.webide.editor.IEditorFamily;
import name.martingeisse.webide.plugin.UserExtensionQuery;
import name.martingeisse.webide.plugin.PluginBundleHandle;
import name.martingeisse.webide.resources.ResourceHandle;
import name.martingeisse.webide.resources.WorkspaceResourceCollisionException;
import name.martingeisse.webide.workbench.services.IWorkbenchEditorService;
import name.martingeisse.webide.workbench.services.IWorkbenchServicesProvider;
import name.martingeisse.webide.workbench.services.UnknownEditorIdException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.util.upload.FileItem;

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
	 * @see name.martingeisse.webide.workbench.services.IWorkbenchEditorService#openDefaultEditor(name.martingeisse.webide.resources.ResourceHandle)
	 */
	@Override
	public void openDefaultEditor(ResourceHandle resourceHandle) {
		openEditor(resourceHandle, getDefaultEditorIdFromFilename(resourceHandle.getName()));
	}
	
	/**
	 * 
	 */
	private String getDefaultEditorIdFromFilename(String filename) {
		
		// TODO
		long userId = 1;
		
		UserExtensionQuery query = new UserExtensionQuery(userId, "webide.editor.association");
		for (UserExtensionQuery.Result result : query.fetch()) {
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
	 * @see name.martingeisse.webide.workbench.services.IWorkbenchEditorService#openEditor(name.martingeisse.webide.resources.ResourceHandle, java.lang.String)
	 */
	@Override
	public void openEditor(ResourceHandle resourceHandle, String editorId) {
		try {
			IEditor editor = ReturnValueUtil.nullNotAllowed(getEditorFactoryForId(editorId).createEditor(), "editorFactory.createEditor");
			editor.initialize(resourceHandle);
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
		
		UserExtensionQuery query = new UserExtensionQuery(userId, "webide.editor");
		for (UserExtensionQuery.Result result : query.fetch()) {
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
		
		UserExtensionQuery query = new UserExtensionQuery(userId, "webide.editor.family");
		for (UserExtensionQuery.Result result : query.fetch()) {
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

	/**
	 * Creates a workspace resource for an uploaded file. If a file or folder
	 * with the same name already exists, the uploaded file is renamed.
	 * 
	 * @param fileItem the {@link FileItem} that represents the uploaded file
	 * @param destinationFolder the folder into which the uploaded
	 * file should be placed. The name of the file is taken from the upload request.
	 */
	public void storeUploadedFile(FileItem fileItem, ResourceHandle destinationFolder) {
		try {
			byte[] contents = IOUtils.toByteArray(fileItem.getInputStream());
			for (int i = 0; i<20; i++) {
				String name = modifyUploadedFileName(fileItem.getName(), i);
				ResourceHandle file = destinationFolder.getChild(name);
				try {
					file.writeFile(contents, false, false);
					return;
				} catch (WorkspaceResourceCollisionException e) {
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		page.setUploadErrorMessage("Too many collisions with existing resources -- giving up.");
	}
	
	/**
	 * Creates an alternative file name in case of collision, or the original
	 * file name if the counter is zero.
	 */
	private static String modifyUploadedFileName(String name, int counter) {
		if (counter == 0) {
			return name;
		}
		int index = name.lastIndexOf('.');
		if (index == -1) {
			return name + '-' + counter;
		}
		String baseName = name.substring(0, index);
		String extension = name.substring(index + 1);
		return baseName + '-' + counter + '.' + extension;
	}
	
}
