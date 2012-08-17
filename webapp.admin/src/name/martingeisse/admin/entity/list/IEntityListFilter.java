/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.list;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;

/**
 * This filter allows to filter lists of entity instances.
 */
public interface IEntityListFilter {

	/**
	 * The alias used for the entity in the query. Filter predicates should
	 * use this to access fields of the entity being filtered.
	 */
	public static final String ALIAS = "e";

	/**
	 * Creates an {@link Expression} for the SQL expression that does the
	 * filtering on the database side.
	 * @return the filter expression
	 */
	public Predicate getFilterPredicate();

}
