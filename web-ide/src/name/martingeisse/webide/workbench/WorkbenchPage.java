/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench;

import java.util.Iterator;
import java.util.List;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QWorkspaceResources;
import name.martingeisse.webide.java.editor.JavaEditor;
import name.martingeisse.webide.plugin.ExtensionQuery;
import name.martingeisse.webide.plugin.ExtensionQuery.Result;
import name.martingeisse.webide.plugin.PluginBundleHandle;
import name.martingeisse.webide.resources.BuilderService;
import name.martingeisse.webide.resources.MarkerData;
import name.martingeisse.webide.resources.MarkerListView;
import name.martingeisse.webide.resources.ResourceIconSelector;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.ResourceType;
import name.martingeisse.webide.resources.WorkspaceWicketResourceReference;
import name.martingeisse.webide.resources.operation.CreateFileOperation;
import name.martingeisse.webide.resources.operation.DeleteResourcesOperation;
import name.martingeisse.webide.resources.operation.FetchResourceResult;
import name.martingeisse.webide.resources.operation.ListResourcesOperation;
import name.martingeisse.webide.workbench.components.IClientFuture;
import name.martingeisse.webide.workbench.components.contextmenu.ContextMenu;
import name.martingeisse.webide.workbench.components.contextmenu.ContextMenuSeparator;
import name.martingeisse.webide.workbench.components.contextmenu.DownloadMenuItem;
import name.martingeisse.webide.workbench.components.contextmenu.SimpleContextMenuItem;
import name.martingeisse.webide.workbench.components.contextmenu.SimpleContextMenuItemWithTextInput;
import name.martingeisse.wicket.component.tree.JsTree;
import name.martingeisse.wicket.util.AjaxRequestUtil;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
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
public class WorkbenchPage extends WebPage {

	/**
	 * the log
	 */
	private String log;

