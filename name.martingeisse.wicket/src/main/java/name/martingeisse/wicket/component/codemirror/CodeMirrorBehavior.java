/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.codemirror;

import name.martingeisse.wicket.util.WicketHeadUtil;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * This behavior turns a text area into a CodeMirror editor.
 */
public class CodeMirrorBehavior extends Behavior {

	/**
	 * the mode
	 */
	private final CodeMirrorMode mode;

	/**
	 * Constructor.
	 * @param mode the CodeMirror mode
	 */
	public CodeMirrorBehavior(final CodeMirrorMode mode) {
		this.mode = mode;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.behavior.Behavior#renderHead(org.apache.wicket.Component, org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(CssHeaderItem.forReference(new CssResourceReference(CodeMirrorBehavior.class, "codemirror.css")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(CodeMirrorBehavior.class, "codemirror.js")));
		WicketHeadUtil.includeClassJavascript(response, CodeMirrorBehavior.class);
		mode.renderResourceReferences(response);
		mode.renderInitializerForTextArea(response, component);

		String script =
			"var q = $('#" + component.getMarkupId() + "'); \n" +
			"q.parents('.collapse').on('shown.bs.collapse', function(e) {q.data('codeMirrorInstance').refresh(); });";
		response.render(OnDomReadyHeaderItem.forScript(script));
	}
	
}
