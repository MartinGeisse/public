/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity;

/**
 * This class represents an entity property, i.e. a field in the
 * entity or in a joined table, or a virtual property derived from
 * other results (e.g. a count query).
 */
public final class EntityProperty {

	/**
	 * the pathText
	 */
	private final String pathText;
	
	/**
	 * Constructor.
	 * @param pathText the textual specification of the property path
	 */
	public EntityProperty(String pathText) {
		this.pathText = pathText;
	}
	
	/**
	 * Getter method for the pathText.
	 * @return the pathText
	 */
	public String getPathText() {
		return pathText;
	}
	
}
