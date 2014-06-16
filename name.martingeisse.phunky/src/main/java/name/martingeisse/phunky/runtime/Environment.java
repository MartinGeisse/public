/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime;

import java.util.HashMap;

/**
 * The environment that keeps variables per function call.
 */
public final class Environment extends HashMap<String, Variable> {

	/**
	 * the runtime
	 */
	private final PhpRuntime runtime;

	/**
	 * Constructor.
	 * @param runtime the PHP runtime
	 */
	public Environment(final PhpRuntime runtime) {
		this.runtime = runtime;
	}

	/**
	 * Getter method for the runtime.
	 * @return the runtime
	 */
	public PhpRuntime getRuntime() {
		return runtime;
	}
	
	/**
	 * Returns a variable, creating it if it doesn't exist yet.
	 * @param name the name of the variable
	 * @return the variable
	 */
	public final Variable getOrCreate(String name) {
		Variable variable = get(name);
		if (variable == null) {
			variable = new Variable();
			put(name, variable);
		}
		return variable;
	}
	
}
