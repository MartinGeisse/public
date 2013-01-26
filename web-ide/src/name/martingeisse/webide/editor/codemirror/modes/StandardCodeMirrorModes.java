/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.editor.codemirror.modes;

import name.martingeisse.webide.editor.codemirror.CodeMirrorMode;

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
	 * Mixed HTML mode.
	 */
	public static final CodeMirrorMode HTML = create("text/html", "xml.js", "javascript.js", "css.js", "htmlmixed.js");
	
	/**
	 * Java mode.
	 */
	public static final CodeMirrorMode CLIKE = create("text/x-csrc", "clike.js");
	
}
