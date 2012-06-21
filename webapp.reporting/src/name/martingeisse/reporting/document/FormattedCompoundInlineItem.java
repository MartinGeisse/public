/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.document;

import java.util.ArrayList;
import java.util.List;

/**
 * This item contains other inline items as well as an inline
 * formatting instruction.
 */
public class FormattedCompoundInlineItem implements IInlineItem {

	/**
	 * the formattingInstruction
	 */
	private InlineFormattingInstruction formattingInstruction;

	/**
	 * the subItems
	 */
	private List<IInlineItem> subItems;

	/**
	 * Constructor.
	 */
	public FormattedCompoundInlineItem() {
		this(InlineFormattingInstruction.NONE);
	}

	/**
	 * Constructor.
	 * @param formattingInstruction the formatting instruction to apply to the sub-items
	 */
	public FormattedCompoundInlineItem(final InlineFormattingInstruction formattingInstruction) {
		this.formattingInstruction = formattingInstruction;
		this.subItems = new ArrayList<IInlineItem>();
	}

	/**
	 * Constructor.
	 * @param formattingInstruction the formatting instruction to apply to the sub-items
	 * @param subItems the sub-item list to use
	 */
	public FormattedCompoundInlineItem(final InlineFormattingInstruction formattingInstruction, final List<IInlineItem> subItems) {
		this.formattingInstruction = formattingInstruction;
		this.subItems = subItems;
	}

	/**
	 * Getter method for the formattingInstruction.
	 * @return the formattingInstruction
	 */
	public InlineFormattingInstruction getFormattingInstruction() {
		return formattingInstruction;
	}

	/**
	 * Setter method for the formattingInstruction.
	 * @param formattingInstruction the formattingInstruction to set
	 */
	public void setFormattingInstruction(final InlineFormattingInstruction formattingInstruction) {
		this.formattingInstruction = formattingInstruction;
	}

	/**
	 * Getter method for the subItems.
	 * @return the subItems
	 */
	public List<IInlineItem> getSubItems() {
		return subItems;
	}

	/**
	 * Setter method for the subItems.
	 * @param subItems the subItems to set
	 */
	public void setSubItems(final List<IInlineItem> subItems) {
		this.subItems = subItems;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.document.IDataBindable#bindToData()
	 */
	@Override
	public FormattedCompoundInlineItem bindToData() {
		final List<IInlineItem> boundSubItems = DocumentUtil.bindToData(IInlineItem.class, subItems, true);
		return (boundSubItems == subItems ? this : new FormattedCompoundInlineItem(formattingInstruction, boundSubItems));
	}

}
