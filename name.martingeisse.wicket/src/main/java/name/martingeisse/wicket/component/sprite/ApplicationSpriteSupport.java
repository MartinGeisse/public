/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.sprite;

import org.apache.wicket.Application;
import org.apache.wicket.MetaDataKey;

/**
 * Glue code between the CSS sprite code and the Wicket application.
 */
public final class ApplicationSpriteSupport {

	/**
	 * The metadata key for this object.
	 */
	public static final MetaDataKey<ApplicationSpriteSupport> KEY = new MetaDataKey<ApplicationSpriteSupport>() {
	};

	/**
	 * Initializes sprite support for the specified application.
	 * @param application the Wicket application
	 */
	public static void initialize(Application application) {
		application.setMetaData(KEY, new ApplicationSpriteSupport());
	}

	/**
	 * Returns the sprite support object for the application of the calling thread,
	 * or null if none is associated with that application.
	 * 
	 * @return the sprite support object
	 */
	public static ApplicationSpriteSupport get() {
		return get(Application.get());
	}
	
	/**
	 * Returns the sprite support object for the specified application, or null if none
	 * is associated with the application.
	 * 
	 * @param application the Wicket application
	 * @return the sprite support object
	 */
	public static ApplicationSpriteSupport get(Application application) {
		return application.getMetaData(KEY);
	}
	
	/**
	 * the spriteRegistry
	 */
	private final SpriteRegistry spriteRegistry;
	
	/**
	 * Prevent instantiation.
	 */
	private ApplicationSpriteSupport() {
		this.spriteRegistry = new SpriteRegistry();
	}

	/**
	 * Getter method for the spriteRegistry.
	 * @return the spriteRegistry
	 */
	public SpriteRegistry getSpriteRegistry() {
		return spriteRegistry;
	}

}
