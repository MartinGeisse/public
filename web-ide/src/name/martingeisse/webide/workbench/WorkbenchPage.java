/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.common.util.GenericTypeUtil;
import name.martingeisse.webide.entity.QWorkspaceResources;
import name.martingeisse.webide.plugin.InternalPluginUtil;
import name.martingeisse.webide.plugin.PluginBundleHandle;
import name.martingeisse.webide.resources.BuilderService;
import name.martingeisse.webide.resources.MarkerListView;
import name.martingeisse.webide.resources.ResourceIconSelector;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.ResourceType;
import name.martingeisse.webide.resources.WorkspaceWicketResourceReference;
import name.martingeisse.webide.resources.operation.CreateFileOperation;
import name.martingeisse.webide.resources.operation.DeleteResourcesOperation;
import name.martingeisse.webide.resources.operation.FetchMarkerResult;
import name.martingeisse.webide.resources.operation.FetchResourceResult;
import name.martingeisse.webide.resources.operation.ListResourcesOperation;
import name.martingeisse.webide.workbench.services.IWorkbenchEditorService;
import name.martingeisse.webide.workbench.services.IWorkbenchServicesProvider;
import name.martingeisse.wicket.component.contextmenu.ContextMenu;
import name.martingeisse.wicket.component.contextmenu.ContextMenuItem;
import name.martingeisse.wicket.component.contextmenu.ContextMenuSeparator;
import name.martingeisse.wicket.component.contextmenu.DownloadMenuItem;
import name.martingeisse.wicket.component.contextmenu.DynamicContextMenuItems;
import name.martingeisse.wicket.component.contextmenu.SimpleContextMenuItem;
import name.martingeisse.wicket.component.contextmenu.SimpleContextMenuItemWithTextInput;
import name.martingeisse.wicket.component.tree.JsTree;
import name.martingeisse.wicket.util.AjaxRequestUtil;
import name.martingeisse.wicket.util.IClientFuture;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import com.mysema.query.sql.SQLQuery;

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
	 * Constructor.
	 */
	public WorkbenchPage() {
		this.servicesImplementation = new WorkbenchPageServicesImpl(this);
		setOutputMarkupId(true);
		add(new IClientFuture.Behavior());

		final ContextMenu<List<FetchResourceResult>> filesContextMenu = new ContextMenu<List<FetchResourceResult>>();
		filesContextMenu.add(new SimpleContextMenuItemWithTextInput<List<FetchResourceResult>>("New...", "File name:") {
			@Override
			protected void onSelect(final List<FetchResourceResult> anchor, String filename) {
				if (!anchor.isEmpty()) {
					FetchResourceResult element = anchor.get(0);
					ResourcePath elementPath = element.getPath();
					ResourcePath parentPath = (element.getType() == ResourceType.FILE ? elementPath.removeLastSegment(false) : elementPath);
					ResourcePath path = parentPath.appendSegment(filename, false);
					new CreateFileOperation(path, "", true).run();
					AjaxRequestUtil.markForRender(WorkbenchPage.this.get("filesContainer"));
					getEditorService().openDefaultEditor(path);
				}
			}
		});
		filesContextMenu.add(new SimpleContextMenuItem<List<FetchResourceResult>>("Open") {
			@Override
			protected void onSelect(final List<FetchResourceResult> anchor) {
				if (!anchor.isEmpty()) {
					getEditorService().openDefaultEditor(anchor.get(0).getPath());
				}
			}
		});
		filesContextMenu.add(new SimpleContextMenuItem<List<FetchResourceResult>>("Rename...") {
			@Override
			protected void onSelect(final List<FetchResourceResult> anchor) {
			}
		});
		filesContextMenu.add(new SimpleContextMenuItem<List<FetchResourceResult>>("Delete") {
			@Override
			protected void onSelect(final List<FetchResourceResult> anchor) {
				ResourcePath[] paths = new ResourcePath[anchor.size()];
				int i = 0;
				for (FetchResourceResult anchorElement : anchor) {
					paths[i] = anchorElement.getPath();
					i++;
				}
				new DeleteResourcesOperation(paths).run();
				AjaxRequestUtil.markForRender(WorkbenchPage.this.get("filesContainer"));
			}
		});
		filesContextMenu.add(new DownloadMenuItem<List<FetchResourceResult>>("Download") {
			@Override
			protected String determineUrl(final List<FetchResourceResult> anchor) {
				if (!anchor.isEmpty()) {
					return urlFor(new WorkspaceWicketResourceReference(anchor.get(0).getPath()), null).toString();
				} else {
					return "";
				}
			}
		});
		filesContextMenu.add(new SimpleContextMenuItem<List<FetchResourceResult>>("Upload") {
			@Override
			protected void onSelect(List<FetchResourceResult> anchor) {
				AjaxRequestUtil.getAjaxRequestTarget().appendJavaScript("TODO");
			}
		});
		filesContextMenu.add(new SimpleContextMenuItem<List<FetchResourceResult>>("Run") {
			@Override
			protected void onSelect(final List<FetchResourceResult> anchor) {
				if (!anchor.isEmpty()) {
					runApplication(anchor.get(0).getPath());
				}
			}
		});
		filesContextMenu.add(new ContextMenuSeparator<List<FetchResourceResult>>());
		filesContextMenu.add(new DynamicContextMenuItems<List<FetchResourceResult>>() {
			@Override
			protected ContextMenuItem<? super List<FetchResourceResult>>[] getReplacementItems() {
				List<ContextMenuItem<? super List<FetchResourceResult>>> replacementItems = new ArrayList<ContextMenuItem<? super List<FetchResourceResult>>>();
				for (ContextMenuStateAccess.Entry entry : ContextMenuStateAccess.get()) {
					final String className = entry.getClassName();
					final long pluginBundleId = entry.getPluginBundleId();
					replacementItems.add(new SimpleContextMenuItem<List<FetchResourceResult>>("Message from " + className) {
						@Override
						protected void onSelect(final List<FetchResourceResult> anchor) {
							try {
								PluginBundleHandle bundleHandle = new PluginBundleHandle(pluginBundleId);
								final Runnable runnable = bundleHandle.createObject(Runnable.class, className);
								runnable.run();
							} catch (Exception e) {
								throw new RuntimeException(e);
							}
						}
					});
				}
				ContextMenuItem<? super List<FetchResourceResult>>[] array = GenericTypeUtil.unsafeCast(new ContextMenuItem<?>[replacementItems.size()]);
				return replacementItems.toArray(array);
			}
		});

		final WebMarkupContainer filesContainer = new WebMarkupContainer("filesContainer");
		filesContainer.setOutputMarkupId(true);
		add(filesContainer);

		final ITreeProvider<FetchResourceResult> resourceTreeProvider = new ITreeProvider<FetchResourceResult>() {
			
			@Override
			public void detach() {
			}
			
			@Override
			public IModel<FetchResourceResult> model(FetchResourceResult object) {
				return Model.of(object);
			}
			
			@Override
			public Iterator<? extends FetchResourceResult> getRoots() {
				return getChildren(FetchResourceResult.createFakeRoot());
			}
			
			@Override
			public boolean hasChildren(FetchResourceResult node) {
				return (node.getType() != ResourceType.FILE);
			}
			
			@Override
			public Iterator<? extends FetchResourceResult> getChildren(FetchResourceResult node) {
				ListResourcesOperation operation = new ListResourcesOperation(node.getPath());
				operation.run();
				return operation.getChildren().iterator();
			}
			
		};
		final JsTree<FetchResourceResult> resourceTree = new JsTree<FetchResourceResult>("resources", resourceTreeProvider, filesContextMenu) {

			@Override
			protected void populateItem(Item<FetchResourceResult> item) {
				final FetchResourceResult fetchResult = item.getModelObject();
				item.add(new Image("icon", new AbstractReadOnlyModel<ResourceReference>() {
					@Override
					public ResourceReference getObject() {
						ResourceIconSelector icon = (fetchResult.getType() == ResourceType.FILE ? ResourceIconSelector.FILE_OK : ResourceIconSelector.FOLDER_OK);
						return icon.getResourceReference();
					}
				}));
				item.add(new Label("name", fetchResult.getPath().getLastSegment()));
			}
			
			/* (non-Javadoc)
			 * @see name.martingeisse.wicket.component.tree.JsTree#onInteraction(org.apache.wicket.ajax.AjaxRequestTarget, java.lang.String, java.util.List)
			 */
			@Override
			protected void onInteraction(AjaxRequestTarget target, String interaction, List<FetchResourceResult> selectedNodes) {
				if ("dblclick".equals(interaction)) {
					if (!selectedNodes.isEmpty()) {
						getEditorService().openDefaultEditor(selectedNodes.get(0).getPath());
					}
				}
			}

		};
		filesContainer.add(resourceTree);
		
		WebMarkupContainer fileUploadForm = new WebMarkupContainer("uploadForm");
		add(fileUploadForm);
		fileUploadForm.add(new FileUploadField("file"));
		// fileUploadForm.add(new AjaxSubmitLink("submit") {
		//});

		WebMarkupContainer markersContainer = new WebMarkupContainer("markersContainer");
		markersContainer.setOutputMarkupId(true);
		add(markersContainer);
		markersContainer.add(new MarkerListView("markers", null, 30) {
			@Override
			protected void populateItem(final ListItem<FetchMarkerResult> item) {
				IModel<FetchMarkerResult> model = item.getModel();
				addResourcePathLabel(item, "resource", model);
				addMeaningIcon(item, "icon", model);
				addMeaningLabel(item, "meaning", model);
				addMessageLabel(item, "message", model);
			}
		});

		WebMarkupContainer editorContainer = new WebMarkupContainer("editorContainer");
		editorContainer.add(new EmptyPanel("editor"));
		editorContainer.setOutputMarkupId(true);
		add(editorContainer);
		
		add(new Label("log", new PropertyModel<String>(this, "log")));
		add(new WebMarkupContainer("buildingWorkspaceIndicator") {
			
			@Override
			public boolean isVisible() {
				return !BuilderService.isBuildFinished();
			}
			
			@Override
			public void onEvent(IEvent<?> event) {
				if (event.getPayload() instanceof AjaxRequestTarget) {
					if (buildingWorkspaceIndicateWasVisible == null || buildingWorkspaceIndicateWasVisible != isVisible()) {
						AjaxRequestTarget ajaxRequestTarget = (AjaxRequestTarget)event.getPayload();
						ajaxRequestTarget.add(this);
						buildingWorkspaceIndicateWasVisible = isVisible();
					}
				}
			}
			
		}.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));
		add(new Link<Void>("refreshPluginsButton") {
			/* (non-Javadoc)
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				InternalPluginUtil.updateUsersPlugins();
				setResponsePage(new WorkbenchPage());
			}
		});
	}
	
	/**
	 * Returns the file names.
	 * @return the file names.
	 */
	public List<String> getFilenames() {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(QWorkspaceResources.workspaceResources);
		query.where(QWorkspaceResources.workspaceResources.type.eq(ResourceType.FILE.name()));
		return query.list(QWorkspaceResources.workspaceResources.name);
	}

	/**
	 * This method is used by the editor service to replace the current editor
	 * with a new one. This happens when the user opens a file.
	 */
	void replaceEditor(IEditor editor) {
		((WebMarkupContainer)get("editorContainer")).replace(editor.createComponent("editor"));
		AjaxRequestUtil.markForRender(WorkbenchPage.this.get("editorContainer"));
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
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(WorkbenchPage.class, "common.js")));
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
			"java", "-cp", "lib/applauncher/code:lib/applauncher/lib/mysql-connector-java-5.1.20-bin.jar",
			"name.martingeisse.webide.tools.AppLauncher", classpath, className,
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
	
}
