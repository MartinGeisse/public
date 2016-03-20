/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime;

import java.util.HashMap;

/**
 * This map keeps globally defined constants.
 */
public final class Constants extends HashMap<String, Object> {

	/**
	 * the runtime
	 */
	private final PhpRuntime runtime;

	/**
	 * Constructor.
	 * @param runtime the PHP runtime
	 */
	public Constants(final PhpRuntime runtime) {
		this.runtime = runtime;
	}

	/**
	 * Getter method for the runtime.
	 * @return the runtime
	 */
	public PhpRuntime getRuntime() {
		return runtime;
	}
	
}
