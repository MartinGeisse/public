/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.document;

import name.martingeisse.reporting.datasource.DataSources;

/**
 * Adapter between inline items and block items.
 */
public class Paragraph implements IBlockItem {

	/**
	 * the contents
	 */
	private IInlineItem contents;

	/**
	 * Constructor.
	 */
	public Paragraph() {
	}

	/**
	 * Constructor.
	 * @param contents the contents
	 */
	public Paragraph(final IInlineItem contents) {
		this.contents = contents;
	}

	/**
	 * Getter method for the contents.
	 * @return the contents
	 */
	public IInlineItem getContents() {
		return contents;
	}

	/**
	 * Setter method for the contents.
	 * @param contents the contents to set
	 */
	public void setContents(final IInlineItem contents) {
		this.contents = contents;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.document.IDataBindable#bindToData(name.martingeisse.reporting.datasource.DataSources)
	 */
	@Override
	public Object bindToData(DataSources dataSources) {
		IInlineItem boundContents = (IInlineItem)contents.bindToData(dataSources);
		return (boundContents == contents ? this : new Paragraph(boundContents));
	}

}