	/**
	 * Constructor.
	 */
	public WorkbenchPage() {
		setOutputMarkupId(true);
		add(new IClientFuture.Behavior());

		final ContextMenu<List<String>> filesContextMenu = new ContextMenu<List<String>>();
		filesContextMenu.getItems().add(new SimpleContextMenuItemWithTextInput<List<String>>("New...", "File name:") {
			@Override
			protected void onSelect(final List<String> anchor, String filename) {
				ResourcePath path = new ResourcePath(true, false, new String[] {filename});
				new CreateFileOperation(path, "", true).run();
				loadEditorContents(filename);
			}
		});
		filesContextMenu.getItems().add(new SimpleContextMenuItem<List<String>>("Open") {
			@Override
			protected void onSelect(final List<String> anchor) {
				if (!anchor.isEmpty()) {
					loadEditorContents(anchor.get(0).toString());
				}
			}
		});
		filesContextMenu.getItems().add(new SimpleContextMenuItem<List<String>>("Rename...") {
			@Override
			protected void onSelect(final List<String> anchor) {
			}
		});
		filesContextMenu.getItems().add(new SimpleContextMenuItem<List<String>>("Delete") {
			@Override
			protected void onSelect(final List<String> anchor) {
				ResourcePath[] paths = new ResourcePath[anchor.size()];
				int i = 0;
				for (String anchorElement : anchor) {
					paths[i] = new ResourcePath(true, false, new String[] {anchorElement});
					i++;
				}
				new DeleteResourcesOperation(paths).run();
				AjaxRequestUtil.markForRender(WorkbenchPage.this);
			}
		});
		filesContextMenu.getItems().add(new DownloadMenuItem<List<String>>("Download") {
			@Override
			protected String determineUrl(final List<String> anchor) {
				if (!anchor.isEmpty()) {
					return urlFor(new WorkspaceWicketResourceReference(anchor.get(0)), null).toString();
				} else {
					return "";
				}
			}
		});
		filesContextMenu.getItems().add(new SimpleContextMenuItem<List<String>>("Run") {
			@Override
			protected void onSelect(final List<String> anchor) {
				if (!anchor.isEmpty()) {
					runApplication(anchor.get(0).toString());
				}
			}
		});
		filesContextMenu.getItems().add(new ContextMenuSeparator<List<String>>());
		for (Result extension : ExtensionQuery.fetch(1, "webide.context_menu.resource")) { // TODO userId
			final String className = extension.getDescriptor().toString();
			final PluginBundleHandle bundleHandle = extension.getBundleHandle();
			filesContextMenu.getItems().add(new SimpleContextMenuItem<List<String>>("Message from " + className) {
				@Override
				protected void onSelect(final List<String> anchor) {
					try {
						final Runnable runnable = bundleHandle.createObject(Runnable.class, className);
						runnable.run();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
		}

		final WebMarkupContainer filesContainer = new WebMarkupContainer("filesContainer");
		/*
		filesContainer.add(new SelectableElementsBehavior<String>(".file", "$('.name', element).text()", filesContextMenu) {

			@Override
			protected String convertSelectedValue(final Object jsonValue) {
				return (String)jsonValue;
			}

			@Override
			protected void onInteraction(final AjaxRequestTarget target, final String interaction, final List<String> selectedValues) {
				if (interaction.equals("dblclick")) {
					if (!selectedValues.isEmpty()) {
						loadEditorContents(selectedValues.get(0).toString());
					}
					target.add(WorkbenchPage.this);
				}
			}

		});
		*/
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
				ResourcePath path = new ResourcePath("/");
				FetchResourceResult fakeResult = new FetchResourceResult(0, path, null, null);
				return getChildren(fakeResult);
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
		final JsTree<FetchResourceResult> resourceTree = new JsTree<FetchResourceResult>("resources", resourceTreeProvider) {

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
			
			@Override
			protected String getNodeType(FetchResourceResult node) {
				return (node.getType() == ResourceType.FILE ? "file" : "folder");
			}

		};
		filesContainer.add(resourceTree);		

		WebMarkupContainer markersContainer = new WebMarkupContainer("markersContainer");
		markersContainer.setOutputMarkupId(true);
		add(markersContainer);
		markersContainer.add(new MarkerListView("markers", null, 30) {
			@Override
			protected void populateItem(final ListItem<MarkerData> item) {
				addMeaningIcon(item, "icon", item.getModel());
				addMeaningLabel(item, "meaning", item.getModel());
				addMessageLabel(item, "message", item.getModel());
			}
		});

		add(new EmptyPanel("editor"));
		add(new Label("log", new PropertyModel<String>(this, "log")));
		add(new WebMarkupContainer("buildingWorkspaceIndicator") {
			@Override
			public boolean isVisible() {
				return !BuilderService.isBuildFinished();
			}
			@Override
			public void onEvent(IEvent<?> event) {
				if (event.getPayload() instanceof AjaxRequestTarget) {
					AjaxRequestTarget ajaxRequestTarget = (AjaxRequestTarget)event.getPayload();
					ajaxRequestTarget.add(this);
				}
			}
		}.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));
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
	 * 
	 */
	private void loadEditorContents(final String filename) {
		ResourcePath path = new ResourcePath(true, false, new String[] {filename});
		IEditor editor = new JavaEditor();
		editor.initialize(path);
		replace(editor.createComponent("editor"));
		AjaxRequestUtil.markForRender(this);
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
	 * @param selectedFilename the filename for the main class (selected by the user)
	 */
	public void runApplication(final String selectedFilename) {

		// this method has vast consequences on the rendered page, so just re-render the whole page
		AjaxRequestUtil.markForRender(this);

		// write the compilation log
		final StringBuilder builder = new StringBuilder();

		// if the build was successful, run the generated application
		final String className = (selectedFilename.endsWith(".java") ? selectedFilename.substring(0, selectedFilename.length() - 5)
			: selectedFilename);
		final String[] commandTokens = new String[] {
			"java", "-cp", "lib/applauncher/code:lib/applauncher/lib/mysql-connector-java-5.1.20-bin.jar",
			"name.martingeisse.webide.tools.AppLauncher", className,
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

}
