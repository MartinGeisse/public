/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.sql;

import name.martingeisse.common.sql.build.ISqlBuilder;

/**
 * This target is used for COUNT(*) queries.
 */
public class CountAllTarget implements ISelectTarget {

	/* (non-Javadoc)
	 * @see name.martingeisse.common.sql.ISelectTarget#writeTo(name.martingeisse.common.sql.build.ISqlBuilder)
	 */
	@Override
	public void writeTo(ISqlBuilder builder) {
		builder.write(" COUNT(*) ");
	}

}
