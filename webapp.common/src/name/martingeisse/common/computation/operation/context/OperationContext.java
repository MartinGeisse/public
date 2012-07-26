/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.computation.operation.context;

/**
 * This class provides a single context value binding.
 * 
 * The value will only be returned if the type matches
 * exactly, not if the key is a subtype of the requested
 * type.
 * 
 * @param <T> the key type
 */
public class OperationContext<T> implements IOperationContext {

	/**
	 * the parent
	 */
	private final IOperationContext parent;

	/**
	 * the key
	 */
	private final Class<T> key;

	/**
	 * the value
	 */
	private final T value;

	/**
	 * Constructor.
	 * @param parent the parent context (or null)
	 * @param key the key for the binding
	 * @param value the value for the binding
	 */
	public OperationContext(final IOperationContext parent, final Class<T> key, final T value) {
		this.parent = parent;
		this.key = key;
		this.value = value;
	}

	/**
	 * Getter method for the parent.
	 * @return the parent
	 */
	public IOperationContext getParent() {
		return parent;
	}

	/**
	 * Getter method for the key.
	 * @return the key
	 */
	public Class<T> getKey() {
		return key;
	}

	/**
	 * Getter method for the value.
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.computation.operation.IOperationContext#getContextValue(java.lang.Class)
	 */
	@Override
	public <X> X getContextValue(Class<X> type) {
		if (type == key) {
			return unsafeGetValue();
		} else if (parent != null) {
			return parent.getContextValue(type);
		} else {
			return null;
		}
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private <X> X unsafeGetValue() {
		return (X)value;
	}
}
