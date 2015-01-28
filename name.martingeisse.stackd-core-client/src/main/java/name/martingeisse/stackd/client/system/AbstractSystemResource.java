/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.system;

/**
 * Base implementation for {@link ISystemResource} that prevents
 * double disposal.
 */
public abstract class AbstractSystemResource implements ISystemResource {

	/**
	 * the disposed
	 */
	private boolean disposed;
	
	/**
	 * Constructor.
	 */
	public AbstractSystemResource() {
		this.disposed = false;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.lowlevel.ISystemResource#dispose()
	 */
	@Override
	public final void dispose() {
		if (disposed) {
			throw new IllegalStateException("this resource has already been disposed of");
		}
		internalDispose();
		disposed = true;
	}
	
	/**
	 * Actually disposes of this resource. Only called if not yet disposed.
	 */
	protected abstract void internalDispose();
	
}
