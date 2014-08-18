/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.codemirror;

import java.io.Serializable;

import org.apache.wicket.markup.head.IHeaderResponse;

/**
 * A "mode" for CodeMirror that defines the formal language being edited.
 */
public interface CodeMirrorMode extends Serializable {

	/**
	 * Renders the resource references for this mode to the specified header response.
	 * 
	 * @param response the header response to render to
	 */
	public void renderResourceReferences(IHeaderResponse response);

	/**
	 * Renders the Javascript value for the CodeMirror 'mode' parameter to
	 * the specified builder.
	 * 
	 * @param builder the builder to render to
	 */
	public void renderModeParameter(StringBuilder builder);
	
}
