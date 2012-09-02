/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.lowlevel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Stores information about a JDBC foreign key.
 */
public final class JdbcForeignKey {

	/**
	 * the foreignKeyName
	 */
	private final String foreignKeyName;
	
	/**
	 * the elements
	 */
	private final List<JdbcForeignKeyElement> elements;
	
	/**
	 * the parentTable
	 */
	private JdbcTableSelector parentTable;
	
	/**
	 * the foreignTable
	 */
	private JdbcTableSelector foreignTable;
	
	/**
	 * Constructor.
	 * @param foreignKeyName the foreign key name
	 */
	public JdbcForeignKey(String foreignKeyName) {
		this.foreignKeyName = foreignKeyName;
		this.elements = new ArrayList<JdbcForeignKeyElement>();
		this.parentTable = null;
		this.foreignTable = null;
	}

	/**
	 * Getter method for the foreignKeyName.
	 * @return the foreignKeyName
	 */
	public String getForeignKeyName() {
		return foreignKeyName;
	}
	
	/**
	 * Getter method for the elements.
	 * @return the elements
	 */
	public List<JdbcForeignKeyElement> getElements() {
		return elements;
	}
	
	/**
	 * Getter method for the parentTable.
	 * @return the parentTable
	 */
	public JdbcTableSelector getParentTable() {
		return parentTable;
	}
	
	/**
	 * Getter method for the foreignTable.
	 * @return the foreignTable
	 */
	public JdbcTableSelector getForeignTable() {
		return foreignTable;
	}
	
	/**
	 * Checks that all elements use the same parent table selector
	 * and the same foreign table selector; extracts those selectors,
	 * and sorts the elements of this foreign key by sequence number.
	 */
	public void prepare() {
		
		// make sure we have any elements
		if (elements.isEmpty()) {
			throw new IllegalStateException("foreign key without any elements");
		}
		
		// extract the table selectors
		this.parentTable = elements.get(0).getParentColumn().copyTableSelector();
		this.foreignTable = elements.get(0).getForeignColumn().copyTableSelector();
		
		// ensure that for both parent and foreign side, the table selectors are all equal
		for (JdbcForeignKeyElement element : elements) {
			{
				JdbcTableSelector elementParentTable = element.getParentColumn().copyTableSelector();
				if (!elementParentTable.equals(parentTable)) {
					throw new IllegalStateException("database contains a foreign key with multiple parent tables " + elementParentTable + " and " + parentTable);
				}
			}
			{
				JdbcTableSelector elementForeignTable = element.getForeignColumn().copyTableSelector();
				if (!elementForeignTable.equals(foreignTable)) {
					throw new IllegalStateException("database contains a foreign key with multiple foreign tables " + elementForeignTable + " and " + foreignTable);
				}
			}
		}
		
		// sort elements
		Collections.sort(elements, new Comparator<JdbcForeignKeyElement>() {
			@Override
			public int compare(JdbcForeignKeyElement x, JdbcForeignKeyElement y) {
				return (x.getElementSequenceNumber() - y.getElementSequenceNumber());
			}
		});
		
	}
	
}
