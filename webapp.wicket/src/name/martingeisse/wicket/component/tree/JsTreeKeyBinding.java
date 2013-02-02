/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.tree;

import name.martingeisse.common.terms.CommandVerb;

/**
 * An instance of this class represents a binding between a hotkey
 * and a command verb.
 */
final class JsTreeKeyBinding {

	/**
	 * the hotkey
	 */
	private final String hotkey;

	/**
	 * the commandVerb
	 */
	private final CommandVerb commandVerb;

	/**
	 * Constructor.
	 * @param hotkey the hotkey
	 * @param commandVerb the command verb
	 */
	JsTreeKeyBinding(final String hotkey, final CommandVerb commandVerb) {
		this.hotkey = hotkey;
		this.commandVerb = commandVerb;
	}

	/**
	 * Getter method for the hotkey.
	 * @return the hotkey
	 */
	String getHotkey() {
		return hotkey;
	}
	
	/**
	 * Getter method for the commandVerb.
	 * @return the commandVerb
	 */
	CommandVerb getCommandVerb() {
		return commandVerb;
	}
	
}
