/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.type;

/**
 * Base implementation of {@link ISqlTypeInfo}.
 */
public abstract class AbstractSqlTypeInfo extends AbstractTypeInfo implements ISqlTypeInfo {

	/**
	 * Constructor.
	 * @param nullable whether this type is nullable
	 */
	public AbstractSqlTypeInfo(boolean nullable) {
		super(nullable);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.schema.type.ISqlTypeInfo#convertForSave(java.lang.Object)
	 */
	@Override
	public Object convertForSave(Object value) {
		return value;
	}
	
}
