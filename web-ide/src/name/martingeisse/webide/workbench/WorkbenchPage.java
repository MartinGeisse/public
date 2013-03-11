/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench;

import name.martingeisse.common.javascript.JavascriptAssemblerUtil;
import name.martingeisse.webide.editor.IEditor;
import name.martingeisse.webide.plugin.InternalPluginUtil;
import name.martingeisse.webide.resources.FetchMarkerResult;
import name.martingeisse.webide.resources.MarkerListView;
import name.martingeisse.webide.resources.ResourceHandle;
import name.martingeisse.webide.resources.ResourceIconSelector;
import name.martingeisse.webide.resources.build.ResourceChangePushMessage;
import name.martingeisse.webide.workbench.services.IWorkbenchEditorService;
import name.martingeisse.webide.workbench.services.IWorkbenchServicesProvider;
import name.martingeisse.wicket.component.tree.JsTree;
import name.martingeisse.wicket.util.AjaxRequestUtil;
import name.martingeisse.wicket.util.IClientFuture;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.atmosphere.Subscribe;
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
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

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
		
		// TODO
		long workspaceId = 1;
		
		this.servicesImplementation = new WorkbenchPageServicesImpl(this);
		setOutputMarkupId(true);
		add(new IClientFuture.Behavior());
		add(new HandleUploadedFileBehavior());

		final WebMarkupContainer filesContainer = new WebMarkupContainer("filesContainer");
		filesContainer.setOutputMarkupId(true);
		add(filesContainer);

		final ITreeProvider<ResourceHandle> resourceTreeProvider = new ResourceTreeProvider(workspaceId, true);
		final JsTree<ResourceHandle> resourceTree = new ResourceTreeComponent("resources", resourceTreeProvider, this) {
			@Override
			protected void populateItem(final Item<ResourceHandle> item) {
				final ResourceHandle resource = item.getModelObject();
				item.add(new Image("icon", new AbstractReadOnlyModel<ResourceReference>() {
					@Override
					public ResourceReference getObject() {
						if (resource.isFile()) {
							return ResourceIconSelector.FILE_OK.getResourceReference();
						} else if (resource.isFolder()) {
							return ResourceIconSelector.FOLDER_OK.getResourceReference();
						} else {
							return ResourceIconSelector.MISSING_ICON.getResourceReference();
						}
					}
				}));
				item.add(new Label("name", resource.getName()));
			}
		};

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
	 * Subscribes to {@link ResourceChangePushMessage}s.
	 * @param target the request handler
	 * @param message the message
	 */
	@Subscribe
	public void receiveMessage(AjaxRequestTarget target, ResourceChangePushMessage message) {
		target.add(WorkbenchPage.this.get("filesContainer"));
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

	/**
	 * Getter method for the uploadErrorMessage.
	 * @return the uploadErrorMessage
	 */
	public String getUploadErrorMessage() {
		return uploadErrorMessage;
	}

	/**
	 * Setter method for the uploadErrorMessage.
	 * @param uploadErrorMessage the uploadErrorMessage to set
	 */
	public void setUploadErrorMessage(final String uploadErrorMessage) {
		this.uploadErrorMessage = uploadErrorMessage;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(final IHeaderResponse response) {
		final CharSequence handleUploadedFile = getBehaviors(HandleUploadedFileBehavior.class).get(0).getCallbackScript();
		response.render(JavaScriptHeaderItem.forScript("window.handleUploadedFile = function() {" + handleUploadedFile + "}", null));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(WorkbenchPage.class, "jquery.jstree.js")));
		super.renderHead(response);
	}

	/**
	 * This method is used by the editor service to replace the current editor
	 * with a new one. This happens when the user opens a file.
	 */
	void replaceEditor(final IEditor editor) {
		final WebMarkupContainer editorContainer = (WebMarkupContainer)get("editorContainer");
		editorContainer.replace(editor.createComponent("editor"));
		AjaxRequestUtil.markForRender(editorContainer);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.services.IWorkbenchServicesProvider#getEditorService()
	 */
	@Override
	public IWorkbenchEditorService getEditorService() {
		return servicesImplementation.getEditorService();
	}
	
	/**
	 * This method allows to obtain the workbench-internal services object which
	 * offers some more services.
	 * @return the internal service object
	 */
	public WorkbenchPageServicesImpl getInternalService() {
		return servicesImplementation;
	}
	
	/**
	 * The only purpose of this behavior is to have a callback URL to re-render the resource tree.
	 */
	class HandleUploadedFileBehavior extends AbstractDefaultAjaxBehavior {

		/* (non-Javadoc)
		 * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#respond(org.apache.wicket.ajax.AjaxRequestTarget)
		 */
		@Override
		protected void respond(final AjaxRequestTarget target) {
			target.add(WorkbenchPage.this.get("filesContainer"));
			if (uploadErrorMessage != null) {
				target.appendJavaScript("alert('" + JavascriptAssemblerUtil.escapeStringLiteralSpecialCharacters(uploadErrorMessage) + "');");
			}
		}

	}

}
