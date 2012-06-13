/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to invoke an {@link IAutoformComponentBuildPostProcessor}
 * after the components of an autoform have been built. It must be tagged to the
 * autoform data bean.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoformComponentBuildPostProcessor {

	/**
	 * @return the value
	 */
	public Class<? extends IAutoformComponentBuildPostProcessor> value();
	
}
