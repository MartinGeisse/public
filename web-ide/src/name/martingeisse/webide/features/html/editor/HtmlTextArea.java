/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.html.editor;

import name.martingeisse.wicket.util.WicketHeadUtil;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * HTML editing area via CodeMirror.
 */
public class HtmlTextArea extends TextArea<String> {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model for the edited contents
	 */
	public HtmlTextArea(String id, IModel<String> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(CssHeaderItem.forReference(new CssResourceReference(HtmlTextArea.class, "codemirror.css")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(HtmlTextArea.class, "codemirror.js")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(HtmlTextArea.class, "xml.js")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(HtmlTextArea.class, "javascript.js")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(HtmlTextArea.class, "css.js")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(HtmlTextArea.class, "htmlmixed.js")));
		WicketHeadUtil.includeClassJavascript(response, HtmlTextArea.class);
		response.render(OnDomReadyHeaderItem.forScript("$('#" + getMarkupId() + "').createHtmlTextArea();"));
		
		// TODO
//		response.render(OnDomReadyHeaderItem.forScript("codeMirror = CodeMirror.fromTextArea(document.getElementById('" + getMarkupId() +
//			"'), {indentWithTabs: true, indentUnit: 4, lineNumbers: true, gutter: true, matchBrackets: true, extraKeys: {'Cmd-S': saveEditor}})"));
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.FormComponent#shouldTrimInput()
	 */
	@Override
	protected boolean shouldTrimInput() {
		return false;
	}
	
}
