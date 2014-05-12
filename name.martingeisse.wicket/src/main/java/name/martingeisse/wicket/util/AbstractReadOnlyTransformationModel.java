/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;

import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

/**
 * A read-only model that wraps another model and transforms the
 * value returned by reading from that model.
 * @param <T> the model type of this model
 * @param <W> the model type of the wrapped model
 */
public abstract class AbstractReadOnlyTransformationModel<T, W> extends AbstractReadOnlyModel<T> {

	/**
	 * the wrappedModel
	 */
	private final IModel<W> wrappedModel;

	/**
	 * Constructor.
	 * @param wrappedModel the wrapped model
	 */
	public AbstractReadOnlyTransformationModel(IModel<W> wrappedModel) {
		this.wrappedModel = wrappedModel;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.AbstractReadOnlyModel#getObject()
	 */
	@Override
	public T getObject() {
		return transformValue(wrappedModel.getObject());
	}

	/**
	 * Transforms the value returned by the wrapped model.
	 * @param wrappedModelValue the value from the wrapped model
	 * @return the transformed value to be returned by this model
	 */
	protected abstract T transformValue(W wrappedModelValue);
	
}
