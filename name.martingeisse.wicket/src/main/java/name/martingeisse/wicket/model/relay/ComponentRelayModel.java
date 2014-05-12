/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.model.relay;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

/**
 * Delegates get/set calls to the model of another component.
 * The {@link #detach()} method does nothing since it expects
 * the other component to detach on its own.
 * 
 * Note: No type checking is done by this model.
 * 
 * @param <T> the model type
 */
public class ComponentRelayModel<T> extends AbstractRelayModel<T> {

	/**
	 * the target
	 */
	private final Component target;

	/**
	 * Constructor.
	 * @param target the target component
	 */
	public ComponentRelayModel(final Component target) {
		this.target = target;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.model.relay.AbstractRelayModel#getTargetModel()
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected IModel<T> getTargetModel() {
		return (IModel<T>)target.getDefaultModel();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.model.relay.AbstractRelayModel#isForwardDetachCall()
	 */
	@Override
	protected boolean isForwardDetachCall() {
		return false;
	}

}
