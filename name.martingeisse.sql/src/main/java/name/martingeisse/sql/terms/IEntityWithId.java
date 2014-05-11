/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.sql.terms;

import java.io.Serializable;
import name.martingeisse.common.terms.IGetAndSetId;

/**
 * Base interface for database entities that have an ID.
 * 
 * @param <ID> the ID type
 */
public interface IEntityWithId<ID> extends IGetAndSetId<ID>, Serializable {
}
