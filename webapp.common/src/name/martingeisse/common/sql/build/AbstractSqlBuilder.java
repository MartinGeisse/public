/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql.build;

/**
 * Base implementation for {@link ISqlBuilder}.
 */
public abstract class AbstractSqlBuilder implements ISqlBuilder {

	/**
	 * the builder
	 */
	private final StringBuilder builder;
	
	/**
	 * Constructor.
	 */
	public AbstractSqlBuilder() {
		this.builder = new StringBuilder();
	}

	/**
	 * Getter method for the builder.
	 * @return the builder
	 */
	public StringBuilder getBuilder() {
		return builder;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.sql.build.ISqlBuilder#write(java.lang.String)
	 */
	@Override
	public void write(String s) {
		builder.append(s);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return builder.toString();
	}
	
}
