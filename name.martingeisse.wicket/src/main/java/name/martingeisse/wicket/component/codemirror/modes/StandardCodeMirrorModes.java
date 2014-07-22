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
public class StandardCodeMirrorModes {

	/**
	 * Prevent instantiation.
	 */
	private StandardCodeMirrorModes() {
	}
	
	/**
	 * 
	 */
	private static CodeMirrorMode create(String id, String... paths) {
		return new CodeMirrorMode(id, StandardCodeMirrorModes.class, paths);
	}
	
	/**
	 * Javascript mode.
	 */
	public static final CodeMirrorMode JAVASCRIPT = create("text/javascript", "javascript.js");
	
}
