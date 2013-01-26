/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.html.editor;

import name.martingeisse.webide.editor.codemirror.AbstractCodeMirrorEditorPanel;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.wicket.util.WicketHeadUtil;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * The main component for the HTML editor.
 */
public class HtmlEditorPanel extends AbstractCodeMirrorEditorPanel {
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param contentsModel the model
	 * @param workspaceResourcePath the path of the workspace resource being edited
	 */
	public HtmlEditorPanel(String id, IModel<String> contentsModel, ResourcePath workspaceResourcePath) {
		super(id, contentsModel, workspaceResourcePath);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(HtmlEditorPanel.class, "xml.js")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(HtmlEditorPanel.class, "javascript.js")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(HtmlEditorPanel.class, "css.js")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(HtmlEditorPanel.class, "htmlmixed.js")));
		WicketHeadUtil.includeClassJavascript(response, HtmlEditorPanel.class);
		response.render(OnDomReadyHeaderItem.forScript("$('#" + get("form").get("textarea").getMarkupId() + "').createHtmlTextArea();"));
	}
	
}
