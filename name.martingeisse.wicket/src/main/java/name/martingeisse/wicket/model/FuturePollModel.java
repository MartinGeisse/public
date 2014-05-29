/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.model;

import java.util.concurrent.Future;
import org.apache.wicket.model.AbstractReadOnlyModel;

/**
 * A model that will return the value from a {@link Future}, or null if
 * the future has not finished yet or has failed.
 * 
 * @param <T> the model type, which is also the type returned by the future
 */
public final class FuturePollModel<T> extends AbstractReadOnlyModel<T> {

	/**
	 * the future
	 */
	private final Future<T> future;
	
	/**
	 * Constructor.
	 * @param future the future
	 */
	public FuturePollModel(Future<T> future) {
		this.future = future;
	}

	/**
	 * Getter method for the future.
	 * @return the future
	 */
	public Future<T> getFuture() {
		return future;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.model.AbstractReadOnlyModel#getObject()
	 */
	@Override
	public T getObject() {
		try {
			return future.isDone() ? future.get() : null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
