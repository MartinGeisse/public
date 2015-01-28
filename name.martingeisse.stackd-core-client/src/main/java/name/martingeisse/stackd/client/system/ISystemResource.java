/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.system;

/**
 * A system resource that can be disposed of. System resources can be put into
 * a {@link SystemResourceNode} for management.
 */
public interface ISystemResource {

	/**
	 * Disposes of this system resource.
	 * @throws IllegalStateException if already disposed
	 */
	public void dispose() throws IllegalStateException;

}
