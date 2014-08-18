/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.codemirror.modes;

import name.martingeisse.common.javascript.JavascriptAssemblerUtil;
import name.martingeisse.wicket.component.codemirror.CodeMirrorMode;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * A simple mode implementation for CodeMirror. This can be used for modes that
 * are selected only by a mode ID, without any further parameters, or by a fixed
 * parameter object.
 * 
 * An instance of this class consists of a Javascript expression for the mode
 * parameter, and a set of resource references; to simplify things, the latter
 * must all use the same anchor class and can only differ in their relative path.
 */
public final class SimpleCodeMirrorMode implements CodeMirrorMode {

	/**
	 * the parameterExpression
	 */
	private final String parameterExpression;

	/**
	 * the anchor
	 */
	private final Class<?> anchor;

	/**
	 * the paths
	 */
	private final String[] paths;

	/**
	 * Builds a mode instance from the specified modeId and resource references.
	 * 
	 * @param modeId the mode ID
	 * @param anchor the anchor class (used for resource references to the mode source files)
	 * @param paths the relative paths (used for resource references to the mode source files)
	 * @return the mode
	 */
	public static SimpleCodeMirrorMode fromModeId(final String modeId, final Class<?> anchor, final String... paths) {
		return new SimpleCodeMirrorMode(JavascriptAssemblerUtil.formatStringLiteral(modeId), anchor, paths);
	}

	/**
	 * Builds a mode instance from the specified parameter expression and resource references.
	 * 
	 * @param parameterExpression the Javascript expression to use for the 'mode' parameter
	 * @param anchor the anchor class (used for resource references to the mode source files)
	 * @param paths the relative paths (used for resource references to the mode source files)
	 * @return the mode
	 */
	public static SimpleCodeMirrorMode fromModeParameterExpression(final String parameterExpression, final Class<?> anchor, final String... paths) {
		return new SimpleCodeMirrorMode(parameterExpression, anchor, paths);
	}

	/**
	 * Constructor.
	 * @param parameterExpression the Javascript expression to use for the 'mode' parameter
	 * @param anchor the anchor class (used for resource references to the mode source files)
	 * @param paths the relative paths (used for resource references to the mode source files)
	 */
	private SimpleCodeMirrorMode(final String parameterExpression, final Class<?> anchor, final String... paths) {
		this.parameterExpression = parameterExpression;
		this.anchor = anchor;
		this.paths = ArrayUtils.clone(paths);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.codemirror.CodeMirrorMode#renderResourceReferences(org.apache.wicket.markup.head.IHeaderResponse)
	 */
	@Override
	public void renderResourceReferences(IHeaderResponse response) {
		for (String path : paths) {
			response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(anchor, path)));
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.codemirror.CodeMirrorMode#renderModeParameter(java.lang.StringBuilder)
	 */
	@Override
	public void renderModeParameter(StringBuilder builder) {
		builder.append(parameterExpression);
	}

}
