/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.list;

import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.common.computation.predicate.IPredicate;

/**
 * This filter allows to filter lists of entity instances.
 * 
 * A filter currently allows to determine for each entity whether it is
 * accepted. TODO: Filters must be able to generate SQL WHERE clauses.
 */
public interface IEntityListFilter extends IPredicate<EntityInstance> {

}
