/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.lowlevel;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Selects a JDBC schema by catalog name and schema name.
 */
public class JdbcSchemaSelector {

	/**
	 * the catalog
	 */
	private final String catalog;
	
	/**
	 * the schema
	 */
	private final String schema;

	/**
	 * Constructor.
	 * @param catalog the catalog name
	 * @param schema the schema name
	 */
	public JdbcSchemaSelector(String catalog, String schema) {
		this.catalog = catalog;
		this.schema = schema;
	}

	/**
	 * Getter method for the catalog.
	 * @return the catalog
	 */
	public String getCatalog() {
		return catalog;
	}
	
	/**
	 * Getter method for the schema.
	 * @return the schema
	 */
	public String getSchema() {
		return schema;
	}
	
	/**
	 * 
	 */
	protected final String schemaToString(String suffix) {
		return ("[DB " + catalog + '.' + schema + suffix + ']');
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return schemaToString("");
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && getClass() == obj.getClass()) {
			JdbcSchemaSelector other = (JdbcSchemaSelector)obj;
			return (ObjectUtils.equals(catalog, other.catalog) && ObjectUtils.equals(schema, other.schema));
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		contributeToHashCode(builder);
		return builder.toHashCode();
		
	}
	
	/**
	 * 
	 */
	protected void contributeToHashCode(HashCodeBuilder builder) {
		builder.append(catalog);
		builder.append(schema);
	}
	
	/**
	 * Creates a copy of the schema selector in this object, ignoring
	 * any more detailed selector information that might be present.
	 * @return the copied schema selector
	 */
	public final JdbcSchemaSelector copySchemaSelector() {
		return new JdbcSchemaSelector(catalog, schema);
	}
	
}
