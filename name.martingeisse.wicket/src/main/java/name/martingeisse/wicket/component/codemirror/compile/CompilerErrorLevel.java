/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.codemirror.compile;

/**
 * Used to indicate the severity of an error.
 */
public enum CompilerErrorLevel {

	/**
	 * Indicates an error.
	 */
	ERROR("exclamation-sign"),

	/**
	 * Indicates something that is wrong but not formally an error.
	 */
	WARNING("warning-sign"),
	
	/**
	 * Indicates an informational message that does not indicate an error.
	 */
	INFO("info-sign");

	/**
	 * the glyphicon
	 */
	private final String glyphicon;

	/**
	 * Constructor.
	 * @param glyphicon the glyphicon identifier
	 */
	private CompilerErrorLevel(String glyphicon) {
		this.glyphicon = glyphicon;
	}
	
	/**
	 * Getter method for the glyphicon.
	 * @return the glyphicon
	 */
	public String getGlyphicon() {
		return glyphicon;
	}
	
}
