/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.terms;

/**
 * Generic interface for the {@link #setId(Object)} method. 
 */
public interface ISetId<T> {

	/**
	 * Setter method for the id
	 * @param id the id to set
	 */
	public void setId(T id);
	
}
