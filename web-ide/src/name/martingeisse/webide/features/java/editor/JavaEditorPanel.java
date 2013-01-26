/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.java.editor;

import name.martingeisse.webide.editor.codemirror.AbstractCodeMirrorEditorPanel;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.wicket.util.WicketHeadUtil;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * The main component for the Java editor.
 */
public class JavaEditorPanel extends AbstractCodeMirrorEditorPanel {
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param contentsModel the model
	 * @param workspaceResourcePath the path of the workspace resource being edited
	 */
	public JavaEditorPanel(String id, IModel<String> contentsModel, ResourcePath workspaceResourcePath) {
		super(id, contentsModel, workspaceResourcePath);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(JavaEditorPanel.class, "clike.js")));
		WicketHeadUtil.includeClassJavascript(response, JavaEditorPanel.class);
		response.render(OnDomReadyHeaderItem.forScript("$('#" + get("form").get("textarea").getMarkupId() + "').createJavaTextArea();"));
	}
	
}
