/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.java.editor;

import name.martingeisse.webide.resources.BuilderService;
import name.martingeisse.webide.resources.WorkspaceUtil;
import name.martingeisse.webide.workbench.components.IClientFuture;
import name.martingeisse.wicket.util.AjaxRequestUtil;

import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * The main component for the Java editor.
 */
public class JavaEditorPanel extends Panel {

	/**
	 * the workspaceResourcePath
	 */
	private String workspaceResourcePath;
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param contentsModel the model
	 * @param workspaceResourcePath the path of the workspace resource being edited
	 */
	public JavaEditorPanel(String id, IModel<String> contentsModel, String workspaceResourcePath) {
		super(id, contentsModel);
		this.workspaceResourcePath = workspaceResourcePath;
		
		final Form<Void> editorForm = new Form<Void>("form") {
			@Override
			protected void onSubmit() {
				WorkspaceUtil.replaceContents(JavaEditorPanel.this.workspaceResourcePath, (String)JavaEditorPanel.this.getDefaultModelObject());
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
		editorForm.add(new JavaTextArea("textarea", contentsModel));
		editorForm.add(new AjaxButton("submit", editorForm) {});
		add(editorForm);
		
	}
	
}
