/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.list;

import java.io.Serializable;

import com.mysema.query.types.Predicate;

/**
 * This class implements a general-purpose entity filter.
 */
public class EntityListFilter implements IEntityListFilter, Serializable {

	/**
	 * the filterPredicate
	 */
	private Predicate filterPredicate;

	/**
	 * Constructor.
	 */
	public EntityListFilter() {
	}

	/**
	 * Constructor.
	 * @param filterPredicate the filter predicate
	 */
	public EntityListFilter(final Predicate filterPredicate) {
		this.filterPredicate = filterPredicate;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.list.IEntityListFilter#getFilterPredicate()
	 */
	@Override
	public Predicate getFilterPredicate() {
		return filterPredicate;
	}

	/**
	 * Setter method for the filterPredicate.
	 * @param filterPredicate the filterPredicate to set
	 */
	public void setFilterPredicate(final Predicate filterPredicate) {
		this.filterPredicate = filterPredicate;
	}

}
