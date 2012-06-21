/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.document;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.reporting.datasource.DataSources;

/**
 * A section in the document.
 */
public final class Section implements IDataBindable {

	/**
	 * the title
	 */
	private String title;

	/**
	 * the directContents
	 */
	private BlockSequenceItem directContents;

	/**
	 * the subsections
	 */
	private List<Section> subsections;

	/**
	 * Constructor.
	 */
	public Section() {
		this.title = "<unnamed>";
		this.directContents = new BlockSequenceItem();
		this.subsections = new ArrayList<Section>();
	}

	/**
	 * Getter method for the title.
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter method for the title.
	 * @param title the title to set
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * Getter method for the directContents.
	 * @return the directContents
	 */
	public BlockSequenceItem getDirectContents() {
		return directContents;
	}

	/**
	 * Setter method for the directContents.
	 * @param directContents the directContents to set
	 */
	public void setDirectContents(final BlockSequenceItem directContents) {
		this.directContents = directContents;
	}

	/**
	 * Getter method for the subsections.
	 * @return the subsections
	 */
	public List<Section> getSubsections() {
		return subsections;
	}

	/**
	 * Setter method for the subsections.
	 * @param subsections the subsections to set
	 */
	public void setSubsections(final List<Section> subsections) {
		this.subsections = subsections;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.document.IDataBindable#bindToData(name.martingeisse.reporting.datasource.DataSources)
	 */
	@Override
	public Object bindToData(DataSources dataSources) {
		Section result = new Section();
		result.setTitle(title);
		result.setDirectContents(directContents.bindToData(dataSources));
		result.setSubsections(DocumentUtil.bindToData(dataSources, Section.class, subsections, true));
		return result;
	}

}
