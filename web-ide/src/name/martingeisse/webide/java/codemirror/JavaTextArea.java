/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.java.codemirror;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * Java editing area via CodeMirror.
 */
public class JavaTextArea extends TextArea<String> {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model for the edited contents
	 */
	public JavaTextArea(String id, IModel<String> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(CssHeaderItem.forReference(new CssResourceReference(JavaTextArea.class, "codemirror.css")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(JavaTextArea.class, "codemirror.js")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(JavaTextArea.class, "clike.js")));
		response.render(OnDomReadyHeaderItem.forScript("codeMirror = CodeMirror.fromTextArea(document.getElementById('" + getMarkupId() +
			"'), {indentWithTabs: true, indentUnit: 4, lineNumbers: true, gutter: true, matchBrackets: true, extraKeys: {'Cmd-S': saveEditor}})"));
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.FormComponent#shouldTrimInput()
	 */
	@Override
	protected boolean shouldTrimInput() {
		return false;
	}
	
}
