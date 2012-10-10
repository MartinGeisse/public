/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.datarow;

import java.io.Serializable;


/**
 * Represents a row of data as well as meta-data to which it conforms.
 * 
 * Note that this class does not ensure that either meta-data or data
 * are present, and does not ensure that the data actually conforms
 * to the meta-data. It is up to the caller to ensure that.
 */
public interface IDataRow extends IDataRowMetaHolder, Serializable {

	/**
	 * Getter method for the data. Note that this method does not
	 * specify whether the returned array is backed by this object
	 * or not.
	 * 
	 * @return the data
	 */
	public Object[] getDataRowFields();

	/**
	 * Returns the value of the specified field.
	 * @param fieldName the name of the field whose value to return
	 * @return the field value
	 */
	public Object getDataRowFieldValue(final String fieldName);

	/**
	 * Sets the value of the specified field.
	 * @param fieldName the name of the field whose value to set
	 * @param fieldValue the value to set
	 */
	public void setDataRowFieldValue(final String fieldName, final Object fieldValue);

}
