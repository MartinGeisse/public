/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.multi.populator;

/**
 * Base implementation of {@link IEntityCellPopulator} that stores the title.
 */
public abstract class AbstractEntityCellPopulator implements IEntityCellPopulator {

	/**
	 * the title
	 */
	private String title;
	
	/**
	 * Constructor.
	 * @param title the title
	 */
	public AbstractEntityCellPopulator(String title) {
		this.title = title;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.multi.IEntityListCellPopulator#getTitle()
	 */
	@Override
	public String getTitle() {
		return title;
	}

	/**
	 * Setter method for the title.
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

}
