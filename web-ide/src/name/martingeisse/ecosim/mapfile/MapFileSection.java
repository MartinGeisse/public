/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.mapfile;


/**
 * A section from a map file.
 */
public class MapFileSection {

	/**
	 * the mapFile
	 */
	private MapFile mapFile;

	/**
	 * the name
	 */
	private String name;

	/**
	 * the start
	 */
	private int start;

	/**
	 * the size
	 */
	private int size;

	/**
	 * Constructor
	 */
	public MapFileSection() {
	}

	/**
	 * @return Returns the mapFile.
	 */
	public MapFile getMapFile() {
		return mapFile;
	}

	/**
	 * Sets the mapFile.
	 * @param mapFile the new value to set
	 */
	public void setMapFile(MapFile mapFile) {
		this.mapFile = mapFile;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name the new value to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the start.
	 */
	public int getStart() {
		return start;
	}

	/**
	 * Sets the start.
	 * @param start the new value to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return Returns the size.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Sets the size.
	 * @param size the new value to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

}
