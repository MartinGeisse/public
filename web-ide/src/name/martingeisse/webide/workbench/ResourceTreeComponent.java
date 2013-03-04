/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import name.martingeisse.common.terms.CommandVerb;
import name.martingeisse.common.util.GenericTypeUtil;
import name.martingeisse.webide.plugin.PluginBundleHandle;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.Workspace;
import name.martingeisse.webide.resources.WorkspaceWicketResourceReference;
import name.martingeisse.wicket.component.contextmenu.ContextMenu;
import name.martingeisse.wicket.component.contextmenu.ContextMenuItem;
import name.martingeisse.wicket.component.contextmenu.ContextMenuSeparator;
import name.martingeisse.wicket.component.contextmenu.DownloadMenuItem;
import name.martingeisse.wicket.component.contextmenu.DynamicContextMenuItems;
import name.martingeisse.wicket.component.contextmenu.FileUploadMenuItem;
import name.martingeisse.wicket.component.contextmenu.IContextMenuDelegate;
import name.martingeisse.wicket.component.contextmenu.SimpleContextMenuItem;
import name.martingeisse.wicket.component.tree.IJsTreeCommandHandler;
import name.martingeisse.wicket.component.tree.JsTree;
import name.martingeisse.wicket.javascript.IJavascriptInteractionInterceptor;
import name.martingeisse.wicket.util.AjaxRequestUtil;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider;
import org.apache.wicket.protocol.http.servlet.MultipartServletWebRequestImpl;
import org.apache.wicket.util.upload.FileItem;

/**
 * The tree component that visualizes the workspace resource tree.
 */
public abstract class ResourceTreeComponent extends JsTree<ResourcePath> {

	/**
	 * the workbenchPage
	 */
	private final WorkbenchPage workbenchPage;
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param treeProvider the tree structure provider
	 * @param workbenchPage the workbench window that contains this component
	 */
	public ResourceTreeComponent(final String id, final ITreeProvider<ResourcePath> treeProvider, final WorkbenchPage workbenchPage) {
		super(id, treeProvider, new ContextMenu<List<ResourcePath>>());
		setOutputMarkupId(true);
		this.workbenchPage = workbenchPage;

		// key bindings
		setDoubleClickCommandVerb(CommandVerbs.OPEN);
		bindHotkey("return", CommandVerbs.OPEN);
		bindHotkey("del", CommandVerbs.DELETE);

		// command handlers
		bindCommandHandler(CommandVerbs.NEW_FILE, new NewFileHandler(), IJavascriptInteractionInterceptor.PROMPT);
		bindCommandHandler(CommandVerbs.NEW_FOLDER, new NewFolderHandler(), IJavascriptInteractionInterceptor.PROMPT);
		bindCommandHandler(CommandVerbs.OPEN, new OpenHandler());
		bindCommandHandler(CommandVerbs.DELETE, new DeleteHandler(), IJavascriptInteractionInterceptor.CONFIRM);
		bindCommandHandler(CommandVerbs.RENAME, new RenameHandler());
		bindCommandHandler(CommandVerbs.RUN, new RunHandler());
		
		// context menu
		// TODO: There is a bug when performing an operation on resources that no longer exist
		// reproduce: rename workspace, create folder -> NPE, should throw an appropriate WSException
		ContextMenu<List<ResourcePath>> contextMenu = getContextMenu();
		contextMenu.add("New File...", CommandVerbs.NEW_FILE);
		contextMenu.add("New Folder...", CommandVerbs.NEW_FOLDER);
		contextMenu.add("Open", CommandVerbs.OPEN);
		contextMenu.add("Rename...", CommandVerbs.RENAME);
		contextMenu.add("Delete", CommandVerbs.DELETE);
		contextMenu.add(new MyDownloadMenuItem(workbenchPage));
		contextMenu.add(new MyUploadMenuItem(workbenchPage));
		contextMenu.add("Run", CommandVerbs.RUN);
		contextMenu.add(new ContextMenuSeparator<List<ResourcePath>>());
		contextMenu.add(new DynamicPluginMenuItems());
		
	}

	/**
	 * This handler opens the first selected resource in the editor.
	 */
	private class OpenHandler implements IJsTreeCommandHandler<ResourcePath, Void> {
		@Override
		public void handleCommand(final CommandVerb commandVerb, final List<ResourcePath> selectedNodes, Void ignored) {
			if (!selectedNodes.isEmpty()) {
				workbenchPage.getEditorService().openDefaultEditor(selectedNodes.get(0));
			}
		}
	}
	
