/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.model.compute;

import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Base class for all models that compute their value from the
 * values obtained from a list of other models.
 * @param <S> the type of the wrapped models
 * @param <T> the type of this model
 */
public abstract class AbstractModelListComputationModel<S, T> extends LoadableDetachableModel<T> {

	/**
	 * the wrappedModels
	 */
	private List<IModel<S>> wrappedModels;
	
	/**
	 * Getter method for the wrappedModels.
	 * @return the wrappedModels
	 */
	public List<IModel<S>> getWrappedModels() {
		return wrappedModels;
	}
	
	/**
	 * Setter method for the wrappedModels.
	 * @param wrappedModels the wrappedModels to set
	 */
	public void setWrappedModels(List<IModel<S>> wrappedModels) {
		this.wrappedModels = wrappedModels;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#onDetach()
	 */
	@Override
	protected void onDetach() {
		for (IModel<S> wrappedModel : wrappedModels) {
			wrappedModel.detach();
		}
	}
	
}
