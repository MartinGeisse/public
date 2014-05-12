/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.model.relay;

import org.apache.wicket.model.IModel;

/**
 * Forwards get/set calls to another model.
 * 
 * Whether this model also forwards the {@link #detach()} call
 * is decided by the return value of {@link #isForwardDetachCall()}.
 * 
 * Note: No type checking is done by this model itself, although
 * subclasses might add type checking.
 * 
 * @param <T> the model type
 */
public abstract class AbstractRelayModel<T> implements IModel<T> {

	/**
	 * Constructor.
	 */
	public AbstractRelayModel() {
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	@Override
	public void detach() {
		if (isForwardDetachCall()) {
			getTargetModel().detach();
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IModel#getObject()
	 */
	@Override
	public T getObject() {
		return getTargetModel().getObject();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
	 */
	@Override
	public void setObject(final T object) {
		getTargetModel().setObject(object);
	}

	/**
	 * Returns the model to forward the calls to.
	 * 
	 * @return the target model
	 */
	protected abstract IModel<T> getTargetModel();

	/**
	 * Decides whether to forward the {@link #detach()} call.
	 * 
	 * @return true to forward the detach() call, false to ignore it.
	 */
	protected abstract boolean isForwardDetachCall();

}
