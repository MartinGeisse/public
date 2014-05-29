/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.common.network;


/**
 * Selector for the different data objects associated with a section.
 */
public enum SectionDataType {

	/**
	 * The definitive data for a section. This is typically stored only
	 * on the server and not transmitted to the client to prevent
	 * information cheating.
	 */
	DEFINITIVE,
	
	/**
	 * In interactive image that contains all visible information, colliders,
	 * and simplified second-layer cubes for smooth interpolation while digging cubes.
	 * This type is used for the section the player is in, as well as neighboring
	 * sections when the player gets close to them.
	 */
	INTERACTIVE,
	
	/**
	 * This image type contains all visible information but nothing else. It is
	 * used for sections that are close enough to be visible with all details,
	 * but not close enough for type {@link #INTERACTIVE}. 
	 */
	VIEW_LOD_0;
	
	/**
	 * the storageId
	 */
	private final String storageId;

	/**
	 * Constructor.
	 */
	private SectionDataType() {
		this.storageId = name().toLowerCase().replace('_', '-');
	}

	/**
	 * Getter method for the storageId.
	 * @return the storageId
	 */
	public String getStorageId() {
		return storageId;
	}
	
}
