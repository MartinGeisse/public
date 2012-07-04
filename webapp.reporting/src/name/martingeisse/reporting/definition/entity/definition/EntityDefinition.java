/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.entity.definition;

/**
 * This class represents the definition of an entity that can be used
 * in report definitions.
 */
public final class EntityDefinition {

	/**
	 * the name
	 */
	private String name;

	/**
	 * the table
	 */
	private EntityDefinitionTable table;

	/**
	 * Constructor.
	 */
	public EntityDefinition() {
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Setter method for the name.
	 * @param name the name to set
	 */
	public final void setName(final String name) {
		this.name = name;
	}

	/**
	 * Getter method for the table.
	 * @return the table
	 */
	public final EntityDefinitionTable getTable() {
		return table;
	}

	/**
	 * Setter method for the table.
	 * @param table the table to set
	 */
	public final void setTable(final EntityDefinitionTable table) {
		this.table = table;
	}

}
