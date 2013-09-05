/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.sprite;

import name.martingeisse.wicket.application.AbstractMyWicketApplication;

import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * The atlas that contains a number of sprites.
 */
public final class SpriteAtlas {

	/**
	 * the application
	 */
	private final AbstractMyWicketApplication application;
	
	/**
	 * the name
	 */
	private final String name;
	
	/**
	 * the resource
	 */
	private final ByteArrayResource resource;
	
	/**
	 * Constructor.
	 */
	SpriteAtlas(AbstractMyWicketApplication application, String name, String contentType, byte[] data) {
		this.application = application;
		this.name = name;
		this.resource = new ByteArrayResource(contentType, data);
		application.getSharedResources().add(SpriteAtlas.class, name, null, null, null, resource);
	}

	/**
	 * Getter method for the resource.
	 * @return the resource
	 */
	public ByteArrayResource getResource() {
		return resource;
	}
	
	/**
	 * Returns the URL for the atlas image.
	 * @return the URL
	 */
	public CharSequence getUrl() {
		ResourceReference resourceReference = application.getSharedResources().get(SpriteAtlas.class, name, null, null, null, true);
		return RequestCycle.get().urlFor(resourceReference, null);
	}
	
}
