/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.document;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a block item that simply contains other block items.
 */
public class BlockSequenceItem implements IBlockItem {

	/**
	 * the subItems
	 */
	private List<IBlockItem> subItems;

	/**
	 * Constructor.
	 */
	public BlockSequenceItem() {
		this.subItems = new ArrayList<IBlockItem>();
	}

	/**
	 * Constructor.
	 * @param subItems the sub-items to use
	 */
	public BlockSequenceItem(List<IBlockItem> subItems) {
		this.subItems = subItems;
	}

	/**
	 * Getter method for the subItems.
	 * @return the subItems
	 */
	public List<IBlockItem> getSubItems() {
		return subItems;
	}

	/**
	 * Setter method for the subItems.
	 * @param subItems the subItems to set
	 */
	public void setSubItems(final List<IBlockItem> subItems) {
		this.subItems = subItems;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.document.IDataBindable#bindToData()
	 */
	@Override
	public BlockSequenceItem bindToData() {
		final List<IBlockItem> boundSubItems = DocumentUtil.bindToData(IBlockItem.class, subItems, true);
		return (boundSubItems == subItems ? this : new BlockSequenceItem(boundSubItems));
	}

}
