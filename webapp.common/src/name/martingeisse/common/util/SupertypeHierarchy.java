/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

import java.util.LinkedList;

/**
 * This class represents a list containing a type and all its supertype, maintaining
 * the subtype-supertype ordering of any two types as the list ordering. That is,
 * assuming a subtype-first ordering, for any two types A and B with A being a
 * subtype of B, A appears earlier in the list than B. If two supertypes are not
 * related, their order in the list is unspecified. Each type appears only once.
 * 
 * The supertype hierarchy is built at construction time. This class behaves as a
 * normal list afterwards.
 * 
 * Clients can specify whether subtypes or supertypes shall appear earlier in the list.
 * 
 * For purposes of this class, {@link Object} is considered a supertype of all
 * interface types.
 */
public class SupertypeHierarchy extends LinkedList<Class<?>> {

	/**
	 * The order in which types appear in the supertype hierarchy.
	 */
	public enum Order {
		
		/**
		 * Specifies that a subtype shall appear earlier than all its supertypes.
		 */
		SUBTYPE_FIRST,
		
		/**
		 * Specifies that a supertype shall appear earlier than all its subtypes.
		 */
		SUPERTYPE_FIRST
		
	}
	
	/**
	 * Creates a supertype hierarchy containing the specified origin class and all
	 * its supertypes.
	 * @param origin the origin class
	 * @param order the order in which types shall appear
	 */
	public SupertypeHierarchy(Class<?> origin, Order order) {
		if (origin == null) {
			throw new IllegalArgumentException("origin is null");
		}
		if (order == null) {
			throw new IllegalArgumentException("order is null");
		}
		if (order == Order.SUPERTYPE_FIRST) {
			addTypes(origin);
		} else {
			SupertypeHierarchy reversed = new SupertypeHierarchy(origin, Order.SUPERTYPE_FIRST);
			for (Class<?> c : reversed) {
				addFirst(c);
			}
		}
	}
	
	/**
	 * Adds the specified type and all its supertypes to this list, in supertype-first order.
	 * @param c the type to start at
	 */
	private void addTypes(Class<?> c) {
	
		/** add the direct superclass, if any **/
		if (c.getSuperclass() != null) {
			addTypes(c.getSuperclass());
		}
		
		/** add superinterfaces, if any **/
		for (Class<?> i : c.getInterfaces()) {
			addTypes(i);
		}
		
		/** for interfaces, add class Object **/
		if (c.isInterface()) {
			addTypes(Object.class);
		}
		
		/** add type c if not yet in the list **/
		if (!contains(c)) {
			add(c);
		}
		
	}
	
}
