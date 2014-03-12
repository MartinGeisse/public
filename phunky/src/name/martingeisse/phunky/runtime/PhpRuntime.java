/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime;

import name.martingeisse.phunky.runtime.builtin.EchoFunction;

/**
 * This class represents the whole PHP runtime environment.
 */
public final class PhpRuntime {

	/**
	 * the globalEnvironment
	 */
	private final Environment globalEnvironment;
	
	/**
	 * the functions
	 */
	private final Functions functions;

	/**
	 * Constructor for a standard PHP runtime.
	 */
	public PhpRuntime() {
		this(true);
	}

	/**
	 * Constructor.
	 * @param standardDefinitions whether to apply standard definitions.
	 * Passing true here has the same effect as calling {@link #applyStandardDefinitions()}.
	 */
	public PhpRuntime(boolean standardDefinitions) {
		this.globalEnvironment = new Environment(this);
		this.functions = new Functions(this);
		if (standardDefinitions) {
			applyStandardDefinitions();
		}
	}
	
	/**
	 * Getter method for the globalEnvironment.
	 * @return the globalEnvironment
	 */
	public Environment getGlobalEnvironment() {
		return globalEnvironment;
	}

	/**
	 * Getter method for the functions.
	 * @return the functions
	 */
	public Functions getFunctions() {
		return functions;
	}

	/**
	 * Applies standard definitions to this runtime
	 */
	public void applyStandardDefinitions() {
		functions.put("echo", new EchoFunction());
		// TODO
	}
	
}
