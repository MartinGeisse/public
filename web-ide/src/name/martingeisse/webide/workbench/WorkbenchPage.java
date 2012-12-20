/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench;

import java.nio.charset.Charset;
import java.util.List;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.QFiles;
import name.martingeisse.webide.java.JavaCompilerFacade;
import name.martingeisse.webide.java.codemirror.JavaTextArea;
import name.martingeisse.webide.resources.MarkerData;
import name.martingeisse.webide.resources.MarkerListView;
import name.martingeisse.webide.resources.ResourceIconSelector;
import name.martingeisse.webide.resources.WorkspaceUtil;
import name.martingeisse.webide.workbench.components.IClientFuture;
import name.martingeisse.webide.workbench.components.SelectableElementsBehavior;
import name.martingeisse.webide.workbench.components.contextmenu.ContextMenu;
import name.martingeisse.webide.workbench.components.contextmenu.SimpleContextMenuItem;
import name.martingeisse.webide.workbench.components.contextmenu.SimpleContextMenuItemWithTextInput;
import name.martingeisse.wicket.util.AjaxRequestUtil;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import com.mysema.query.sql.SQLQuery;

/**
 * The main workbench page.
 */
public class WorkbenchPage extends WebPage {

	/**
	 * the editorFilename
	 */
	private String editorFilename;

	/**
	 * the editorContents
	 */
	private String editorContents;

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
				WorkspaceUtil.createFile(filename, "");
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
				WorkspaceUtil.delete(anchor);
				AjaxRequestUtil.markForRender(WorkbenchPage.this);
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

		final Form<Void> editorForm = new Form<Void>("editorForm") {
			@Override
			protected void onSubmit() {
				if (editorFilename != null) {
					WorkspaceUtil.replaceContents(editorFilename, editorContents);
					JavaCompilerFacade.requestCompilation();
					IClientFuture.Behavior.get(WorkbenchPage.this).addFuture(new IClientFuture() {
						@Override
						public boolean check(Behavior behavior) {
							boolean compiled = JavaCompilerFacade.isCompilationFinished();
							if (compiled) {
								AjaxRequestUtil.markForRender(WorkbenchPage.this.get("markersContainer"));
							}
							return compiled;
						}
					});
				}
			}
		};
		editorForm.add(new AjaxButton("submit", editorForm) {});
		editorForm.add(new JavaTextArea("editorArea", new PropertyModel<String>(this, "editorContents")));
		add(editorForm);

		add(new Label("log", new PropertyModel<String>(this, "log")));
	}

	/**
	 * Returns the file names.
	 * @return the file names.
	 */
	public List<String> getFilenames() {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		return query.from(QFiles.files).list(QFiles.files.name);
	}

	/**
	 * 
	 */
	private void loadEditorContents(final String filename) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		final Object resultObject = query.from(QFiles.files).where(QFiles.files.name.eq(filename)).singleResult(QFiles.files.contents);
		final byte[] encodedContents = (byte[])(((Object[])resultObject)[0]);
		editorContents = new String(encodedContents, Charset.forName("utf-8"));
		editorFilename = filename;
		AjaxRequestUtil.markForRender(this);
	}

	/**
	 * Getter method for the editorFilename.
	 * @return the editorFilename
	 */
	public String getEditorFilename() {
		return editorFilename;
	}

	/**
	 * Setter method for the editorFilename.
	 * @param editorFilename the editorFilename to set
	 */
	public void setEditorFilename(final String editorFilename) {
		this.editorFilename = editorFilename;
	}

	/**
	 * Getter method for the editorContents.
	 * @return the editorContents
	 */
	public String getEditorContents() {
		return editorContents;
	}

	/**
	 * Setter method for the editorContents.
	 * @param editorContents the editorContents to set
	 */
	public void setEditorContents(final String editorContents) {
		this.editorContents = editorContents;
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
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(new CssResourceReference(WorkbenchPage.class, "jquery.contextMenu.css")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(WorkbenchPage.class, "common.js")));
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
