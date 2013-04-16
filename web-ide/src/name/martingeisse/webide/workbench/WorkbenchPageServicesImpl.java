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
import name.martingeisse.webide.plugin.ExtensionQuery;
import name.martingeisse.webide.plugin.PluginBundleHandle;
import name.martingeisse.webide.resources.ResourceHandle;
import name.martingeisse.webide.resources.WorkspaceResourceCollisionException;
import name.martingeisse.webide.workbench.services.IWorkbenchEditorService;
import name.martingeisse.webide.workbench.services.IWorkbenchServicesProvider;
import name.martingeisse.webide.workbench.services.UnknownEditorIdException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
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
	public void openDefaultEditor(final ResourceHandle resourceHandle) {
		openEditor(resourceHandle, getDefaultEditorIdFromFilename(resourceHandle.getName()));
	}

	/**
	 * 
	 */
	private String getDefaultEditorIdFromFilename(final String filename) {

		// TODO
		final long workspaceId = 1;
		final long userId = 1;

		final ExtensionQuery query = new ExtensionQuery(workspaceId, userId, "webide.editor.association");
		for (final ExtensionQuery.Result result : query.fetch()) {
			final JsonAnalyzer editorAssociation = new JsonAnalyzer(result.getDescriptor());
			final String targetType = editorAssociation.analyzeMapElement("target_type").expectString();
			final String targetSpec = editorAssociation.analyzeMapElement("target_spec").expectString();
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
	public void openEditor(final ResourceHandle resourceHandle, final String editorId) {
		try {
			final IEditor editor = ReturnValueUtil.nullNotAllowed(getEditorFactoryForId(editorId).createEditor(), "editorFactory.createEditor");
			editor.initialize(resourceHandle);
			page.replaceEditor(editor);
		} catch (final RuntimeException e) {
			throw e;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 */
	private IEditorFactory getEditorFactoryForId(final String editorId) throws Exception {

		// TODO
		final long workspaceId = 1;
		final long userId = 1;

		final ExtensionQuery query = new ExtensionQuery(workspaceId, userId, "webide.editor");
		for (final ExtensionQuery.Result result : query.fetch()) {
			final JsonAnalyzer editorSpec = new JsonAnalyzer(result.getDescriptor());
			if (editorSpec.analyzeMapElement("id").expectString().equals(editorId)) {
				final String familyId = editorSpec.analyzeMapElement("family").expectString();
				final IEditorFamily family = getEditorFamilyForId(familyId);
				return ReturnValueUtil.nullNotAllowed(family.createEditorFactory(editorSpec), "family.createEditorFactory");
			}
		}
		throw new UnknownEditorIdException(editorId);

	}

	/**
	 * 
	 */
	private IEditorFamily getEditorFamilyForId(final String editorFamilyId) throws Exception {

		// TODO
		final long workspaceId = 1;
		final long userId = 1;

		final ExtensionQuery query = new ExtensionQuery(workspaceId, userId, "webide.editor.family");
		for (final ExtensionQuery.Result result : query.fetch()) {
			final JsonAnalyzer editorAssociation = new JsonAnalyzer(result.getDescriptor());
			final String id = editorAssociation.analyzeMapElement("id").expectString();
			if (id.equals(editorFamilyId)) {
				final String className = editorAssociation.analyzeMapElement("class").expectString();
				final PluginBundleHandle handle = new PluginBundleHandle(result.getPluginBundleId());
				return handle.createObject(IEditorFamily.class, className);
			}

		}
		throw new IllegalArgumentException("unknown editor family: " + editorFamilyId);

	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.services.IWorkbenchEditorService#getEditorPanel()
	 */
	@Override
	public Panel getEditorPanel() {
		return (Panel)((WebMarkupContainer)page.get("editorContainer")).get("editor");
	}

	/**
	 * Creates a workspace resource for an uploaded file. If a file or folder
	 * with the same name already exists, the uploaded file is renamed.
	 * 
	 * @param fileItem the {@link FileItem} that represents the uploaded file
	 * @param destinationFolder the folder into which the uploaded
	 * file should be placed. The name of the file is taken from the upload request.
	 */
	public void storeUploadedFile(final FileItem fileItem, final ResourceHandle destinationFolder) {
		try {
			final byte[] contents = IOUtils.toByteArray(fileItem.getInputStream());
			for (int i = 0; i < 20; i++) {
				final String name = modifyUploadedFileName(fileItem.getName(), i);
				final ResourceHandle file = destinationFolder.getChild(name);
				try {
					file.writeFile(contents, false, false);
					return;
				} catch (final WorkspaceResourceCollisionException e) {
				}
			}
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		page.setUploadErrorMessage("Too many collisions with existing resources -- giving up.");
	}

	/**
	 * Creates an alternative file name in case of collision, or the original
	 * file name if the counter is zero.
	 */
	private static String modifyUploadedFileName(final String name, final int counter) {
		if (counter == 0) {
			return name;
		}
		final int index = name.lastIndexOf('.');
		if (index == -1) {
			return name + '-' + counter;
		}
		final String baseName = name.substring(0, index);
		final String extension = name.substring(index + 1);
		return baseName + '-' + counter + '.' + extension;
	}

}
