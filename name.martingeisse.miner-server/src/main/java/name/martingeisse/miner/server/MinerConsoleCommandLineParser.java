/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.server;

import name.martingeisse.miner.server.game.InventoryAccess;
import name.martingeisse.miner.server.game.ItemType;

/**
 * Helper class to process the command line.
 */
public final class MinerConsoleCommandLineParser {

	/**
	 * the session
	 */
	private final MinerSession session;
	
	/**
	 * the arguments
	 */
	private final String[] arguments;
	
	/**
	 * the position
	 */
	private int position;

	/**
	 * Constructor.
	 * @param session the session to use for error output
	 * @param arguments the command-line arguments
	 */
	public MinerConsoleCommandLineParser(MinerSession session, String[] arguments) {
		this.session = session;
		this.arguments = arguments;
		this.position = 0;
	}

	/**
	 * Returns the next argument but does not remove it. Returns null if there are no more arguments.
	 * @return the argument or null
	 */
	public String peek() {
		return (position == arguments.length ? null : arguments[position]);
	}

	/**
	 * Fetches and removes the next argument. Returns null if there are no more arguments.
	 * @return the argument or null
	 */
	public String fetch() {
		if (position == arguments.length) {
			return null;
		} else {
			String result = arguments[position];
			position++;
			return result;
		}
	}
	
	/**
	 * Un-fetches the latest argument. More than one argument can be un-fetched, but
	 * trying to move beyond the first argument will throw an {@link IllegalStateException}.
	 */
	public void backOff() {
		if (position == 0) {
			throw new IllegalStateException("cannot back off -- no fetched arguments");
		} else {
			position--;
		}
	}
	
	/**
	 * Tries to fetch an integer argument. If the next argument is an integer, it is
	 * removed and returned. Otherwise the next argument is not an integer or there
	 * isn't even a next argument anymore, and null gets returned.
	 * 
	 * @return the integer or null
	 */
	public Integer tryInteger() {
		String argument = fetch();
		if (argument == null) {
			return null;
		}
		try {
			return Integer.parseInt(argument);
		} catch (NumberFormatException e) {
			backOff();
			return null;
		}
	}
	
	/**
	 * Checks if the next argument is the specified keyword. If so, removes the keyword
	 * and returns true, otherwise leaves the argument in place and returns false.
	 * @param keyword the keyword to look for
	 * @return true if the keyword was found, false if not
	 */
	public boolean tryKeyword(String keyword) {
		String argument = fetch();
		if (argument == null) {
			return false;
		}
		if (argument.equals(keyword)) {
			return true;
		} else {
			backOff();
			return false;
		}
	}
	
	/**
	 * Fetches an argument and parses it as an item type. If the argument is null, this method
	 * returns null, and if optional is false, also prints an error message. If the
	 * argument is non-null but is not a valid item type display name, this function returns
	 * null too and prints an error message on the console. Any keywords that could be used
	 * instead of an item name should be checked before using this method.
	 *  
	 * @param optional whether the argument is optional
	 * @return the item type or null
	 */
	public ItemType fetchItemType(boolean optional) {
		String argument = fetch();
		if (argument == null) {
			if (!optional) {
				session.sendConsoleOutput("missing item type parameter");
			}
			return null;
		}
		final ItemType type = ItemType.getByDisplayName(argument);
		if (type == null) {
			session.sendConsoleOutput("No such item type: " + argument);
		}
		return type;
	}
	
	/**
	 * Fetches an inventory slot indicator that can be either an item type, or
	 * the keyword "inventory" and a slot index. If the 'optional' argument is true,
	 * the slot indicator may be missing, otherwise it is mandatory. This method
	 * prints an appropriate error message to the console in all error cases.
	 * 
	 * @param inventoryAccess the inventory access, used to resolve an item type
	 * display name to a slot index
	 * @param equipped whether to look for an item by type in the equipped items (true) or backpack (false)
	 * @param optional whether the slot indicator is optional
	 * @return the slot index, or -1 on error
	 */
	public int fetchInventorySlot(InventoryAccess inventoryAccess, boolean equipped, boolean optional) {
		if (tryKeyword("inventory")) {
			Integer result = tryInteger();
			if (result == null) {
				session.sendConsoleOutput("found 'inventory' keyword but no slot index");
				return -1;
			} else {
				return result;
			}
		}
		ItemType itemType = fetchItemType(optional);
		if (itemType == null) {
			return -1;
		}
		int result = inventoryAccess.findByType(itemType, equipped);
		if (result < 0) {
			session.sendConsoleOutput("no such item in your " + (equipped ? "equipped" : "backpack") + " items: " + itemType.getDisplayName());
		}
		return result;
	}
	
}
