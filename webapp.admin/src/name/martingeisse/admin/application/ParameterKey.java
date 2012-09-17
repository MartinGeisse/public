/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application;


/**
 * Identifies a type of parameter that can be set by the
 * application code. For each key, at most one value can
 * be stored.
 * 
 * Parameter keys are compared based on their identity.
 * That is, two parameter keys can only be equal if they
 * are the same object. Therefore, parameter keys are
 * typically created and stored as class constants.
 * 
 * @param <T> the type of parameter object
 */
public final class ParameterKey<T> {

	/**
	 * Constructor.
	 */
	public ParameterKey() {
	}

	/**
	 * Sets the parameter object for this key.
	 * @param value the parameter object to set
	 */
	public void set(T value) {
		ApplicationConfiguration.get().getParameters().set(this, value);
	}

	/**
	 * Obtains the parameter object for this key
	 * @return the parameter object
	 */
	public T get() {
		return ApplicationConfiguration.get().getParameters().get(this);
	}
	
}
