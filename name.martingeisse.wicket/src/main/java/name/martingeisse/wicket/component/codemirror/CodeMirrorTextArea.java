/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.codemirror;

import name.martingeisse.wicket.util.WicketHeadUtil;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * Base class for CodeMirror based editor panels. Subclasses must
 * provide Javascript code to actually create the CodeMirror instance
 * for the text area.
 */
public class CodeMirrorTextArea extends TextArea<String> {

	/**
	 * the mode
	 */
	private final CodeMirrorMode mode;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param contentsModel the model
	 * @param mode the CodeMirror mode
	 */
	public CodeMirrorTextArea(String id, IModel<String> contentsModel, final CodeMirrorMode mode) {
		super(id, contentsModel);
		this.mode = mode;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.FormComponent#shouldTrimInput()
	 */
	@Override
	protected boolean shouldTrimInput() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(new CssResourceReference(CodeMirrorTextArea.class, "codemirror.css")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(CodeMirrorTextArea.class, "codemirror.js")));
		WicketHeadUtil.includeClassJavascript(response, CodeMirrorTextArea.class);
		mode.renderResourceReferences(response);
		mode.renderInitializerForTextArea(response, this);
	}
	
}
