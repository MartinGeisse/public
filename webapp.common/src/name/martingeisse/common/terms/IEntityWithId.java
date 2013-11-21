/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.common.terms;

import java.io.Serializable;

/**
 * Base interface for database entities that have an ID.
 */
public interface IEntityWithId<ID> extends IGetAndSetId<ID>, Serializable {
}
