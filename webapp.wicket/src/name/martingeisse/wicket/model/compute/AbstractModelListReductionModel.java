/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.model.compute;

import org.apache.wicket.model.IModel;

/**
 * Base class for models that "reduce" the values from a list
 * of other models using a start value and a combination
 * method.
 * @param <T> the type of values to reduce
 */
public abstract class AbstractModelListReductionModel<T> extends AbstractModelListComputationModel<T, T> {

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	@Override
	protected T load() {
		T result = getStartValue();
		for (IModel<T> wrappedModel : getWrappedModels()) {
			result = reduce(result, wrappedModel.getObject());
		}
		return result;
	}

	/**
	 * @return the start value for the reduction
	 */
	protected abstract T getStartValue();
	
	/**
	 * Reduces two values of type T to a single value
	 * @param x the first value
	 * @param y the second value
	 * @return the reduced value
	 */
	protected abstract T reduce(T x, T y);
	
}
