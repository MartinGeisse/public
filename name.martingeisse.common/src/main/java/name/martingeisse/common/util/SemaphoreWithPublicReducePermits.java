/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

import java.util.concurrent.Semaphore;

/**
 * A {@link Semaphore} subclass whose {@link #reducePermits(int)} method
 * is public.
 */
public class SemaphoreWithPublicReducePermits extends Semaphore {

	/**
	 * Constructor.
	 * @param permits the initial number of permits
	 * @param fair whether this semaphore is fair
	 */
	public SemaphoreWithPublicReducePermits(final int permits, final boolean fair) {
		super(permits, fair);

	}

	/**
	 * Constructor.
	 * @param permits the initial number of permits
	 */
	public SemaphoreWithPublicReducePermits(final int permits) {
		super(permits);

	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Semaphore#reducePermits(int)
	 */
	@Override
	public void reducePermits(final int reduction) {
		super.reducePermits(reduction);
	};

}
