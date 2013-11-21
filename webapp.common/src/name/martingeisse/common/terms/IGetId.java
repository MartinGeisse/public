/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.terms;

/**
 * Generic interface for the {@link #getId()} method. 
 */
public interface IGetId<T> {

	/**
	 * Getter method for the id
	 * @return the id of this object
	 */
	public T getId();
	
}
