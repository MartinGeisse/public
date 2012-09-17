/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.reference;

import name.martingeisse.admin.application.IPlugin;
import name.martingeisse.admin.entity.EntityCapabilities;

/**
 * Base implementation of {@link IEntityReferenceDetector} that also behaves
 * as a plugin that contributes itself.
 */
public abstract class AbstractEntityReferenceDetector implements IEntityReferenceDetector, IPlugin {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.IPlugin#unbox()
	 */
	@Override
	public IPlugin[] unbox() {
		return null;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.application.IPlugin#contribute()
	 */
	@Override
	public void contribute() {
		EntityCapabilities.entityReferenceDetectorCapability.add(this);
	}

}
