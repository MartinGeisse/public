/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.sql.terms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import name.martingeisse.common.terms.IGetAndSetId;

/**
 * Base interface for database entities that have an ID.
 * 
 * @param <ID> the ID type
 */
public interface IEntityWithId<ID> extends IGetAndSetId<ID>, Serializable {

	/**
	 * Contains utility methods related to {@link IEntityWithId}.
	 */
	public static class Util {

		/**
		 * Prevent instantiation.
		 */
		private Util() {
		}
		
		/**
		 * Extracts the IDs of a collection of entities. The IDs are returned
		 * in iteration order of the collection.
		 * 
		 * @param entities the entities
		 * @return the IDs
		 */
		public static <ID> List<ID> extractIds(Collection<? extends IEntityWithId<ID>> entities) {
			List<ID> result = new ArrayList<ID>();
			for (IEntityWithId<ID> entity : entities) {
				result.add(entity.getId());
			}
			return result;
		}

	}

}