	/**
	 * This handler deletes all selected resources.
	 */
	private class DeleteHandler implements IJsTreeCommandHandler<ResourcePath, Void> {
		@Override
		public void handleCommand(final CommandVerb commandVerb, final List<ResourcePath> selectedNodes, Void ignored) {
			Workspace.delete(selectedNodes);
			AjaxRequestUtil.markForRender(workbenchPage.get("filesContainer"));
		}
	}
	
	/**
	 * This handler renames the first selected resource
	 */
	private class RenameHandler implements IJsTreeCommandHandler<ResourcePath, Void> {
		@Override
		public void handleCommand(final CommandVerb commandVerb, final List<ResourcePath> selectedNodes, Void ignored) {
		}
	}
	
	/**
	 * This handler creates a new file.
	 */
	private class NewFileHandler implements IJsTreeCommandHandler<ResourcePath, String> {
		@Override
		public void handleCommand(final CommandVerb commandVerb, final List<ResourcePath> selectedNodes, String filename) {
			if (!selectedNodes.isEmpty()) {
				final ResourcePath parentFolderPath = Workspace.getFolderPath(selectedNodes.get(0));
				final ResourcePath newFilePath = parentFolderPath.appendSegment(filename, false);
				Workspace.writeFile(newFilePath, "", true, false);
				AjaxRequestUtil.markForRender(workbenchPage.get("filesContainer"));
				workbenchPage.getEditorService().openDefaultEditor(newFilePath);
			}
		}
	}
	
	/**
	 * This handler creates a new folder.
	 */
	private class NewFolderHandler implements IJsTreeCommandHandler<ResourcePath, String> {
		@Override
		public void handleCommand(final CommandVerb commandVerb, final List<ResourcePath> selectedNodes, String filename) {
			if (!selectedNodes.isEmpty()) {
				final ResourcePath parentFolderPath = Workspace.getFolderPath(selectedNodes.get(0));
				final ResourcePath newFolderPath = parentFolderPath.appendSegment(filename, false);
				Workspace.createFolder(newFolderPath, true);
				AjaxRequestUtil.markForRender(workbenchPage.get("filesContainer"));
			}
		}
	}
	
	/**
	 * This handler runs a Java program.
	 */
	private class RunHandler implements IJsTreeCommandHandler<ResourcePath, Void> {
		@Override
		public void handleCommand(final CommandVerb commandVerb, final List<ResourcePath> selectedNodes, Void ignored) {
			if (!selectedNodes.isEmpty()) {
				ResourcePath selectedPath = selectedNodes.get(0);
				
				// this method has vast consequences on the rendered page, so just re-render the whole page
				AjaxRequestUtil.markForRender(workbenchPage);

				// write the compilation log
				final StringBuilder builder = new StringBuilder();

				// if the build was successful, run the generated application
				if ("java".equals(selectedPath.getExtension())) {
					String lastSegment = selectedPath.getLastSegment();
					lastSegment = lastSegment.substring(0, lastSegment.length() - 5);
					selectedPath = selectedPath.replaceLastSegment(lastSegment);
				}
				final String classpath = selectedPath.withLeadingSeparator(false).retainFirstSegments(1, false).appendSegment("bin", false).toString();
				final String className = selectedPath.removeFirstSegments(2, false).toString().replace('/', '.');
				final String[] commandTokens = new String[] {
					"java", "-cp", "lib/applauncher/code:lib/applauncher/lib/mysql-connector-java-5.1.20-bin.jar", "name.martingeisse.webide.tools.AppLauncher", classpath, className,
				};
				try {
					final Process process = Runtime.getRuntime().exec(commandTokens);
					process.getOutputStream().close();
					builder.append(IOUtils.toString(process.getInputStream()));
					builder.append(IOUtils.toString(process.getErrorStream()));
					process.waitFor();
				} catch (final Exception e) {
					builder.append(e.toString());
				}

				// store the log for rendering
				workbenchPage.setLog(builder.toString());				
				
			}
		}
	}
	
	/**
	 * This menu item handles file downloads. Since it is handled 100% client-side,
	 * it cannot be mapped to a command verb "the simple way" (although with appropriate
	 * client-side script generation features, this would be possible).
	 */
	private class MyDownloadMenuItem extends DownloadMenuItem<List<ResourcePath>> {

		/**
		 * the workbenchPage
		 */
		private final WorkbenchPage workbenchPage;
		
		/**
		 * Constructor.
		 */
		MyDownloadMenuItem(final WorkbenchPage workbenchPage) {
			super("Download...");
			this.workbenchPage = workbenchPage;
		}
		
