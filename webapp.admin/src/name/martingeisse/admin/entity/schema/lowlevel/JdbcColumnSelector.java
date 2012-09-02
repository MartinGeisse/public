/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.lowlevel;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Selects a JDBC column by catalog name, schema name, table name, and column name.
 */
public class JdbcColumnSelector extends JdbcTableSelector {

	/**
	 * the column
	 */
	private final String column;

	/**
	 * Constructor.
	 * @param catalog the catalog name
	 * @param schema the schema name
	 * @param table the table name
	 * @param column the column name
	 */
	public JdbcColumnSelector(String catalog, String schema, String table, String column) {
		super(catalog, schema, table);
		this.column = column;
	}

	/**
	 * Getter method for the column.
	 * @return the column
	 */
	public String getColumn() {
		return column;
	}

	/**
	 * 
	 */
	protected final String columnToString(String suffix) {
		return tableToString("." + column + suffix);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return columnToString("");
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.lowlevel.JdbcSchemaSelector#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			JdbcColumnSelector other = (JdbcColumnSelector)obj;
			return ObjectUtils.equals(column, other.column);
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
		builder.append(column);
	}

	/**
	 * Creates a copy of the column selector in this object, ignoring
	 * any more detailed selector information that might be present.
	 * @return the copied column selector
	 */
	public final JdbcColumnSelector copyColumnSelector() {
		return new JdbcColumnSelector(getCatalog(), getSchema(), getTable(), column);
	}
	
}
