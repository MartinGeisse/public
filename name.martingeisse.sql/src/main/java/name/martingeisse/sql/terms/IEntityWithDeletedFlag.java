/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.sql.terms;

import java.io.Serializable;

/**
 * Base interface for database entities that have a "deleted" flag.
 */
public interface IEntityWithDeletedFlag extends Serializable {
	
	/**
	 * Getter method for the deleted.
	 * @return the deleted
	 */
	public Boolean getDeleted();
	
	/**
	 * Setter method for the deleted.
	 * @param deleted the deleted to set
	 */
	public void setDeleted(Boolean deleted);
	
}