		/* (non-Javadoc)
		 * @see name.martingeisse.wicket.component.contextmenu.DownloadMenuItem#determineUrl(java.lang.Object)
		 */
		@Override
		protected String determineUrl(final List<ResourcePath> anchor) {
			if (!anchor.isEmpty()) {
				return workbenchPage.urlFor(new WorkspaceWicketResourceReference(anchor.get(0)), null).toString();
			} else {
				return "";
			}
		}
		
	}
	
	/**
	 * This menu item handles file uploads. Due to browser restrictions this cannot be
	 * mapped to a command verb.
	 */
	private class MyUploadMenuItem extends FileUploadMenuItem<List<ResourcePath>> {

		/**
		 * the workbenchPage
		 */
		private final WorkbenchPage workbenchPage;
		
		/**
		 * Constructor.
		 */
		MyUploadMenuItem(final WorkbenchPage workbenchPage) {
			super("Upload");
			this.workbenchPage = workbenchPage;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.wicket.component.contextmenu.FileUploadMenuItem#renderOptions()
		 */
		@Override
		protected String renderOptions() {
			final String resourceTreeMarkupId = workbenchPage.get("filesContainer").get("resources").getMarkupId();
			return "{formData: function() {return [{name: 'resources', value: $('#" + resourceTreeMarkupId + "').jstreeAjaxNodeIndexList()}]; }, done: handleUploadedFile}";
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.wicket.component.contextmenu.FileUploadMenuItem#onFileUploaded(org.apache.wicket.protocol.http.servlet.MultipartServletWebRequestImpl, org.apache.wicket.util.upload.FileItem)
		 */
		@Override
		protected void onFileUploaded(final MultipartServletWebRequestImpl multipartRequest, final FileItem fileItem) throws IOException {
			System.out.println("name: " + fileItem.getName());

			workbenchPage.setUploadErrorMessage(null);
			try {
				final String resourceListSpecifier = multipartRequest.getPostParameters().getParameterValue("resources").toString();
				if (resourceListSpecifier == null) {
					return;
				}
				@SuppressWarnings("unchecked")
				final JsTree<ResourcePath> resourceTree = (JsTree<ResourcePath>)(workbenchPage.get("filesContainer").get("resources"));
				final List<ResourcePath> selectedPaths = resourceTree.lookupSelectedNodes(resourceListSpecifier);
				if (selectedPaths.size() > 0) {
					try {
						final ResourcePath parentFolderPath = Workspace.getFolderPath(selectedPaths.get(0));
						workbenchPage.getInternalService().storeUploadedFile(fileItem, parentFolderPath);
					} catch (final Exception e) {
						throw new RuntimeException(e);
					}
				}
			} catch (final Exception e) {
				workbenchPage.setUploadErrorMessage("An internal error occurred.");
				throw new RuntimeException(e);
			}
		}
		
	}

	/**
	 * This item expands to the set of all items contributed by plugins.
	 */
	private class DynamicPluginMenuItems extends DynamicContextMenuItems<List<ResourcePath>> {
		@Override
		protected ContextMenuItem<? super List<ResourcePath>>[] getReplacementItems() {
			final List<ContextMenuItem<? super List<ResourcePath>>> replacementItems = new ArrayList<ContextMenuItem<? super List<ResourcePath>>>();
			for (final ContextMenuStateAccess.Entry entry : ContextMenuStateAccess.get()) {
				final String className = entry.getClassName();
				final long pluginBundleId = entry.getPluginBundleId();
				replacementItems.add(new SimpleContextMenuItem<List<ResourcePath>>(entry.getMenuItemName()) {
					@Override
					protected void onSelect(final List<ResourcePath> anchor) {
						try {
							final PluginBundleHandle bundleHandle = new PluginBundleHandle(pluginBundleId);
							final IContextMenuDelegate<?, ?, ?> runnable = bundleHandle.createObject(IContextMenuDelegate.class, className);
							@SuppressWarnings("unchecked")
							final IContextMenuDelegate<?, List<ResourcePath>, ?> runnable2 = (IContextMenuDelegate<?, List<ResourcePath>, ?>)runnable;
							runnable2.invoke(null, anchor, null);
						} catch (final Exception e) {
							throw new RuntimeException(e);
						}
					}
				});
			}
			final ContextMenuItem<? super List<ResourcePath>>[] array = GenericTypeUtil.unsafeCast(new ContextMenuItem<?>[replacementItems.size()]);
			return replacementItems.toArray(array);
		}		
	}
	
}
