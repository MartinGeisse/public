/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.editor.codemirror;

import name.martingeisse.webide.resources.BuilderService;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.operation.ReplaceFileContentsOperation;
import name.martingeisse.webide.util.NoTrimTextArea;
import name.martingeisse.wicket.util.AjaxRequestUtil;
import name.martingeisse.wicket.util.IClientFuture;
import name.martingeisse.wicket.util.WicketHeadUtil;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * Base class for CodeMirror based editor panels.
 */
public class AbstractCodeMirrorEditorPanel extends Panel {

	/**
	 * the workspaceResourcePath
	 */
	private final ResourcePath workspaceResourcePath;
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param contentsModel the model
	 * @param workspaceResourcePath the path of the workspace resource being edited
	 */
	public AbstractCodeMirrorEditorPanel(String id, IModel<String> contentsModel, ResourcePath workspaceResourcePath) {
		super(id, contentsModel);
		this.workspaceResourcePath = workspaceResourcePath;
		
		final Form<Void> editorForm = new Form<Void>("form") {
			@Override
			protected void onSubmit() {
				
				// save the resource
				final ResourcePath workspaceResourcePath = AbstractCodeMirrorEditorPanel.this.workspaceResourcePath;
				final String newContents = (String)AbstractCodeMirrorEditorPanel.this.getDefaultModelObject();
				ReplaceFileContentsOperation operation = new ReplaceFileContentsOperation(workspaceResourcePath, newContents);
				operation.run();
				
				// request a rebuild and wait for it to finish. TODO: This should be decoupled from editors.
				BuilderService.requestBuild();
				IClientFuture.Behavior.get(getWebPage()).addFuture(new IClientFuture() {
					@Override
					public boolean check(Behavior behavior) {
						boolean compiled = BuilderService.isBuildFinished();
						if (compiled) {
							AjaxRequestUtil.markForRender(getWebPage().get("markersContainer"));
						}
						return compiled;
					}
				});
				
			}
		};
		editorForm.add(new NoTrimTextArea<String>("textarea", contentsModel).setOutputMarkupId(true));
		editorForm.add(new AjaxButton("submit", editorForm) {});
		add(editorForm);
		
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(new CssResourceReference(AbstractCodeMirrorEditorPanel.class, "codemirror.css")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(AbstractCodeMirrorEditorPanel.class, "codemirror.js")));
		WicketHeadUtil.includeClassJavascript(response, AbstractCodeMirrorEditorPanel.class);
	}
	
}
