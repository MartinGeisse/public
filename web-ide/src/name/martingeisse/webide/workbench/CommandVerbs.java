/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench;

import name.martingeisse.common.terms.CommandVerb;

/**
 * Collection of commonly used command verbs.
 */
public final class CommandVerbs {

	/**
	 * Prevent instantiation.
	 */
	private CommandVerbs() {
	}

	/**
	 * The "open" verb.
	 */
	public static final CommandVerb OPEN = new CommandVerb(CommandVerbs.class, "open");

	/**
	 * The "delete" verb.
	 */
	public static final CommandVerb DELETE = new CommandVerb(CommandVerbs.class, "delete");
	
}
