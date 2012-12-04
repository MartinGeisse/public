/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.serialize;

import java.util.ArrayList;


/**
 * Abstract base implementation of {@link IJavascriptSerializer}.
 * 
 * This method implements all callback methods empty to optimize
 * for the common case that the callbacks aren't needed. Utility
 * methods are provided for other implementations.
 * 
 * @param <T> the type being serialized
 */
public abstract class AbstractJavascriptSerializer<T> implements IJavascriptSerializer<T> {

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.serialize.IJavascriptSerializer#onBeforeSerialize(java.lang.Object)
	 */
	@Override
	public void onBeforeSerialize(T value) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.serialize.IJavascriptSerializer#onBeforeSerializeMultiple(java.lang.Iterable)
	 */
	@Override
	public void onBeforeSerializeMultiple(Iterable<? extends T> values) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.serialize.IJavascriptSerializer#onAfterSerialize(java.lang.Object)
	 */
	@Override
	public void onAfterSerialize(T value) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.javascript.serialize.IJavascriptSerializer#onAfterSerializeMultiple(java.lang.Iterable)
	 */
	@Override
	public void onAfterSerializeMultiple(Iterable<? extends T> values) {
	}

	/**
	 * Invokes {@link #onBeforeSerialize(Object)} in a loop for all the
	 * specified values. Provided for a loop-based implementation of
	 * {@link #onBeforeSerializeMultiple(Iterable)}.
	 * @param values the values
	 */
	protected final void invokeOnBeforeSerializeWithLoop(Iterable<? extends T> values) {
		for (T value : values) {
			onBeforeSerialize(value);
		}
	}
	
	/**
	 * Invokes {@link #onBeforeSerializeMultiple(Iterable)} with a one-element
	 * list containing the specified value. Provided for a list-based
	 * implementation of {@link #onBeforeSerialize(Object)}.
	 * @param value the value
	 */
	protected final void invokeOnBeforeSerializeWithSingularList(T value) {
		ArrayList<T> list = new ArrayList<T>();
		list.add(value);
		onBeforeSerializeMultiple(list);
	}
	
	/**
	 * Invokes {@link #onAfterSerialize(Object)} in a loop for all the
	 * specified values. Provided for a loop-based implementation of
	 * {@link #onAfterSerializeMultiple(Iterable)}.
	 * @param values the values
	 */
	protected final void invokeOnAfterSerializeWithLoop(Iterable<? extends T> values) {
		for (T value : values) {
			onAfterSerialize(value);
		}
	}
	
	/**
	 * Invokes {@link #onAfterSerializeMultiple(Iterable)} with a one-element
	 * list containing the specified value. Provided for a list-based
	 * implementation of {@link #onAfterSerialize(Object)}.
	 * @param value the value
	 */
	protected final void invokeOnAfterSerializeWithSingularList(T value) {
		ArrayList<T> list = new ArrayList<T>();
		list.add(value);
		onAfterSerializeMultiple(list);
	}
	
}
