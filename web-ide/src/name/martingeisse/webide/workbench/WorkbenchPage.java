/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import name.martingeisse.common.javascript.JavascriptAssemblerUtil;
import name.martingeisse.common.terms.CommandVerb;
import name.martingeisse.common.util.GenericTypeUtil;
import name.martingeisse.webide.plugin.InternalPluginUtil;
import name.martingeisse.webide.plugin.PluginBundleHandle;
import name.martingeisse.webide.resources.FetchMarkerResult;
import name.martingeisse.webide.resources.MarkerListView;
import name.martingeisse.webide.resources.ResourceIconSelector;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.Workspace;
import name.martingeisse.webide.resources.WorkspaceResourceCollisionException;
import name.martingeisse.webide.resources.WorkspaceWicketResourceReference;
import name.martingeisse.webide.workbench.services.IWorkbenchEditorService;
import name.martingeisse.webide.workbench.services.IWorkbenchServicesProvider;
import name.martingeisse.wicket.component.contextmenu.ContextMenu;
import name.martingeisse.wicket.component.contextmenu.ContextMenuItem;
import name.martingeisse.wicket.component.contextmenu.ContextMenuSeparator;
import name.martingeisse.wicket.component.contextmenu.DownloadMenuItem;
import name.martingeisse.wicket.component.contextmenu.DynamicContextMenuItems;
import name.martingeisse.wicket.component.contextmenu.FileUploadMenuItem;
import name.martingeisse.wicket.component.contextmenu.IContextMenuDelegate;
import name.martingeisse.wicket.component.contextmenu.SimpleContextMenuItem;
import name.martingeisse.wicket.component.contextmenu.SimpleContextMenuItemWithTextInput;
import name.martingeisse.wicket.component.tree.IJsTreeCommandHandler;
import name.martingeisse.wicket.component.tree.JsTree;
import name.martingeisse.wicket.component.upload.AbstractAjaxFileUploadField;
import name.martingeisse.wicket.javascript.IJavascriptInteractionInterceptor;
import name.martingeisse.wicket.util.AjaxRequestUtil;
import name.martingeisse.wicket.util.IClientFuture;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.servlet.MultipartServletWebRequestImpl;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.upload.FileItem;

/**
 * The main workbench page.
 */
public class WorkbenchPage extends WebPage implements IWorkbenchServicesProvider {

	/**
	 * the servicesImplementation
	 */
	private WorkbenchPageServicesImpl servicesImplementation;

	/**
	 * the log
	 */
	private String log;

	/**
	 * the buildingWorkspaceIndicateWasVisible
	 */
	private Boolean buildingWorkspaceIndicateWasVisible = null;
	
	/**
	 * the uploadErrorMessage
	 */
	private String uploadErrorMessage = null;

