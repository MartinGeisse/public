/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.mapfile;


/**
 * A single symbol entry from a map file.
 */
public class MapFileSymbol {

	/**
	 * the mapFile
	 */
	private MapFile mapFile;

	/**
	 * the name
	 */
	private String name;

	/**
	 * the sectionName
	 */
	private String sectionName;

	/**
	 * the offset
	 */
	private int offset;

	/**
	 * Constructor
	 */
	public MapFileSymbol() {
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
	 * @return Returns the sectionName.
	 */
	public String getSectionName() {
		return sectionName;
	}

	/**
	 * Sets the sectionName.
	 * @param sectionName the new value to set
	 */
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	/**
	 * @return Returns the offset.
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Sets the offset.
	 * @param offset the new value to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	/**
	 * @return Returns the absolute address of this symbol. This uses the mapFile
	 * reference to find the section that contains this symbol.
	 * @throws IllegalStateException if no map file is set, or if that
	 * map file does not contain a section with the name specified in this symbol
	 */
	public int resolveAddress() {
		
		/** ensure that a map file exists **/
		if (mapFile == null) {
			throw new IllegalStateException("no parent map file");
		}
		
		/** find the section **/
		MapFileSection section = mapFile.findSectionByName(sectionName);
		if (section == null) {
			throw new IllegalStateException("map file does not contain a section named " + sectionName);
		}
		
		/** return the absolute address **/
		return section.getStart() + offset;

	}

}
