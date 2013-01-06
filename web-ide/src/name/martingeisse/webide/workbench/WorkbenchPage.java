/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench;

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
import name.martingeisse.webide.workbench.components.IClientFuture;
import name.martingeisse.webide.workbench.components.SelectableElementsBehavior;
import name.martingeisse.webide.workbench.components.contextmenu.ContextMenu;
import name.martingeisse.webide.workbench.components.contextmenu.ContextMenuSeparator;
import name.martingeisse.webide.workbench.components.contextmenu.DownloadMenuItem;
import name.martingeisse.webide.workbench.components.contextmenu.SimpleContextMenuItem;
import name.martingeisse.webide.workbench.components.contextmenu.SimpleContextMenuItemWithTextInput;
import name.martingeisse.wicket.util.AjaxRequestUtil;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
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
		add(filesContainer);

		final ListView<String> filesList = new ListView<String>("files", new PropertyModel<List<String>>(this, "filenames")) {
			@Override
			protected void populateItem(final ListItem<String> item) {
				final String filename = item.getModelObject();

				final WebMarkupContainer container = new WebMarkupContainer("file");
				container.add(new Image("icon", new AbstractReadOnlyModel<ResourceReference>() {
					@Override
					public ResourceReference getObject() {
						return ResourceIconSelector.FILE_OK.getResourceReference();
					}
				}));
				container.add(new Label("name", filename));
				item.add(container);

			}
		};
		filesContainer.add(filesList);

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