	/**
	 * Constructor.
	 */
	public WorkbenchPage() {
		this.servicesImplementation = new WorkbenchPageServicesImpl(this);
		setOutputMarkupId(true);
		add(new IClientFuture.Behavior());
		add(new HandleUploadedFileBehavior());
		
		final ContextMenu<List<ResourcePath>> filesContextMenu = new ContextMenu<List<ResourcePath>>();
		filesContextMenu.add(new SimpleContextMenuItemWithTextInput<List<ResourcePath>>("New File...", "File name:") {
			@Override
			protected void onSelect(final List<ResourcePath> anchor, final String filename) {
				if (!anchor.isEmpty()) {
					final ResourcePath parentFolderPath = Workspace.getFolderPath(anchor.get(0));
					final ResourcePath newFilePath = parentFolderPath.appendSegment(filename, false);
					Workspace.writeFile(newFilePath, "", true, false);
					AjaxRequestUtil.markForRender(WorkbenchPage.this.get("filesContainer"));
					getEditorService().openDefaultEditor(newFilePath);
				}
			}
		});
		filesContextMenu.add(new SimpleContextMenuItemWithTextInput<List<ResourcePath>>("New Folder...", "Folder name:") {
			@Override
			protected void onSelect(final List<ResourcePath> anchor, final String filename) {
				if (!anchor.isEmpty()) {
					final ResourcePath parentFolderPath = Workspace.getFolderPath(anchor.get(0));
					final ResourcePath newFolderPath = parentFolderPath.appendSegment(filename, false);
					Workspace.createFolder(newFolderPath, true);
					AjaxRequestUtil.markForRender(WorkbenchPage.this.get("filesContainer"));
					getEditorService().openDefaultEditor(newFolderPath);
				}
			}
		});
		filesContextMenu.add("Open", CommandVerbs.OPEN);
		filesContextMenu.add(new SimpleContextMenuItem<List<ResourcePath>>("Rename...") {
			@Override
			protected void onSelect(final List<ResourcePath> anchor) {
			}
		});
		filesContextMenu.add("Delete", CommandVerbs.DELETE);
		filesContextMenu.add(new DownloadMenuItem<List<ResourcePath>>("Download") {
			@Override
			protected String determineUrl(final List<ResourcePath> anchor) {
				if (!anchor.isEmpty()) {
					return urlFor(new WorkspaceWicketResourceReference(anchor.get(0)), null).toString();
				} else {
					return "";
				}
			}
		});
		final WebMarkupContainer fileUploadMenuItem = new WebMarkupContainer("fileUploadMenuItem");
		fileUploadMenuItem.setMarkupId("fileUploadMenuItem");
		add(fileUploadMenuItem);
		
		// TODO: There is a but when performing an operation on resources that no longer exist
		// reproduce: rename workspace, create folder -> NPE, should throw an appropriate WSException
		
		// TODO: two upload menu items ???
		
		fileUploadMenuItem.add(new AbstractAjaxFileUploadField("fileUploadField") {
			
			@Override
			protected String renderOptions() {
				String resourceTreeMarkupId = WorkbenchPage.this.get("filesContainer").get("resources").getMarkupId();
				return "{formData: function() {return [{name: 'resources', value: $('#" + resourceTreeMarkupId + "').jstreeAjaxNodeIndexList()}]; }, done: handleUploadedFile}";
			}
			
			/* (non-Javadoc)
			 * @see name.martingeisse.wicket.component.upload.AbstractAjaxFileUploadField#onFileUploaded(org.apache.wicket.protocol.http.servlet.MultipartServletWebRequestImpl, org.apache.wicket.util.upload.FileItem)
			 */
			@Override
			protected void onFileUploaded(MultipartServletWebRequestImpl multipartRequest, FileItem fileItem) {
			}
			
		});
		filesContextMenu.add(new FileUploadMenuItem<List<ResourcePath>>("Upload") {
			
			@Override
			protected String renderOptions() {
				String resourceTreeMarkupId = WorkbenchPage.this.get("filesContainer").get("resources").getMarkupId();
				return "{formData: function() {return [{name: 'resources', value: $('#" + resourceTreeMarkupId + "').jstreeAjaxNodeIndexList()}]; }, done: handleUploadedFile}";
			}
			
			@Override
			protected void onFileUploaded(MultipartServletWebRequestImpl multipartRequest, FileItem fileItem) throws IOException {
				System.out.println("name: " + fileItem.getName());
				
				uploadErrorMessage = null;
				try {
					String resourceListSpecifier = multipartRequest.getPostParameters().getParameterValue("resources").toString();
					if (resourceListSpecifier == null) {
						return;
					}
					@SuppressWarnings("unchecked")
					JsTree<ResourcePath> resourceTree = (JsTree<ResourcePath>)(WorkbenchPage.this.get("filesContainer").get("resources"));
					List<ResourcePath> selectedPaths = resourceTree.lookupSelectedNodes(resourceListSpecifier);
					if (selectedPaths.size() > 0) {
						try {
							final ResourcePath parentFolderPath = Workspace.getFolderPath(selectedPaths.get(0));
							storeUploadedFile(fileItem, parentFolderPath);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				} catch (Exception e) {
					uploadErrorMessage = "An internal error occurred.";
					throw new RuntimeException(e);
				}
			}
			
		});
		filesContextMenu.add(new SimpleContextMenuItem<List<ResourcePath>>("Run") {
			@Override
			protected void onSelect(final List<ResourcePath> anchor) {
				if (!anchor.isEmpty()) {
					runApplication(anchor.get(0));
				}
			}
		});
		filesContextMenu.add(new ContextMenuSeparator<List<ResourcePath>>());
		filesContextMenu.add(new DynamicContextMenuItems<List<ResourcePath>>() {
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
		});

		final WebMarkupContainer filesContainer = new WebMarkupContainer("filesContainer");
		filesContainer.setOutputMarkupId(true);
		add(filesContainer);

		final ITreeProvider<ResourcePath> resourceTreeProvider = new ResourceTreeProvider(true);
		final JsTree<ResourcePath> resourceTree = new JsTree<ResourcePath>("resources", resourceTreeProvider, filesContextMenu) {
			@Override
			protected void populateItem(final Item<ResourcePath> item) {
				final ResourcePath path = item.getModelObject();
				item.add(new Image("icon", new AbstractReadOnlyModel<ResourceReference>() {
					@Override
					public ResourceReference getObject() {
						File file = Workspace.map(path);
						if (file.isFile()) {
							return ResourceIconSelector.FILE_OK.getResourceReference();
						} else if (file.isDirectory()) {
							return ResourceIconSelector.FOLDER_OK.getResourceReference();
						} else {
							return ResourceIconSelector.MISSING_ICON.getResourceReference();
						}
					}
				}));
				item.add(new Label("name", path.getLastSegment()));
			}
		};
		resourceTree.setDoubleClickCommandVerb(CommandVerbs.OPEN);
		resourceTree.bindCommandHandler(CommandVerbs.OPEN, new IJsTreeCommandHandler<ResourcePath>() {
			@Override
			public void handleCommand(CommandVerb commandVerb, List<ResourcePath> selectedNodes) {
				if (!selectedNodes.isEmpty()) {
					getEditorService().openDefaultEditor(selectedNodes.get(0));
				}
			}
		});
		resourceTree.bindHotkey("return", CommandVerbs.OPEN);
		resourceTree.bindCommandHandler(CommandVerbs.DELETE, new IJsTreeCommandHandler<ResourcePath>() {
			@Override
			public void handleCommand(CommandVerb commandVerb, List<ResourcePath> selectedNodes) {
				Workspace.delete(selectedNodes);
				AjaxRequestUtil.markForRender(WorkbenchPage.this.get("filesContainer"));
			}
		}, IJavascriptInteractionInterceptor.CONFIRM);
		resourceTree.bindHotkey("del", CommandVerbs.DELETE);
		resourceTree.setOutputMarkupId(true);
		filesContainer.add(resourceTree);

		final WebMarkupContainer markersContainer = new WebMarkupContainer("markersContainer");
		markersContainer.setOutputMarkupId(true);
		add(markersContainer);
		markersContainer.add(new MarkerListView("markers", null, 30) {
			@Override
			protected void populateItem(final ListItem<FetchMarkerResult> item) {
				final IModel<FetchMarkerResult> model = item.getModel();
				addResourcePathLabel(item, "resource", model);
				addMeaningIcon(item, "icon", model);
				addMeaningLabel(item, "meaning", model);
				addMessageLabel(item, "message", model);
			}
		});

		final WebMarkupContainer editorContainer = new WebMarkupContainer("editorContainer");
		editorContainer.add(new EmptyPanel("editor"));
		editorContainer.setOutputMarkupId(true);
		add(editorContainer);

		add(new Label("log", new PropertyModel<String>(this, "log")));
		add(new WebMarkupContainer("buildingWorkspaceIndicator") {

			@Override
			public boolean isVisible() {
				return false; // TODO !BuilderService.isBuildFinished();
			}

			@Override
			public void onEvent(final IEvent<?> event) {
				if (event.getPayload() instanceof AjaxRequestTarget) {
					if (buildingWorkspaceIndicateWasVisible == null || buildingWorkspaceIndicateWasVisible != isVisible()) {
						final AjaxRequestTarget ajaxRequestTarget = (AjaxRequestTarget)event.getPayload();
						ajaxRequestTarget.add(this);
						buildingWorkspaceIndicateWasVisible = isVisible();
					}
				}
			}

		}.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));
		add(new Link<Void>("refreshPluginsButton") {
			@Override
			public void onClick() {
				InternalPluginUtil.updateUsersPlugins();
				setResponsePage(new WorkbenchPage());
			}
		});
		
	}

	/**
	 * This method is used by the editor service to replace the current editor
	 * with a new one. This happens when the user opens a file.
	 */
	void replaceEditor(final IEditor editor) {
		WebMarkupContainer editorContainer = (WebMarkupContainer)get("editorContainer");
		editorContainer.replace(editor.createComponent("editor"));
		AjaxRequestUtil.markForRender(editorContainer);
	}

	/**
	 * Getter method for the log.
	 * @return the log
	 */
	public String getLog() {
		return log;
	}

	/**
	 * Setter method for the log.
	 * @param log the log to set
	 */
	public void setLog(final String log) {
		this.log = log;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(final IHeaderResponse response) {
		CharSequence handleUploadedFile = getBehaviors(HandleUploadedFileBehavior.class).get(0).getCallbackScript();
		response.render(JavaScriptHeaderItem.forScript("window.handleUploadedFile = function() {" + handleUploadedFile + "}", null));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(WorkbenchPage.class, "jquery.jstree.js")));
		super.renderHead(response);
	}

