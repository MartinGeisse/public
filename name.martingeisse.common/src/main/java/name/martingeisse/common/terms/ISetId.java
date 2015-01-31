/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.common.terms;

/**
 * Generic interface for the {@link #setId(Object)} method. 
 *  
 * @param <T> the ID type
 */
public interface ISetId<T> {

	/**
	 * Setter method for the id
	 * @param id the id to set
	 */
	public void setId(T id);
	
}
