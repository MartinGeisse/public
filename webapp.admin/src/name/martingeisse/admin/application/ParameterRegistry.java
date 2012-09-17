/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application;

import java.io.Serializable;
import java.util.HashMap;

import name.martingeisse.common.util.GenericTypeUtil;

/**
 * This class keeps track of the parameter objects set by
 * the application.
 * 
 * Parameter objects are recognized by a {@link ParameterKey} which
 * also defines the type of object that represents the parameter.
 * 
 * The parameter registry can be sealed and will not allow any
 * further modification afterwards.
 * 
 * This class is meant for internal use by the parameter system.
 * Public access is through {@link ParameterKey} only.
 */
class ParameterRegistry implements Serializable {

	/**
	 * the map
	 */
	private final HashMap<ParameterKey<?>, Object> map;
	
	/**
	 * the sealed
	 */
	private boolean sealed;
	
	/**
	 * Constructor.
	 */
	ParameterRegistry() {
		this.map = new HashMap<ParameterKey<?>, Object>();
		this.sealed = false;
	}

	/**
	 * Seals this container.
	 */
	void seal() {
		this.sealed = true;
	}

	/**
	 * Checks whether this container is sealed, and if so, throws an
	 * {@link IllegalStateException}.
	 */
	private void ensureNotSealed() {
		if (sealed) {
			throw new IllegalStateException("container is already sealed");
		}
	}
	
	/**
	 * Sets a parameter object using the specified key.
	 * @param key the key
	 * @param value the value to set
	 */
	<C> void set(ParameterKey<C> key, C value) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}
		if (value == null) {
			throw new IllegalArgumentException("value is null");
		}
		ensureNotSealed();
		map.put(key, value);
	}
	
	/**
	 * Gets a parameter object using the specified key
	 * @param key the key
	 * @return the value
	 */
	<C> C get(ParameterKey<C> key) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}
		return GenericTypeUtil.unsafeCast(map.get(key));
	}
	
}