	/**
	 * Runs the user-written application.
	 * @param selectedPath the file path for the main class (selected by the user)
	 */
	public void runApplication(ResourcePath selectedPath) {

		// this method has vast consequences on the rendered page, so just re-render the whole page
		AjaxRequestUtil.markForRender(this);

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
		log = builder.toString();

	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.services.IWorkbenchServicesProvider#getEditorService()
	 */
	@Override
	public IWorkbenchEditorService getEditorService() {
		return servicesImplementation.getEditorService();
	}

	/**
	 * Stores an uploaded file in the database.
	 * 
	 * @param fileItem the {@link FileItem} from the upload
	 * @param destinationFolderPath the path of the intended parent folder. This must
	 * not be a FILE type resource.
	 */
	private void storeUploadedFile(FileItem fileItem, ResourcePath destinationFolderPath) {
		try {
			byte[] contents = IOUtils.toByteArray(fileItem.getInputStream());
			for (int i = 0; i<20; i++) {
				String name = modifyUploadedFileName(fileItem.getName(), i);
				ResourcePath path = destinationFolderPath.appendSegment(name, false);
				try {
					Workspace.writeFile(path, contents, false, false);
					return;
				} catch (WorkspaceResourceCollisionException e) {
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		uploadErrorMessage = "Too many collisions with existing resources -- giving up.";
	}
	
	/**
	 * Creates an alternative file name in case of collision, or the original
	 * file name if the counter is zero.
	 */
	private String modifyUploadedFileName(String name, int counter) {
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

	/**
	 * The only purpose of this behavior is to have a callback URL to re-render the resource tree.
	 */
	class HandleUploadedFileBehavior extends AbstractDefaultAjaxBehavior {

		/* (non-Javadoc)
		 * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#respond(org.apache.wicket.ajax.AjaxRequestTarget)
		 */
		@Override
		protected void respond(AjaxRequestTarget target) {
			target.add(WorkbenchPage.this.get("filesContainer"));
			if (uploadErrorMessage != null) {
				target.appendJavaScript("alert('" + JavascriptAssemblerUtil.escapeStringLiteralSpecialCharacters(uploadErrorMessage) + "');");
			}
		}
		
	}
	
}
