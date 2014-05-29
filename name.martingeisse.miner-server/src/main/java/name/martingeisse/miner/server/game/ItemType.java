/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.miner.server.game;

import java.util.HashMap;
import java.util.Map;

/**
 * Inventory item types.
 */
public enum ItemType {

	/**
	 * the FOO
	 */
	FOO,
	
	/**
	 * the BAR
	 */
	BAR,
	
	/**
	 * the FUPP
	 */
	FUPP;
	
	/**
	 * the typeByDisplayName
	 */
	private static final Map<String, ItemType> typeByDisplayName = new HashMap<String, ItemType>();

	//
	static {
		for (ItemType itemType : values()) {
			typeByDisplayName.put(itemType.getDisplayName(), itemType);
		}
	}

	/**
	 * Returns the item type with the specified display name.
	 * @param displayName the display name
	 * @return the item type
	 */
	public static ItemType getByDisplayName(String displayName) {
		return typeByDisplayName.get(displayName);
	}
	
	/**
	 * the displayName
	 */
	private final String displayName;

	/**
	 * Constructor.
	 */
	private ItemType() {
		this(null);
	}
	
	/**
	 * Constructor.
	 * @param displayName the displayed item name
	 */
	private ItemType(String displayName) {
		if (displayName == null) {
			this.displayName = generateDisplayName(name());
		} else {
			this.displayName = displayName;
		}
	}

	/**
	 * 
	 */
	private static String generateDisplayName(String name) {
		// TODO
		return name;
	}

	/**
	 * Getter method for the displayName.
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	
}
