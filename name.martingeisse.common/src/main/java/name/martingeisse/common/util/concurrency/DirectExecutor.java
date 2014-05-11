/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util.concurrency;

import java.util.concurrent.Executor;

/**
 * An {@link Executor} that executes runnables directly in the calling thread.
 */
public final class DirectExecutor implements Executor {

	/* (non-Javadoc)
	 * @see java.util.concurrent.Executor#execute(java.lang.Runnable)
	 */
	@Override
	public void execute(Runnable runnable) {
		runnable.run();
	}
	
}
