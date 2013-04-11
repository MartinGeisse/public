/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.mapfile;

import java.util.List;

/**
 * 
 */
public class MapFile {

	/**
	 * the sections
	 */
	private List<MapFileSection> sections;

	/**
	 * the symbols
	 */
	private List<MapFileSymbol> symbols;

	/**
	 * Constructor
	 */
	public MapFile() {
	}

	/**
	 * @return Returns the sections.
	 */
	public List<MapFileSection> getSections() {
		return sections;
	}

	/**
	 * Sets the sections.
	 * @param sections the new value to set
	 */
	public void setSections(List<MapFileSection> sections) {
		this.sections = sections;
	}

	/**
	 * @return Returns the symbols.
	 */
	public List<MapFileSymbol> getSymbols() {
		return symbols;
	}

	/**
	 * Sets the symbols.
	 * @param symbols the new value to set
	 */
	public void setSymbols(List<MapFileSymbol> symbols) {
		this.symbols = symbols;
	}
	
	/**
	 * @param sectionName the name of the section to find
	 * @return Returns the section with the specified name, or null if no such section exists
	 */
	public MapFileSection findSectionByName(String sectionName) {
		for (MapFileSection section : sections) {
			if (section.getName().equals(sectionName)) {
				return section;
			}
		}
		return null;
	}
	
	/**
	 * @param symbolName the name of the symbol to find
	 * @return Returns the symbol with the specified name, or null if no such symbol exists
	 */
	public MapFileSymbol findSymbolByName(String symbolName) {
		for (MapFileSymbol symbol : symbols) {
			if (symbol.getName().equals(symbolName)) {
				return symbol;
			}
		}
		return null;
	}

}
