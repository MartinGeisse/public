/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.lowlevel;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Selects a JDBC table by catalog name, schema name, and table name.
 */
public class JdbcTableSelector extends JdbcSchemaSelector {

	/**
	 * the table
	 */
	private final String table;

	/**
	 * Constructor.
	 * @param catalog the catalog name
	 * @param schema the schema name
	 * @param table the table name
	 */
	public JdbcTableSelector(String catalog, String schema, String table) {
		super(catalog, schema);
		this.table = table;
	}

	/**
	 * Getter method for the table.
	 * @return the table
	 */
	public String getTable() {
		return table;
	}
	
	/**
	 * 
	 */
	protected final String tableToString(String suffix) {
		return schemaToString("." + table + suffix);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return tableToString("");
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.lowlevel.JdbcSchemaSelector#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			JdbcTableSelector other = (JdbcTableSelector)obj;
			return ObjectUtils.equals(table, other.table);
		} else {
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.lowlevel.JdbcSchemaSelector#contributeToHashCode(org.apache.commons.lang.builder.HashCodeBuilder)
	 */
	@Override
	protected void contributeToHashCode(HashCodeBuilder builder) {
		super.contributeToHashCode(builder);
		builder.append(table);
	}

	/**
	 * Creates a copy of the table selector in this object, ignoring
	 * any more detailed selector information that might be present.
	 * @return the copied table selector
	 */
	public final JdbcTableSelector copyTableSelector() {
		return new JdbcTableSelector(getCatalog(), getSchema(), table);
	}
	
}
