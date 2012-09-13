/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.type;

/**
 * Base implementation of {@link IEntityIdTypeInfo}.
 */
public abstract class AbstractEntityIdTypeInfo extends AbstractSqlTypeInfo implements IEntityIdTypeInfo {

	/**
	 * Constructor.
	 * @param nullable whether this type is nullable
	 */
	public AbstractEntityIdTypeInfo(boolean nullable) {
		super(nullable);
	}
	
}
