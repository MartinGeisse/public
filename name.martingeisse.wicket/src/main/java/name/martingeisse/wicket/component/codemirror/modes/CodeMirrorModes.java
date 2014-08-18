/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.codemirror.modes;

import name.martingeisse.wicket.component.codemirror.CodeMirrorMode;


/**
 * Standard modes are available as static fields of this class.
 */
public class CodeMirrorModes {

	/**
	 * Prevent instantiation.
	 */
	private CodeMirrorModes() {
	}
	
	/**
	 * 
	 */
	private static CodeMirrorMode fromModeId(String modeId, String... paths) {
		return SimpleCodeMirrorMode.fromModeId(modeId, CodeMirrorModes.class, paths);
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static CodeMirrorMode fromModeParameterExpression(String parameterExpression, String... paths) {
		return SimpleCodeMirrorMode.fromModeParameterExpression(parameterExpression, CodeMirrorModes.class, paths);
	}
	
	/**
	 * Javascript mode.
	 */
	public static final CodeMirrorMode JAVASCRIPT = fromModeId("text/javascript", "javascript.js");
	
	/**
	 * JSON mode.
	 */
	public static final CodeMirrorMode JSON = fromModeId("application/json", "javascript.js");
	
	
}
