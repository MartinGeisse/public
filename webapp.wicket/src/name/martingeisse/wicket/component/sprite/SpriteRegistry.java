/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.sprite;

import java.util.Map;

import org.apache.wicket.request.resource.ResourceReference;

/**
 * Registry for the atlas images that contain CSS sprites. A single
 * instance of this class is typically kept by the application.
 */
public final class SpriteRegistry {

	/**
	 * Constructor.
	 */
	public SpriteRegistry() {
	}

	/*
	 * TODO: Wicket uses a separate resource for style / variation / locate which happen
	 * to access the same resource stream internally. This is fatal for matching these
	 * resources as "known" to the registry.
	 */
}
