/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.document;

/**
 * TODO: document me
 *
 */
public final class Document implements IDataBindable {

	/**
	 * the rootSection
	 */
	private Section rootSection;

	/**
	 * Constructor.
	 */
	public Document() {
		this.rootSection = new Section();
	}

	/**
	 * Constructor.
	 * @param rootSection the root section to use
	 */
	public Document(Section rootSection) {
		this.rootSection = rootSection;
	}
	
	/**
	 * Getter method for the rootSection.
	 * @return the rootSection
	 */
	public Section getRootSection() {
		return rootSection;
	}

	/**
	 * Setter method for the rootSection.
	 * @param rootSection the rootSection to set
	 */
	public void setRootSection(final Section rootSection) {
		this.rootSection = rootSection;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.document.IDataBindable#bindToData()
	 */
	@Override
	public Document bindToData() {
		return new Document((Section)rootSection.bindToData());
	}

}
