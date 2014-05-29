/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;


/**
 * This class wraps a hash map in which the keys are {@link Class} objects
 * of which the corresponding values are lists of instances. For example,
 * a {@link ClassKeyedListContainer} can use the class object for {@link String}
 * as a key, but the corresponding value list must contain only strings. This
 * can be used as an extensible general-purpose container that contains data
 * objects from various modules of an application and returns them in a type-safe
 * way and without the potential for name collisions.
 * 
 * The rule is that each value must be an instance of the key, not necessarily
 * that the value's getClass() method returns the key. This allows the key
 * to be a superclass or interface of which the value is an instance. This is
 * useful for modules that store data objects in the container whose outside
 * behavior is defined only by an interface.
 * 
 * The intention is that keys are module-specific classes or interfaces from
 * application modules. The above example of using the Class object of class
 * {@link String}, or even {@link Object}, is possible, but does not make very
 * much sense in the way this container is intended to be used.
 * 
 * The types of contained objects can be restricted by base type B.
 * 
 * This class is written in a way that does not define different meanings
 * to keys without value lists vs. keys with empty value lists. This class
 * does distinguish these cases, but solely to avoid creating lots of
 * empty list instances.
 * 
 * @param <B> the base type of all contained objects
 */
public class ClassKeyedListContainer<B> extends KeyedListContainer<Class<? extends B>, B> {

	/**
	 * Constructor.
	 */
	public ClassKeyedListContainer() {
	}
	
}
