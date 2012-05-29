/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.terms;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Generic annotation to specify a displayed name for arbitrary code artifacts
 * to be used in user interfaces.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DisplayName {

	/**
	 * @return the value
	 */
	public String value();
	
}
