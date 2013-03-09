/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.build;

import java.util.Collection;

/**
 * Implementations can be registered in the {@link WorkspaceListenerRegistry}
 * to receive workspace resource deltas like builders do.
 */
public interface IWorkspaceListener {

	/**
	 * This method is invoked when workspace resource deltas are processed.
	 * @param deltas the deltas
	 */
	public void onWorkspaceChange(Collection<BuilderResourceDelta> deltas);
	
}
