/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.list;

import com.mysema.query.types.Predicate;

/**
 * This interface is implemented by objects that are able
 * to make use of an entity filtering predicate.
 */
public interface IEntityPredicateAcceptor {

	/**
	 * Sets the entity predicate for this acceptor.
	 * @param predicate the entity predicate
	 */
	public void acceptEntityListFilter(Predicate predicate);

}
