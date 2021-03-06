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
	 * The "new file" verb.
	 */
	public static final CommandVerb NEW_FILE = new CommandVerb(CommandVerbs.class, "new file");
	
	/**
	 * The "new folder" verb.
	 */
	public static final CommandVerb NEW_FOLDER = new CommandVerb(CommandVerbs.class, "new folder");
	
	/**
	 * The "open" verb.
	 */
	public static final CommandVerb OPEN = new CommandVerb(CommandVerbs.class, "open");

	/**
	 * The "delete" verb.
	 */
	public static final CommandVerb DELETE = new CommandVerb(CommandVerbs.class, "delete");
	
	/**
	 * The "rename" verb.
	 */
	public static final CommandVerb RENAME = new CommandVerb(CommandVerbs.class, "rename");
	
	/**
	 * The "run" verb.
	 */
	public static final CommandVerb RUN = new CommandVerb(CommandVerbs.class, "run");
	
}
