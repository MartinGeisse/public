/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.codemirror;

import java.io.Serializable;
import name.martingeisse.common.javascript.JavascriptAssemblerUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * A "mode" for CodeMirror.
 * 
 * A CodeMirror mode is basically a set of resource references; to simplify
 * things, these must use the same anchor class and can only differ in their
 * relative path. A mode also has an ID, which is typically the MIME content type
 * of the content being edited -- this is also the ID used by CodeMirror
 * itself. 
 */
public final class CodeMirrorMode implements Serializable {

	/**
	 * the id
	 */
	private final String id;

	/**
	 * the anchor
	 */
	private final Class<?> anchor;

	/**
	 * the paths
	 */
	private final String[] paths;

	/**
	 * Constructor.
	 * @param id the mode id
	 * @param anchor the anchor class (used for resource references to the mode source files)
	 * @param paths the relative paths (used for resource references to the mode source files)
	 */
	public CodeMirrorMode(final String id, final Class<?> anchor, final String... paths) {
		this.id = id;
		this.anchor = anchor;
		this.paths = ArrayUtils.clone(paths);
	}

	/**
	 * Getter method for the id.
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Renders the resource references for this mode to the specified header response.
	 * @param response the header response to render to
	 */
	public void renderResourceReferences(IHeaderResponse response) {
		for (String path : paths) {
			response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(anchor, path)));
		}
	}

	/**
	 * Renders a Javascript fragment to the specified header response that initializes CodeMirror in this mode for the specified text area.
	 * 
	 * @param response the response to render to
	 * @param textArea the text area that shall be using CodeMirror
	 */
	public void renderInitializerForTextArea(IHeaderResponse response, Component textArea) {
		String escapedId = JavascriptAssemblerUtil.escapeStringLiteralSpecialCharacters(id);
		String script =
			"var q = $('#" + textArea.getMarkupId() + "'); \n" +
			"var codeMirror = q.createCodeMirrorForTextArea('" + escapedId + "', {}); \n" +
			"q.data('codeMirrorInstance', codeMirror); ";
		response.render(OnDomReadyHeaderItem.forScript(script));
	}
	
}
