/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.gui;

import org.apache.wicket.behavior.Behavior;

/**
 * This behavior stores the field path for a form field.
 */
public final class FieldPathBehavior extends Behavior {

	/**
	 * the path
	 */
	private final String path;

	/**
	 * Constructor.
	 * @param path the path
	 */
	public FieldPathBehavior(String path) {
		this.path = path;
	}

	/**
	 * Getter method for the path.
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	
}
