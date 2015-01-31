/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.common.terms;

/**
 * Generic interface for the {@link #getId()} method.
 *  
 * @param <T> the ID type
 */
public interface IGetId<T> {

	/**
	 * Getter method for the id
	 * @return the id of this object
	 */
	public T getId();
	
}
