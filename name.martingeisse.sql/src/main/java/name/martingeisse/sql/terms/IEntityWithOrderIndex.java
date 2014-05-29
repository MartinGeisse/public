/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.sql.terms;

import java.io.Serializable;

/**
 * Base interface for database entities that have a "orderIndex" field.
 */
public interface IEntityWithOrderIndex extends Serializable {
	
	/**
	 * Getter method for the orderIndex.
	 * @return the orderIndex
	 */
	public Integer getOrderIndex();
	
	/**
	 * Setter method for the orderIndex.
	 * @param orderIndex the orderIndex to set
	 */
	public void setOrderIndex(Integer orderIndex);

	/**
	 * Comparator based on order index.
	 */
	public static class Comparator implements java.util.Comparator<IEntityWithOrderIndex> {

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(IEntityWithOrderIndex o1, IEntityWithOrderIndex o2) {
			return o1.getOrderIndex() - o2.getOrderIndex();
		}
		
	}
	
}
