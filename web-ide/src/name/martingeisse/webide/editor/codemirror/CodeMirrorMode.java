/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.editor.codemirror;

import java.io.Serializable;

import name.martingeisse.common.javascript.JavascriptAssemblerUtil;
import name.martingeisse.webide.plugin.PluginBundleHandle;
import name.martingeisse.webide.plugin.PluginBundleWicketResourceReference;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * A "mode" for CodeMirror provided by a plug-in.
 * 
 * TODO: Distinguish from a "CodeMirrorModeReference" that can be
 * resolved to a CodeMirrorMode but does not hold a Class<?> object,
 * only a reference to a (possible not-yet-loaded) plugin.
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
			PluginBundleHandle handle = new PluginBundleHandle(anchor.getClassLoader());
			if (handle.isUsingMainClassLoader()) {
				response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(anchor, path)));
			} else {
				String bundleIdText = Long.toString(handle.getPluginBundleId());
				String localPath = "/" + anchor.getCanonicalName().replace('.', '/');
				localPath = localPath.substring(0, localPath.lastIndexOf('/') + 1) + path;
				response.render(JavaScriptHeaderItem.forReference(new PluginBundleWicketResourceReference(bundleIdText + '/' + localPath)));
			}
		}
	}

	/**
	 * Renders a Javascript fragment to the specified header response that
	 * initializes CodeMirror in this mode for the specified text area.
	 * @param response the response to render to
	 * @param textArea the text area that shall be using CodeMirror
	 * @param otUsername the username to use for registering with the OT server
	 */
	public void renderInitializerForTextArea(IHeaderResponse response, TextArea<?> textArea, String otUsername) {
		String escapedId = JavascriptAssemblerUtil.escapeStringLiteralSpecialCharacters(id);
		String script = "$('#" + textArea.getMarkupId() + "').createCodeMirrorWorkbenchEditor('" + escapedId + "', {}, '" + otUsername + "');";
		response.render(OnDomReadyHeaderItem.forScript(script));
	}
	
}
